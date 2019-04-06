import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

public class Scheduler implements Runnable {
    List<Process> processes = new ArrayList<Process>();
    boolean noCommands = false;

    @Override
    public void run() {
        setup();
        MemoryManager.getDiskMemoryInstance().nukeDisk();

        for (int time = 0; true; time += 1000) {

            if (noCommands && allProcessFinished()) {
                //No more commands
                System.out.println("No more commands, program is finished");
                break;
            }

            notify(time);

            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }

    /**
     * To get the commands before the processes start running
     * To set up the virtual memory
     */
    private void setup() {
        try {
            CommandManager.setup(new File("Testing/commands.txt"));
            CommandManager.subscribe(this);
            MemoryManager.setup(new File("Testing/memconfig.txt"), "Testing/disk.txt");
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

    /* To basically know when to end the program as there will be no commands to be executed */
    public void receiveCommandUpdate(int i) {
        if (i == 0) {
            noCommands = true;
        }
    }

    /* To know whether all the processes are done */
    private boolean allProcessFinished() {
        return processes.stream().allMatch(process -> !process.isRunning());
    }
}

