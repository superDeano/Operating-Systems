import java.util.concurrent.Semaphore;

public class MemoryManager {

    Semaphore lock = new Semaphore(1);
    MemoryManager(){}

    public void store(){}
    public void release(){}
    public void lookup(){}
}

