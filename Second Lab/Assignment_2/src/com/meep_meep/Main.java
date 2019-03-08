package com.meep_meep;

import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        final int firstColumnIndex = 0;
        final int secondColumnIndex = 1;
        int userCounter = 0;
        ArrayList<User> users = new ArrayList<>();

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
            System.out.println("Quantum time is " + quantumTime);
            String[] lineReadArray;

            while (readingLine.hasNextLine()) {
                String fullLine = readingLine.nextLine();
                lineReadArray = fullLine.split("\\s", 2);
                String first = lineReadArray[firstColumnIndex].trim();

                System.out.println(fullLine);
//                System.out.println("First is "+ first);

                if (isAlpha(first)) {

                    String userName = lineReadArray[firstColumnIndex].trim();
                    int numOfProcesses = Integer.parseInt(lineReadArray[secondColumnIndex].trim());
                    users.add(new User(userName, numOfProcesses));

                    System.out.println("here");
                    System.out.println("User: " + userName + " number of Processes: " + numOfProcesses);

                    Process processes[] = new Process[numOfProcesses];
                    int index = 0;
                    while (index < numOfProcesses) {
                        fullLine = readingLine.nextLine();
                        System.out.println(fullLine);
                        lineReadArray = fullLine.split("\\s", 2);
                        int startTime = Integer.parseInt(lineReadArray[firstColumnIndex].trim());
                        int duration = Integer.parseInt(lineReadArray[secondColumnIndex].trim());

                        System.out.println("Starting: " + startTime + " duration: " + duration);
                        processes[index] = new Process(users.get(userCounter), index, startTime, duration);
                        index++;
                    }
                    users.get(userCounter).setProcesses(processes);
                    System.out.println(userCounter + 1 +  " users created");
                    userCounter++;
                }
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

