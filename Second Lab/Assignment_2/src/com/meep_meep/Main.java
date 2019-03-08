package com.meep_meep;

import java.io.File;
import java.io.PrintWriter;
import java.util.List;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        final int firstColumnIndex = 0;
        final int secondColumnIndex = 1;

        //Used to open the Input file
        File inputFile;

        //Getting file from the Program Argument
        if (args.length > 1) {
            String totalPathName = "";

            //Adds the spaces in the file path location if there is from the program argument
            for (int eachArgument = 0; eachArgument < args.length; eachArgument++) {
                totalPathName += args[eachArgument];

                //To not add a space at the end
                if (eachArgument + 1 < args.length) {
                    totalPathName += " ";
                }
            }
            inputFile = new File(totalPathName);

        } else {
            //If there are no space in the program argument
            inputFile = new File(args[0]);
        }

        try (Scanner readingLine = new Scanner(inputFile)) {
            int quantumTime = Integer.parseInt(readingLine.nextLine());

            Scheduler scheduler = new Scheduler(quantumTime);

            String[] lineSplitArray;

            while (readingLine.hasNextLine()) {
                //Reads the full line
                String fullLine = readingLine.nextLine();
                //Splits the line into two parts based on the tabs or spaces
                lineSplitArray = fullLine.split("\\s", 2);

                //First part without spaces
                String first = lineSplitArray[firstColumnIndex].trim();


                if (isAlpha(first)) {

                    String userName = lineSplitArray[firstColumnIndex].trim();
                    int numOfProcesses = Integer.parseInt(lineSplitArray[secondColumnIndex].trim());

                    int index = 0;
                    while (index < numOfProcesses) {
                        //Reads full line
                        fullLine = readingLine.nextLine();
                        //Splits the line into two parts
                        lineSplitArray = fullLine.split("\\s", 2);
                        int startTime = Integer.parseInt(lineSplitArray[firstColumnIndex].trim());
                        int duration = Integer.parseInt(lineSplitArray[secondColumnIndex].trim());

                        scheduler.addProcess(new Process(userName, index, startTime, duration));
                        index++;
                    }
                }
            }
            scheduler.start();

            List<String> log = scheduler.getLog();
            PrintWriter writeToFile = new PrintWriter("Output.txt");
            for (String line: log){
                writeToFile.println(line);
            }
            

        } catch (Exception e) {
            System.out.println(e);
        }
    }


    /*
     * Function which checks if a string does not contain numbers*/
    private static boolean isAlpha(String name) {
        char[] chars = name.toCharArray();

        for (char c : chars) {
            if (!Character.isLetter(c)) {
                return false;
            }
        }
        return true;
    }
}

