import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

public class Scheduler implements Runnable {
    CommandManager commandManager;
    List<Process> processes = new ArrayList<Process>();
    boolean noCommands = false;

    @Override
    public void run() {
        setup();
        MemoryManager.getDiskMemoryInstance().nukeDisk();

        for (int time = 0; true; time += 50) {

            if(noCommands){
                //No more commands
                System.out.println("No more commands, program is finished");
                break;
            }

            notify(time);

            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void setup() {
        try {
            CommandManager.setup(new File("./Testing/commands.txt"));
            CommandManager.subscribe(this);
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

    public void receiveCommandUpdate(int i){
        if(i == 0){
            noCommands = true;
        }
    }
}
