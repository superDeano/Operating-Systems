import java.io.*;
import java.nio.file.DirectoryNotEmptyException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.util.Scanner;

public class DiskMemory {
    private File file;

    DiskMemory(String path) {
        this.file = new File(path);
    }

    //To store a variable in the disk
    public synchronized void store(Variable var) {
        try {
            FileWriter fr = new FileWriter(file, true);
            BufferedWriter br = new BufferedWriter(fr);
            br.write(var.toString()+"\n");

            br.close();
            fr.close();
        } catch (IOException e) {
            System.out.println("Disk Exception [store]: " + e.getMessage());
        }
    }

    // Goes through the file (disk) to look for the variable
    public synchronized Variable lookup(int id) {
        Variable toReturn = null;
        try {
            Scanner input = new Scanner(file);
            while (input.hasNext()) {
                String var = input.nextLine();
                if (Integer.parseInt(var.split("&")[0]) == id) {
                    toReturn = new Variable(var);
                }
            }
            input.close();
        } catch (FileNotFoundException e) {
            System.out.println("Disk Exception [read]: " + e.getMessage());
            return null;
        }
        return toReturn;
    }

    public synchronized void release(int id) {

        try {
            //Construct the new file that will later be renamed to the original filename.
            File tempFile = new File(file.getAbsolutePath() + ".tmp");

            FileReader fileReader = new FileReader(file);
            FileWriter fileWriter = new FileWriter(tempFile);

            BufferedReader br = new BufferedReader(fileReader);
            PrintWriter pw = new PrintWriter(fileWriter);

            String line = null;

            //Read from the original file and write to the new
            //unless content matches data to be removed.
            while ((line = br.readLine()) != null) {

                if (Integer.parseInt(line.split("&")[0]) != id) {
                    pw.println(line);
                    pw.flush();
                }
            }
            pw.close();
            br.close();
            fileReader.close();
            fileWriter.close();

            //Delete the original file

            try{
                Files.delete(file.toPath());
            } catch (NoSuchFileException x) {
               System.out.println("Delete Failed: no such  file or directory");
            } catch (DirectoryNotEmptyException x) {
                System.out.println("Error: Directory not empty when deleting file");
            } catch (IOException x) {
                // File permission problems are caught here.
                System.err.println(x);
            }
//            if (!file.delete()) {
//                System.out.println("Could not delete file");
//                return;
//            }

            //Rename the new file to the filename the original file had.
            if (!tempFile.renameTo(file))
                System.out.println("Could not rename file");

        } catch (FileNotFoundException ex) {
            System.out.println("Disk Exception [release]: " + ex.getMessage());
        } catch (IOException ex) {
            System.out.println("Disk Exception [release]: " + ex.getMessage());
        }
    }

    //Deletes the whole disk
    public synchronized void nukeDisk() {
        try {
            PrintWriter writer = new PrintWriter(file);
            writer.print("");
            writer.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}
