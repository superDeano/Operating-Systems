import java.io.*;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        try {
            Scheduler scheduler = new Scheduler();
            BufferedWriter writer = new BufferedWriter(new FileWriter("output.txt"));

            File file = new File("processes.txt");
            Scanner input = new Scanner(file);

            //Discard Number as we dont care;
            input.nextLine();

            for(int i = 1; input.hasNext(); i++){
                String[] temp = input.nextLine().split(" ");
                scheduler.addProcess(new Process(i, Integer.parseInt(temp[0]), Integer.parseInt(temp[1]),writer));
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }catch (IOException ex){
            ex.printStackTrace();
        }
    }
}
