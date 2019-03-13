import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class CommandManager {
    Scanner input;

    public CommandManager(File file){
        try {
            this.input = new Scanner(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public String getNextCommand(){
        return input.hasNext() ? input.next() : null;
    }
}
