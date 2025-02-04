import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.io.File;
import java.util.Random;
import java.io.FileWriter;

public class FileService {
    public void fileValidator(String[] commandArg) {
        try {
            String fileName = commandArg[0];
            File customFile = new File(fileName);
            if (!customFile.exists()) {
                System.out.println("The specified file: " + fileName +"does not exist. Please input valid file name.");
                System.exit(0);
            }

        } 
         catch (Exception e) {
            System.out.println("An unexpected error occurred.");
            e.printStackTrace();
        }

}

    
    public String createFile() {
        try {
            String newFileName = "table.txt";
            File newFile = new File(newFileName);

            if (newFile.createNewFile()) {
                System.out.println("File created: " + newFile.getName());

                // generate random ASCII table and write to file
                try (FileWriter writer = new FileWriter(newFile)) {
                    Random random = new Random();

                    // create the 3x3 table
                    for (int row = 0; row < 3; row++) {
                        StringBuilder rowContent = new StringBuilder();

                        for (int col = 0; col < 3; col++) {
                            // Generate random key and value
                            String key = generateRandomAscii(random, 3);  // 3 random characters
                            String value = generateRandomAscii(random, 3);

                            // create key-value pair
                            String pair = key + "\u0001" + value;

                            // append pair to row, separate by control character, > 0 ensures we control character is added in between columns
                            if (col > 0) rowContent.append("\u0009");
                            rowContent.append(pair);
                        }

                        // write the whole row to file
                        writer.write(rowContent.toString() + "\n");
                    }
                }
                return newFileName;
            } else {
                System.out.println("File already exists.");
            }
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
        return null; // Return null if file creation fails
    }

    // generate random ASCII string of given length
    public String generateRandomAscii(Random random, int length) {
        StringBuilder asciiString = new StringBuilder();

        for (int i = 0; i < length; i++) {
            // Generate random ASCII character in the range 32-126
            char randomChar = (char) (32 + random.nextInt(95));
            asciiString.append(randomChar);
        }
        return asciiString.toString();
    }

    public TableModel fileProcessor(String filePath, TableModel table) {
        

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;

            // each line read represents a row 
            while ((line = reader.readLine()) != null) {
                List<TableModel.Pair> row = new ArrayList<>();
                StringBuilder key = new StringBuilder();
                StringBuilder value = new StringBuilder();
                boolean isKey = true; // flag for starting the processing of key

                // processing each character in the line
                for (int i = 0; i < line.length(); i++) {
                    char current = line.charAt(i);

                    if (current == '\u0001') {
                        // Switch from key to value
                        isKey = false;
                    } else if (current == '\u0009') {
                        // Switch to the next pair
                        row.add(new TableModel.Pair(key.toString(), value.toString()));
                        key.setLength(0); // clear the key & value Stringbuilder buffer
                        value.setLength(0); 
                        isKey = true; // start processing the next key
                    } else {
                        // append the current printable character
                        if (isKey) {
                            key.append(current);
                        } else {
                            value.append(current);
                        }
                    }
                }

                // add the last pair in the line, if any (why it's needed: no '\u0009 at the end of the line that signals that the pair is complete')
                if (key.length() > 0 || value.length() > 0) {
                    row.add(new TableModel.Pair(key.toString(), value.toString()));
                }

                
                table.addRow(row);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // return the processed table
        return table;
}

    public void writeToFile(TableModel table, String fileName) {
        try (FileWriter writer = new FileWriter(fileName)) {  
            // iterate over the rows and write each row to the file
            for (List<TableModel.Pair> row : table.getRows()) {
                StringBuilder rowContent = new StringBuilder();

                for (int i = 0; i < row.size(); i++) {
                    TableModel.Pair pair = row.get(i);
                    rowContent.append(pair.key).append("\u0001").append(pair.value);  
                    if (i < row.size() - 1) {
                        rowContent.append("\u0009"); 
                    }
                }

               
                writer.write(rowContent.toString() + "\n");
            }

            System.out.println("Table successfully updated in the file.");
        } catch (IOException e) {
            System.out.println("An error occurred while writing to the file.");
            e.printStackTrace();
        }
    }

   
}
