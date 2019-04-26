import java.io.File;
import java.io.FileNotFoundException;
import java.time.Instant;
import java.util.Scanner;
import java.util.concurrent.Semaphore;

public class MemoryManager {
    private static Variable[] mainMemory;
    private static Semaphore lock = new Semaphore(1);
    private static Semaphore lookingForOldVariable = new Semaphore(1);
    private static DiskMemory disk;

    MemoryManager() {
    }

    /*
     * Sets up the main memory and disk before the processes start running
     * */

    public static void setup(File memConfig, String diskFile) throws FileNotFoundException {
        Scanner input = new Scanner(memConfig);
        mainMemory = new Variable[input.nextInt()]; // Creates main mainMemory of Pages
        disk = new DiskMemory(diskFile);

        input.close();
    }

    public static DiskMemory getDiskMemoryInstance() {
        return disk;
    }

    /**
     * Can only be executed by a process at once
     * First tries to store the variable in the main memory
     * If there's no space, it then stores it in the disk
     */
    public static Variable store(int id, int value) {
        boolean canWriteInMainMemory = false;
        boolean alreadyWrittenInMainMemory = false;
        Variable toStore = new Variable(id, value);

        try {
            lock.acquire();

            if (variableAlreadyExists(id)) {

                for (Variable v : mainMemory) {
                    if (v.getId() == id) {
                        v.setValue(value);
                        alreadyWrittenInMainMemory = true;
                        return toStore;
                    }
                }
                if (!alreadyWrittenInMainMemory) {
                    disk.store(id, value);
                    return toStore;
                }

            } else {
                for (int i = 0; i < mainMemory.length; i++) {
                    if (mainMemory[i] == null) {
                        mainMemory[i] = toStore;
                        canWriteInMainMemory = true;
                        break;
                    }
                }
                if (!canWriteInMainMemory) {
                    disk.store(toStore);
                }
            }

        } catch (InterruptedException e) {
            //interrupted
        } finally {
            lock.release();
        }
        return toStore;
    }

    /**
     * Can only be executed by a process at once
     * First try to see if the variable is in main memory
     * If it is, releases it and leaves that space for free
     * If the variable is in the disk, the variable is released
     */
    public static Variable release(int id) {
        boolean released = false;
        Variable toRelease = new Variable(id, null);

        try {
            lock.acquire();
            for (int i = 0; i < mainMemory.length; i++) {
                if (mainMemory[i].getId() == id) {
                    mainMemory[i] = null;
                    released = true;
                    break;
                }
            }
            if (!released) {
                disk.release(id);
            }
        } catch (InterruptedException e) {
            //interrupted
        } finally {
            lock.release();
        }
        return toRelease;
    }

    /**
     * Can also be executed by only one process at a time
     * First looks for the variable in main memory and returns it if present
     * If not in main memory, looks in the disk,
     * Then puts it in main memory if there is free space
     * If not, does a swap with the oldest accessed variable in main memory before readying
     */
    public static Variable lookup(int id) {
        Variable toReturn = null;
        boolean found = false;
        try {

            lock.acquire();

            //Variable has to be present somewhere to do these
            if (variableAlreadyExists(id)) {
                for (Variable var : mainMemory) {
                    if (var.getId() == id) {
                        toReturn = var;
                        found = true;
                        break;
                    }
                }

                if (!found) { // Not found in Main Memory
                    Variable temp = disk.lookup(id); //Gets it from disk

                    if (mainMemoryHasSpace()) {
                        store(temp.id, temp.value);
                        disk.release(temp.id);
                        return temp;

                    } else { // Has to swap
                        disk.release(id); //Deletes from disk

                        lookingForOldVariable.acquire();
                        int oldestVarIndex = findOldestVariableIndex();
                        lookingForOldVariable.release();

                        Variable oldestVar = mainMemory[oldestVarIndex];
                        /**
                         * There was a swap, the resulting Variable will tell the process that a swapped happened
                         * The process will then be able to print it
                         * */
                        Integer[] swapping = new Integer[2];
                        swapping[Variable.oldVariable] = oldestVar.getId();
                        swapping[Variable.newVariable] = id;
                        temp.setSwapped(swapping);

                        disk.store(oldestVar);
//                        release(oldestVar.id);
                        temp.updateLastAccessed();
                        mainMemory[oldestVarIndex] = temp;

                        toReturn = temp;
                    }
                }
            }

        } catch (InterruptedException e) {
            //got interrupted
        } finally {
            lock.release();
        }

        return toReturn;

    }


    private static boolean mainMemoryHasSpace() {
        for (int i = 0; i < mainMemory.length; i++) {
            if (mainMemory[i] == null) {
                return true;
            }
        }
        return false;
    }

    private static Variable findOldestVariable() {
        Instant min = Instant.now();
        int index = 0;

        for (int i = 0; i < mainMemory.length; i++) {
            if (mainMemory[i].getLastAccess().isBefore(min)) {
                min = mainMemory[i].getLastAccess();
                index = i;
            }
        }
        return mainMemory[index];
    }

    private static int findOldestVariableIndex() {

        int index = 0;
        System.out.println(index +" : last access for variable "+ mainMemory[index].getId() + " is : " + mainMemory[index].getLastAccess());

        System.out.println((index +1 ) +" : last access for variable "+ mainMemory[index + 1].getId() + " is : " + mainMemory[index + 1].getLastAccess());


        Instant lastAccessed = mainMemory[index].getLastAccess();


        for (int i = 1; i < mainMemory.length; i++) {
            System.out.println("In for loop");

            String meh = (mainMemory[i].getLastAccess().isBefore(lastAccessed) ? " : true." : " : false.");

            System.out.println(meh);

            if (mainMemory[i].getLastAccess().isBefore(lastAccessed)) {

                lastAccessed = mainMemory[i].getLastAccess();
                index = i;
            }
        }
        System.out.println("Index is " + index);
        return index;
    }

    /**
     * Function which checks whether a variable is already present in main memory or disk
     * Checks the id so the value can be overridden instead of duplicating the variable
     */
    private static boolean variableAlreadyExists(int id) {

        for (Variable v : mainMemory) {
            if (v != null) {
                if (id == v.getId()) {
                    System.out.println("Variable of ID : " + id + " already exist in Main memory");
                    return true;
                }
            }
        }

        return disk.variableExists(id);
    }
}

