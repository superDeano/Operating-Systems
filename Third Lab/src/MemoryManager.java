import java.io.File;
import java.io.FileNotFoundException;
import java.time.Instant;
import java.util.Scanner;
import java.util.concurrent.Semaphore;

public class MemoryManager {
    private static Variable[] mainMemory;
    private static Semaphore lock = new Semaphore(1);
    private static DiskMemory disk;

    MemoryManager() {
    }

    public static void setup(File memConfig, File diskFile) throws FileNotFoundException {
            Scanner input = new Scanner(memConfig);
            mainMemory = new Variable[input.nextInt()]; // Creates main mainMemory of Pages
            disk = new DiskMemory(diskFile);
    }


    public static Variable store(int id, int value) {
        boolean couldWrite = false;
        Variable toStore = new Variable(id, value);
        try {
            lock.acquire();
            for (int i = 0; i < mainMemory.length; i++) {
                if (mainMemory[i] == null) {
                    mainMemory[i] = toStore;
                    couldWrite = true;
                    break;
                }
            }
            if(!couldWrite){
                disk.store(toStore);
            }

        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally{
            lock.release();
            return toStore;
        }


    }

    public static Variable release(int id) {
        boolean released = false;
        Variable toRelease = new Variable(id, null);

        try {
            lock.acquire();
            for (int i = 0; i < mainMemory.length; i++) {
                if (mainMemory[i].getId()== id) {
                    mainMemory[i] = null;
                    released = true;
                    break;
                }
            }
            if(!released){
                disk.release(id);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }finally {
            lock.release();
            return toRelease;
        }
    }

    public static Variable lookup(int id) {
        Variable toReturn = null;
        boolean found = false;
        try {

            lock.acquire();
            for (Variable var : mainMemory) {
                if (var.getId() == id) {
                    toReturn = var;
                    found = true;
                    break;
                }
            }

            if(!found){ // Not found in Main Memory
                Variable temp = disk.lookup(id); //Gets it from disk

                if(mainMemoryHasSpace()){
                    store(temp.id, temp.value);
                    disk.release(temp.id);
                    return temp;
                }else { // Has to swap
                    disk.release(id);

                    Variable oldestVar = findOldestVariable();

                    disk.store(oldestVar);
                    release(oldestVar.id);
                    store(temp.id, temp.value);

                    toReturn = temp;
                }
            }

        } catch (InterruptedException e) {
            e.printStackTrace();
        }finally{
            lock.release();
        }

        return toReturn;

    }

    private static boolean mainMemoryHasSpace(){
        for(int i = 0; i < mainMemory.length; i++){
            if(mainMemory[i] == null){
                return true;
            }
        }
        return false;
    }

    private static Variable findOldestVariable() {
        Instant min = mainMemory[0].getLastAccess();
        int index = 0;

        for (int i = 1; i < mainMemory.length; i++) {
            if (mainMemory[i].getLastAccess().isBefore(min)) {
                min = mainMemory[i].getLastAccess();
                index = i;
            }
        }
        return mainMemory[index];
    }
}

