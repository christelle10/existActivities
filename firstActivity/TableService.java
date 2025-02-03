
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
public class TableService { 
    //declared as public to allow Activity1.java & MenuService.java to access it. If i were to put tableservice.java, menuservice.java and activity1.java to the same package, i can use the default (package-private) access modifier instead
    //these are all utility methods that doesn't need new instances of the method, so they are declared as static, except for createTable
    
    public static String[][] finalTable;
    public static int finalRow, finalCol;
    public static String asciiGenerator() { //identify the best modifier

        Random rand = new Random();
        //generate 3 random ints,  convert generated ints into ascii character, 
        char a = (char) (rand.nextInt((126-33)+1) + 33); //upper limit bound should be ascii's limit which is 126
        char b = (char) (rand.nextInt((126-33)+1) + 33);
        char c = (char) (rand.nextInt((126-33)+1) + 33);

        //concatenate the chars to a string 
        String result = "" + a + b + c;
        return result;
        
        
    }
    public void menuPrinter() {

        System.out.println("\n MENU :\n" + //
                        "[ 1 ] - Search\n" + //
                        "[ 2 ] - Edit\n" + //
                        "[ 3 ] - Print\n" + //
                        "[ 4 ] - Reset\n" + //
                        "[ x ] - Exit\n" + //
                        "");
    

    }
    public static void search(String given[][], String pattern_, int row, int col) {
        //use java regex method pattern create the pattern from user input
        boolean matchFound = false; //flag for handling of no occurences found
        String escapedPattern = Pattern.quote(pattern_);
        Pattern pattern = Pattern.compile(escapedPattern); //match special characters literally 
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < col; j++) {
                //search for a match on each iterated elements of the table array, count matches
                Matcher matcher = pattern.matcher(given[i][j]);
                long matchCount = matcher.results().count();
                //only print for successful matches
                if (matchCount > 0) {
                    System.out.println(matchCount + " Occurence/s at [ " + i + "," + j + " ]");
                    matchFound = true;
                }
            }
        }

        //handling for no occurences found
        if (!matchFound) {
            System.out.println("No occurences found.");
        }
            
    }

    public static void printTable(String given[][], int row, int col) {
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < col; j++) {
                System.out.print(given[i][j] + "   ");
            }
            System.out.print("\n");
        }

    }

    public static void createTable(String dimension) {
        //validating input if its in correct format
        dimensionCheck(dimension);

        //use delimiter to seperate two dimension numbers
        String[] result = dimension.split("x");

        //cast the seperated numbers as int type
        finalRow = Integer.parseInt(result[0]);
        finalCol = Integer.parseInt(result[1]);
        String[][] table = new String[finalRow][finalCol];

        for (int i = 0; i < finalRow; i++) {
            for (int j = 0; j < finalCol; j++) {
                table[i][j] = asciiGenerator();
            }
        }
        finalTable = table;
    }

    public static void dimensionCheck(String dimension) {
        String inputPattern = "^\\d+x\\d+$"; //use regex to match format "3x3" //handle negative numbers

        Pattern regex = Pattern.compile(inputPattern);
        Matcher correctInput = regex.matcher(dimension);
        if (!correctInput.matches()) {
            System.out.print("Invalid input of dimensions. Enter correct format, e.g.: 3x3.");

            System.exit(0);
        }
    }

}