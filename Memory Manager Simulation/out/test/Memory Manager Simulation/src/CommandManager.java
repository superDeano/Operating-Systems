import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class CommandManager {
    static private Queue<Command> commands = new ArrayDeque<>();
    static private Scheduler observer;

    public CommandManager() {
    }

    public static void setup(File file) throws FileNotFoundException {
        Scanner input = new Scanner(file);

        while (input.hasNextLine()) {
            ArrayList<String> str_command = new ArrayList<String>(Arrays.asList(input.nextLine().split(" ")));
            String command = str_command.get(0);

            //Creates a new sub-array of integer for the arguments of the command
            Integer[] args = Arrays.copyOf(str_command.subList(1, str_command.size()).stream().map(Integer::new).toArray(), str_command.subList(1, str_command.size()).stream().map(Integer::new).toArray().length, Integer[].class);
            commands.add(new Command(command.toLowerCase(), args));
        }
    }

    public static Command getNextCommand() {
        Command toReturn = commands.poll();
        update(commands.size()); //Keeps track of how many commands are left

        return toReturn;
    }

    public static void subscribe(Scheduler obj){
        observer = obj;
    }

    public static void update(int num){
        observer.receiveCommandUpdate(num);
    }
}
