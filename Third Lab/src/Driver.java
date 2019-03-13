import java.io.File;
import java.util.List;

public class Driver implements Runnable{
    CommandManager commandManager;
    List<Process> processes;
    @Override
    public void run() {
        for(int time = 0; true; time+=50){


            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void setup(){
        commandManager = new CommandManager(new File("commands.txt"));
    }
}
