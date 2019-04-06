import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

public class Scheduler implements Runnable {
    List<Process> processes = new ArrayList<Process>();

    @Override
    public void run() {
        setup();

        for (int time = 0; true; time += 1000) {

            notify(time);

            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void setup() {
        try {
            CommandManager.setup(new File("./Testing/commands.txt"));
            MemoryManager.setup(new File("./Testing/memconfig.txt"), new File("./Testing/disk.txt"));
        } catch (FileNotFoundException ex) {
            System.out.println(ex.getMessage());
        }
    }

    public void addProcess(Process p) {
        processes.add(p);
    }

    private void notify(int time) {
        processes.forEach(process -> process.check(time));
    }
}

