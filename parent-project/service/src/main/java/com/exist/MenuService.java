package com.exist; 
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.List;
import java.util.Random;
import java.util.ArrayList;
import java.util.Comparator;
import java.io.IOException;
import java.util.InputMismatchException;
public class MenuService {
    Scanner scanner = new Scanner(System.in);

    public void handleOption(String option, TableModel table, FileService file, String fileName) { 
        switch (option.toLowerCase()) { // for case-insensitive matching
            case "search":
                System.out.print("Search term: ");
                String pattern = scanner.nextLine();
                search(table, pattern);
                break;

            case "edit":
                edit(table, file, fileName);
                break;

            case "add row":
                addRow(table, file, fileName);
                break;

            case "sort":
                sortRow(table, file, fileName);
                break;

            case "print":
                printTable(table);
                break;

            case "reset":
                reset(table, file, fileName);
                break;

            case "x":
                System.out.println("App will be closing any second now...");
                break;

            default:
                System.out.println("Please enter a valid menu option.");
                break;
        }
    }

    public void menuPrinter() {
        System.out.println("\n MENU :\n" + 
                        "[ search ] - Search\n" + 
                        "[ edit ] - Edit\n" + 
                        "[ add_row ] - Add Row\n" + 
                        "[ sort ] - Sort\n" + 
                        "[ print ] - Print\n" + 
                        "[ reset ] - Reset\n" + 
                        "[ x ] - Exit\n");
    }
    public void printTable(TableModel table) {
        List<List<TableModel.Pair>> rows = table.getRows();

        for (int i = 0; i < rows.size(); i++) {
            List<TableModel.Pair> row = rows.get(i);
            for (int j = 0; j < row.size(); j++) {
                TableModel.Pair pair = row.get(j);
                System.out.print(pair.key + "," + pair.value + "   ");
            }
            System.out.println();
        }
    }

    
    private void search(TableModel table, String pattern_) {
        boolean matchFound = false;
        String escapedPattern = Pattern.quote(pattern_);
        Pattern pattern = Pattern.compile(escapedPattern);

        // access rows using the getter method
        List<List<TableModel.Pair>> rows = table.getRows();
        
     
        for (int i = 0; i < rows.size(); i++) {
            List<TableModel.Pair> row = rows.get(i);

            for (int j = 0; j < row.size(); j++) {
                TableModel.Pair pair = row.get(j);
                
                // match pattern against the key and value
                Matcher matcherKey = pattern.matcher(pair.key);
                Matcher matcherValue = pattern.matcher(pair.value);

                // count the matches for key and value
                long matchCountKey = matcherKey.results().count();
                long matchCountValue = matcherValue.results().count();

                // print output
                if (matchCountKey > 0) {
                    System.out.println(matchCountKey + " <" + pattern_ + "> at key of [" + i + "," + j + "]");
                    matchFound = true;
                }
                if (matchCountValue > 0) {
                    System.out.println(matchCountValue + " <" + pattern_ + "> at value of [" + i + "," + j + "]");
                    matchFound = true;
                }
            }
        }

        // if no matches were found
        if (!matchFound) {
            System.out.println("No occurrences found.");
        }
    }

    public boolean dimensionCheck(String dimension) {
        // Adjust regex to match the format [3,3] or [1,1]
        String inputPattern = "^\\[\\d+,\\d+\\]$";  // Matches [3,3] or [1,1]
        Pattern regex = Pattern.compile(inputPattern);
        Matcher correctInput = regex.matcher(dimension);

        // Validate the format of the dimension string
        if (!correctInput.matches()) {
            return false;
        }
        else {
            return true;
        }
    }

    private void edit(TableModel table, FileService file, String fileName) {
        int repRow = -1, repCol = -1;
        boolean correctDimensions = false; 
        boolean correctDim_format = false;
        boolean correctDim_bounds = false;
        // dimension input validation
        do {
            System.out.print("\nEdit: ");
            String repDimension = scanner.next();

            scanner.nextLine();

            // validate the dimension input
            correctDim_format = dimensionCheck(repDimension);
            if (!correctDim_format) {
                System.out.println("Invalid input of dimensions. Enter correct format, e.g.: [3,3]");
                continue;
            }
            else {
                correctDim_format = true;
            }
            // remove the square brackets and split by the comma
            repDimension = repDimension.substring(1, repDimension.length() - 1); // remove '[' and ']'
            String[] repResult = repDimension.split(",");  // split by comma

            try {
                repRow = Integer.parseInt(repResult[0]);  // Get the row index
                repCol = Integer.parseInt(repResult[1]);  // Get the column index
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Row and column must be integers.");
                continue;  // If the input is invalid, continue the loop
            }

            // handling if repRow is out of bounds of the table
            if (repRow >= table.getRows().size()) {
                System.out.println("Entered row out of bounds of the table. Row must be < " + table.getRows().size());
                continue;
            }

            // handling if repCol is out of bounds for the specific row
            if (repCol >= table.getRows().get(repRow).size()) {
                System.out.println("Entered column out of bounds for the selected row. Column must be < " + table.getRows().get(repRow).size());
                continue;
            }

            correctDim_bounds = true;  // Valid row and column indices

            if (correctDim_format && correctDim_bounds) {
                correctDimensions = true;
            }
        }
        while (!correctDimensions);
        

        // Asking whether to edit the key or the value
        String editType = null;
        boolean correctKeyValueBoth = false;

        do {
            System.out.print("Key, value, or both: ");
            editType = scanner.next().toLowerCase();
            if (!("key".equals(editType)) && !("value".equals(editType)) && !("both".equals(editType))) {
                System.out.println("Invalid input! Please enter 'key', 'value', or 'both'.");
            } else {
                correctKeyValueBoth = true;
            }
        }
        while (!correctKeyValueBoth);

        
        
        scanner.nextLine();  

        System.out.print("Input new value: ");
        String replacement = scanner.nextLine();

        // get the row and the pair that shall be replaced from the table
        TableModel.Pair pair = table.getRows().get(repRow).get(repCol);
        String previousKey = pair.key;
        String previousValue = pair.value;

        // Edit based on user input (key, value, or both)
        if ("key".equals(editType)) {
            pair.key = replacement; // Replace the key
            System.out.println("Output: " + previousKey + " → " + pair.key);
        } else if ("value".equals(editType)) {
            pair.value = replacement; // Replace the value
            System.out.println("Output: " + previousValue + " → " + pair.value);
        } else if ("both".equals(editType)) {
            pair.key = replacement; // Replace both key and value
            pair.value = replacement;
            System.out.println("Output: " + previousKey + "," + previousValue + " → " + pair.key + "," + pair.value);
        } 

    file.writeToFile(table, fileName); // Call method to write the table to file
    }

    private void addRow(TableModel table, FileService file, String fileName) {
        Random random = new Random();
        boolean correctRowIndex = false;
        int startIndex = -1;
        do {
            System.out.print("Enter the row index to start inserting new rows: ");
            try {
                startIndex = scanner.nextInt(); 
                if (startIndex < 0 || startIndex > table.getRows().size() - 1) {
                    System.out.println("Invalid row index! It must be between 0 and " + (table.getRows().size() - 1));
                } else {
                    correctRowIndex = true; // Valid input, exit the loop
                }
            } catch (InputMismatchException e) {
                System.out.println("Please input a valid integer as input.");
                scanner.next(); 
            }
        } while (!correctRowIndex);
        
        int rowCount = -1;
        boolean validInput = false;
        do {
            System.out.print("Enter the number of rows to add: ");
            try {
                rowCount = scanner.nextInt();

                if (rowCount < 0) {
                    System.out.println("Error: Please enter a positive integer.");
                } else {
                    validInput = true; 
                }
            } catch (InputMismatchException e) {
                System.out.println("Error: Please input a valid integer.");
                scanner.next();
            }
        } while (!validInput);

        

        // determine the maximum number of columns in the current table (useful for when there's an uneven no. of columns & rows)
        // the maximum number of columns will be the one followed for the newly generated row
        int maxColumns = 0;
        for (List<TableModel.Pair> row : table.getRows()) {
            maxColumns = Math.max(maxColumns, row.size());
        }

        // add rows with randomly generated ascii content
        for (int i = 0; i < rowCount; i++) {
            List<TableModel.Pair> newRow = new ArrayList<>();

            // Fill the new row with the maximum number of columns
            for (int j = 0; j < maxColumns; j++) {
                newRow.add(new TableModel.Pair(file.generateRandomAscii(random, 3), file.generateRandomAscii(random, 3))); // Placeholder values
            }

            // insert the new row at the specified index
            insertRow(table, startIndex + i, newRow); 
        }

        System.out.println(rowCount + " new rows added starting at index " + startIndex + ".");

       
        file.writeToFile(table, fileName); 
        printTable(table);
    }

    private void insertRow(TableModel table, int index, List<TableModel.Pair> row) {
        if (index < 0 || index > table.getRows().size()) { 
            throw new IndexOutOfBoundsException("Invalid index for inserting a row.");
        }
        table.getRows().add(index, row); 
    }

    private void sortRow(TableModel table, FileService file, String fileName) {
        boolean correctInput = false;
        int rowIndex = -1;
        String order = "";

        // regular expression for input: "Row to sort: <index> - <order>"
        Pattern pattern = Pattern.compile("(\\d+)\\s*-\\s*(asc|desc)", Pattern.CASE_INSENSITIVE);

        // prompt user for row index and sorting order 
        do {
            System.out.print("Row to sort: ");
            String input = scanner.nextLine().trim();


            
            Matcher matcher = pattern.matcher(input);

            if (matcher.matches()) {
                rowIndex = Integer.parseInt(matcher.group(1));  
                order = matcher.group(2).toLowerCase();        

               
                if (rowIndex >= 0 && rowIndex < table.getRows().size()) {
                    correctInput = true;  // Input is valid
                } else {
                    System.out.println("Invalid row index! It must be between 0 and " + (table.getRows().size() - 1));
                }
            } else {
                
                System.out.println("Invalid input format. Please use the format: Row to sort: <index> - <asc|desc>");
            }
        } while (!correctInput);

        
        List<TableModel.Pair> row = table.getRows().get(rowIndex);

        // create a comparator using a final order value (needed to be declared as final for it to be used in lambda)
        final String finalOrder = order;  // Final or effectively final variable
        // use of lambda 
        Comparator<TableModel.Pair> comparator = (pair1, pair2) -> {
            // Concatenate key and value to compare as a single string
            String concatenated1 = pair1.key + pair1.value;
            String concatenated2 = pair2.key + pair2.value;

            // Compare the concatenated key-value pairs based on the sorting order
            if (finalOrder.equals("asc")) {
                return concatenated1.compareTo(concatenated2); // ascending order
            } else {
                return concatenated2.compareTo(concatenated1); // descending order
            }
        };

        // sort the row using the comparator
        row.sort(comparator);

        // after sorting, print the sorted row to ensure it's correct
        System.out.println("Sorted row " + rowIndex + " (" + order + "):");
        for (TableModel.Pair pair : row) {
            System.out.print(pair.key + "," + pair.value + "   ");
        }
        System.out.println();

        // after sorting, write the updated table to the file
        file.writeToFile(table, fileName);

        printTable(table);
    }

    private void reset(TableModel table, FileService file, String fileName) {

        
        int rowCount = 0;
        int colCount = 0;
        boolean correctRowColumnFormat = false;

        do {
            
            System.out.print("Enter table dimensions (e.g., 3x3): ");
            String dimensionInput = scanner.nextLine();

          
            correctRowColumnFormat = newDimensionCheck(dimensionInput);
            if (!correctRowColumnFormat) {
                System.out.println("Please enter dimensions in correct format. e.g: 3x4");
                continue;
            }

            String[] dimensions = dimensionInput.split("x");
            rowCount = Integer.parseInt(dimensions[0]);
            colCount = Integer.parseInt(dimensions[1]);

            
            if (rowCount <= 0 || colCount <= 0) {
                System.out.println("Row and column numbers must be positive integers.");
            } else {
                break;
            }
            
        } while (true);
        try {
            //clear the current table object to empty state
            clearTable(table);

            Random random = new Random();

           
            for (int row = 0; row < rowCount; row++) {
                List<TableModel.Pair> rowContent = new ArrayList<>();

                for (int col = 0; col < colCount; col++) {
                    // Generate random key and value
                    String key = file.generateRandomAscii(random, 3);  // 3 random characters for key
                    String value = file.generateRandomAscii(random, 3); // 3 random characters for value

                    
                    rowContent.add(new TableModel.Pair(key, value));
                }

                // Add the generated row to the table
                addRowsOnTable(table, rowContent);
            }

            file.writeToFile(table, fileName);
            printTable(table);
        } catch (Exception e) {
            System.out.println("An error occurred while writing the table to the file.");
            e.printStackTrace();
        }
    }

    private void addRowsOnTable(TableModel table, List<TableModel.Pair> row) {
        table.getRows().add(row); 
    }

    private void clearTable(TableModel table) {
        table.getRows().clear();  
    }

    private boolean newDimensionCheck(String dimension) {
        String inputPattern = "^\\d+x\\d+$"; // Use regex to match format "3x3" (positive numbers only)
        Pattern regex = Pattern.compile(inputPattern);
        Matcher correctInput = regex.matcher(dimension);
        if (!correctInput.matches()) {
            return false;
        }
        else {
            return true;
        }
    }




}  
