package com.exist;
import java.io.File;
import java.io.FileNotFoundException;  
import java.io.IOException; 
import java.util.Scanner; 



public class App 
{
    public static void main( String[] args )
    {
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
        menu.printTable(table);


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
