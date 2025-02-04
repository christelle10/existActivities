
import java.util.Scanner;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class Main {
    public static void main(String[] args) throws Exception {
        Scanner scanner = new Scanner(System.in);

        TableService table = new TableService();
        MenuService menu = new MenuService();
        
        //input
        System.out.print("Input table dimension: ");
        String dimension = scanner.next(); 
       
        //generating the table // identify why i used static 
        //String[][] table1 = table.createTable(dimension);
        //int row = table1.length;
        //int col = table1[0].length;
        table.createTable(dimension);
        
        //printing the table
        System.out.println("\n Generated table: ");
        table.printTable(table.finalTable, table.finalRow, table.finalCol);
        char option = '0';

        //printing of menu
        do {
            table.menuPrinter();
            System.out.print("Action: ");
            option = scanner.next().charAt(0);
            menu.handleOption(option, table.finalTable, table.finalRow, table.finalCol, scanner);
        }

        while (option != 'x');
           
        
        scanner.close();
    }

    
}

    




