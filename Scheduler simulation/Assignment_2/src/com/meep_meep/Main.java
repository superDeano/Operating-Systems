package com.meep_meep;

import java.io.File;
import java.io.BufferedWriter;
import java.util.List;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        final int firstPart = 0;
        final int secondPart = 1;

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
                String first = lineSplitArray[firstPart].trim();


                if (isAlpha(first)) {

                    String userName = lineSplitArray[firstPart].trim();
                    int numOfProcesses = Integer.parseInt(lineSplitArray[secondPart].trim());

                    int index = 0;
                    while (index < numOfProcesses) {
                        //Reads full line
                        fullLine = readingLine.nextLine();
                        //Splits the line into two parts
                        lineSplitArray = fullLine.split("\\s", 2);
                        int startTime = Integer.parseInt(lineSplitArray[firstPart].trim());
                        int duration = Integer.parseInt(lineSplitArray[secondPart].trim());

                        scheduler.addProcess(new Process(userName, ++index, startTime, duration, null));

                    }
                }
            }
            scheduler.start();

            List<String> log = scheduler.getLog();
            //Writing the output

        } catch (Exception e) {
            System.out.println(e);
        }
    }


    /*
     * Function which checks if a string does not contain numbers
     */
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

