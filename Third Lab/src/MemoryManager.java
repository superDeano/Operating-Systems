import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.concurrent.Semaphore;

public class MemoryManager {
    Variable[] memory;
    Semaphore lock = new Semaphore(1);

    MemoryManager(File file) {
        try {
            Scanner input = new Scanner(file);
            memory = new Variable[input.nextInt()];

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void store(int id, int value) {
        try {

            lock.acquire();
            for (int i = 0; i < memory.length; i++) {
                if (memory[i] == null) {
                    memory[i] = new Variable(id, value);
                    return;
                }
            }
            System.out.println("Memory is full and you are an awful engineer to NOT check your memory ");
            lock.release();

        } catch (InterruptedException e) {
            e.printStackTrace();
        }


    }

    public void release(int id) {
        try {

            lock.acquire();
            for (int i = 0; i < memory.length; i++) {
                if (memory[i].getId() == id) {
                    memory[i] = null;
                }
            }
            lock.release();

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public Variable lookup(int id) {
        Variable toReturn = null;
        try {

            lock.acquire();
            for (Variable var : memory) {
                if (var.getId() == id) {
                    toReturn = var;
                }
            }
            lock.release();

        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return toReturn;

    }
}

