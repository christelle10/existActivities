
import java.util.Scanner;

public class MenuService {
//doesn't rely on instance variables, but it handles logic that may be extended in the future (e.g., overriding in a subclass).could benefit from being an instance method to allow flexibility.
    public void handleOption (char option, String[][] table, int row, int col, Scanner scanner) { //this method is used in Main.java so i declared it as public. If i were to include MenuService & TableService in 1 package, i could change it to default
        switch (option) { 
            case '1':
                handleSearch(table, row, col, scanner);
                break;

            case '2':
                handleEdit(table, row, col, scanner);
                break;

            case '3':
                TableService.printTable(table, row, col);
                break;

            case '4':
                handleNewTable(scanner);
                break;

            case 'x':
                System.out.println("Program will close now...");
                System.exit(0);
                break;

            default:
                System.out.println("Invalid action. Enter a valid option.");
                break;
        }
    } 

    private static void handleSearch(String[][] table, int row, int col, Scanner scanner) { //this is declared as private because we will only use this method inside the MenuService
        System.out.print("Search: ");
        String pattern = scanner.next();
        // searching for pattern matches
        TableService.search(table, pattern, row, col);
    }

    private static void handleEdit(String[][] table, int row, int col, Scanner scanner) { //remove static if private
        int repRow, repCol;
        while (true) {
            System.out.print("\nEdit: ");
            String repDimension = scanner.next();
            // validating dimension input
            TableService.dimensionCheck(repDimension);
            String[] repResult = repDimension.split("x");
            repRow = Integer.parseInt(repResult[0]);
            repCol = Integer.parseInt(repResult[1]);
            // handling if repRow, repCol is out of bounds of table
            if (repRow >= table.length || repCol >= table[0].length) {
                System.out.println("Entered dimension out of bounds of original table. Row must be <" + table.length + " and column <" + table[0].length);
            } else {
                break;
            }
        }

        System.out.print("Value: ");
        String replacement = scanner.next();
        // Did not create a new method for edit to ensure that original table array is being replaced, not just its copy. *OLD*
        // ^ did not use a global variable for the table array to keep it simple *OLD*
        String previous = table[repRow][repCol]; // get the original value to be used for output later
        table[repRow][repCol] = replacement;
        System.out.println("Output: " + previous + " → " + table[repRow][repCol]);
    }

//generates a new table, and its logic could be extended or overridden in the future. making it non-static would allow for better flexibility.
    private void handleNewTable(Scanner scanner) {
        System.out.print("Input NEW table dimension: ");
        String newDimension = scanner.next();
        TableService.createTable(newDimension);
        System.out.println("\nGenerated table: ");
        TableService.printTable(TableService.finalTable, TableService.finalRow, TableService.finalCol);
    }
}
