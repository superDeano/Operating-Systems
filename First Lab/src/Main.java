import java.io.File;
import java.io.PrintWriter;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        //Used to open the Input file
        File inputFile;

        //Getting file from the Program Argument
        if (args.length > 1) {
            String totalPathName = "";

            //Adds the spaces in the file path location if there is from the program argument
            for (int eachArgument = 0; eachArgument < args.length; eachArgument++) {
                totalPathName += args[eachArgument];

                //To not add a space at the end
                if (eachArgument + 1 < args.length){
                    totalPathName += " ";
                }
            }

            inputFile = new File(totalPathName);

        } else {
            //If there are no space in the program argument
            inputFile = new File(args[0]);
        }

        //Buffer to read the file
        try (Scanner readingFile = new Scanner(inputFile)) {

            //Gets the number of numbers from the first line from the Input.txt file
            int lengthOfArray = Integer.parseInt(readingFile.nextLine());

            //Creates the array
            int[] unsortedArray = new int[lengthOfArray];

            int index = 0;

            do {
                //Putting the numbers from the file into the array
                unsortedArray[index] = Integer.parseInt(readingFile.nextLine());

            } while (readingFile.hasNextLine() && index++ < lengthOfArray);

            //sorting the array using the quick sort algorithm
            QSort.sort(unsortedArray);

            //Creating the Output.txt file
            PrintWriter writeToFile = new PrintWriter("Output.txt");

            //Writing the result to the Output.txt file
            for (int lineNumber = 0; lineNumber < unsortedArray.length; lineNumber++) {
                writeToFile.println(unsortedArray[lineNumber]);
            }
            //Closing the file
            writeToFile.close();

        } catch (Exception e) {
            System.out.print(e);
        }
    }
}
