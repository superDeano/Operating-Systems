package com.meep_meep;

import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        final int firstColumn = 0;
        final int secondColumn = 1;
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

        try (Scanner readingLine = new Scanner( inputFile)) {
            int quantumTime = Integer.parseInt(readingLine.nextLine());

            Scheduler scheduler = new Scheduler(quantumTime);

            String[] lineRead;

            while(readingLine.hasNextLine()){

                lineRead = readingLine.nextLine().split("\t");

                if(isAlpha(lineRead[firstColumn].trim())){

                String userName = lineRead[firstColumn].trim();
                int numOfProcesses = Integer.parseInt(lineRead[secondColumn]);
                users.add(new User(userName, numOfProcesses));

                while (numOfProcesses != 0){

                }

                }else {
                    users[userCounter].setPr
                }

            }


        } catch (Exception e) {
            System.out.println(e);
        }
    }


    private static boolean isAlpha(String name) {
        char[] chars = name.toCharArray();

        for (char c : chars) {
            if(!Character.isLetter(c)) {
                return false;
            }
        }
        return true;
    }
}

