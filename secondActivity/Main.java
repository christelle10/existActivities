import java.io.File;  // Import the File class
import java.io.FileNotFoundException;  // Import this class to handle errors
import java.io.IOException; // Import IOException
import java.util.Scanner; // Import the Scanner class to read text files

public class Main {
    public static void main(String[] args) {
        FileService file = new FileService();
        TableModel table = new TableModel();
        MenuService menu = new MenuService();
        Scanner scanner = new Scanner(System.in);
        String fileName = null;

        //*********** COMMAND-LINE ARGUMENTS INPUT *********** //

        //user does not provide filename
        if (args.length == 0 || args[0].isEmpty()) {
            try { 
                // create a new file
                fileName = file.createFile();
                if (fileName == null) { 
                    throw new Exception("Failed to create the default file.");
                }
                System.out.println("Default file created: " + fileName);
            } catch (Exception e) {
                System.out.println("An error occurred while creating the default file.");
                return;
            }
        } else { //user provides filename 
            file.fileValidator(args);  
            fileName = args[0];
        }
        table = file.fileProcessor(fileName, table);
        //after successful read, print contents of file in table format
        table.printTable();


        //******************* MENU INPUT *******************//
        String option = null;

        //printing of menu
        do {
            menu.menuPrinter();
            System.out.print("Action: ");
            option = scanner.nextLine();
            menu.handleOption(option, table, file, fileName);
        }

        while (!option.equals("x"));

    }
}
