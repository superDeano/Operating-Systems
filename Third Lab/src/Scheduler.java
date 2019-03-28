import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;

public class Scheduler implements Runnable{
    CommandManager commandManager;
    List<Process> processes;
    @Override
    public void run() {
        setup();

        for(int time = 0; true; time+=50){

            notify(time);

            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void setup(){
        try {
            CommandManager.setup(new File("commands.txt"));
            MemoryManager.setup(new File("memoryConfig.txt"), new File("disk.txt"));
        }catch (FileNotFoundException ex){
            System.out.println(ex.getMessage());
        }
    }

    public void addProcesse(Process p){
        processes.add(p);
    }

    private void notify(int time){
        processes.forEach(process -> process.check(time));
    }
}
