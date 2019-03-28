import java.io.*;
import java.util.Scanner;

public class DiskMemory {
    private File file;

    DiskMemory(File file) {
        this.file = file;
    }

    public void store(Variable var) {
        try {
            FileWriter fr = new FileWriter(file, true);
            BufferedWriter br = new BufferedWriter(fr);
            br.write(var.toString());

            br.close();
            fr.close();
        } catch (IOException e) {
            System.out.println("Disk Exception [Store]: " + e.getMessage());
        }
    }

    public Variable lookup(int id) {
        try {
            Scanner input = new Scanner(file);
            while (input.hasNext()) {
                String var = input.nextLine();
                if (Integer.parseInt(var.split("&")[0]) == id) {
                    return new Variable(var);
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("Disk Exception [read]: " + e.getMessage());
            return null;
        }
        return null;
    }

    public void release(int id) {

        try {
            //Construct the new file that will later be renamed to the original filename.
            File tempFile = new File(file.getAbsolutePath() + ".tmp");

            BufferedReader br = new BufferedReader(new FileReader(file));
            PrintWriter pw = new PrintWriter(new FileWriter(tempFile));

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

            //Delete the original file
            if (!file.delete()) {
                System.out.println("Could not delete file");
                return;
            }

            //Rename the new file to the filename the original file had.
            if (!tempFile.renameTo(file))
                System.out.println("Could not rename file");

        } catch (FileNotFoundException ex) {
            System.out.println("Disk Exception [release]: " + ex.getMessage());
        } catch (IOException ex) {
            System.out.println("Disk Exception [release]: " + ex.getMessage());
        }
    }

    public void nukeDisk() {
        try {
            PrintWriter writer = new PrintWriter(file);
            writer.print("");
            writer.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}
