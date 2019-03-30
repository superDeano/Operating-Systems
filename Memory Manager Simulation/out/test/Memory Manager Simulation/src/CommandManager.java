import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class CommandManager {
    static private Queue<Command> commands = new ArrayDeque<>();

    public CommandManager(){
    }

    public static void setup(File file) throws FileNotFoundException {
        Scanner input = new Scanner(file);

        while(input.hasNext()){
            ArrayList<String> str_command = new ArrayList<String>(Arrays.asList(input.nextLine().split(" ")));
            String command = str_command.get(0);

            //Creates a new sub-array of integer for the arguments of the command
            Integer[] args = (Integer[]) str_command.subList(1, str_command.size()).stream().map(Integer::new).toArray();

            commands.add(new Command(command,args));
        }
    }

    public static Command getNextCommand(){
        return commands.poll();
    }
}
