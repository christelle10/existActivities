package com.existActivities;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class FileService {

    public void fileValidator(String[] commandArg) {
        try {
            String fileName = commandArg[0];
            File customFile = FileUtils.getFile(fileName);

            if (!customFile.exists() || !customFile.isFile()) {
                System.out.println("The specified file: " + fileName + " does not exist or is not a valid file. Please input a valid file name.");
                System.exit(0);
            }
        } catch (Exception e) {
            System.out.println("An unexpected error occurred.");
            e.printStackTrace();
        }
    }
    
   public String createFile() {
        try {
            String newFileName = "table.txt";
            File newFile = new File(newFileName);

            if (!newFile.exists()) { // Check if the file does not already exist
                // Generate random ASCII table content
                StringBuilder content = new StringBuilder();
                Random random = new Random();

                // Create the 3x3 table
                for (int row = 0; row < 3; row++) {
                    for (int col = 0; col < 3; col++) {
                        // Generate random key and value
                        String key = generateRandomAscii(random, 3); // 3 random characters
                        String value = generateRandomAscii(random, 3);

                        // Create key-value pair
                        content.append(key).append("\u0001").append(value);

                        if (col < 2) {
                            content.append("\u0009"); // Tab character between columns
                        }
                    }
                    content.append("\n");
                }

                // Write content to the file using Apache Commons IO
                FileUtils.writeStringToFile(newFile, content.toString(), StandardCharsets.UTF_8);
                System.out.println("File created: " + newFile.getName());
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

    // Generate random ASCII string of given length
    String generateRandomAscii(Random random, int length) {
        StringBuilder asciiString = new StringBuilder();

        for (int i = 0; i < length; i++) {
            // Generate random ASCII character in the range 32-126
            char randomChar = (char) (32 + random.nextInt(95));
            asciiString.append(randomChar);
        }
        return asciiString.toString();
    }

    public TableModel fileProcessor(String filePath, TableModel table) {
        try {
            // Read file content
            List<String> lines = FileUtils.readLines(new File(filePath), StandardCharsets.UTF_8);

            for (String line : lines) {
                List<TableModel.Pair> row = new ArrayList<>();
                String[] pairs = line.split("\u0009"); // Split by tab character

                for (String pair : pairs) {
                    String[] keyValue = pair.split("\u0001", 2); // Split by control character
                    String key = keyValue.length > 0 ? keyValue[0] : "";
                    String value = keyValue.length > 1 ? keyValue[1] : "";
                    row.add(new TableModel.Pair(key, value));
                }

                table.addRow(row);
            }
        } catch (IOException e) {
            System.out.println("An error occurred while processing the file.");
            e.printStackTrace();
        }

        return table;
    }

    public void writeToFile(TableModel table, String fileName) {
        try {
            StringBuilder content = new StringBuilder();

            for (List<TableModel.Pair> row : table.getRows()) {
                for (int i = 0; i < row.size(); i++) {
                    TableModel.Pair pair = row.get(i);
                    content.append(pair.key).append("\u0001").append(pair.value);

                    if (i < row.size() - 1) {
                        content.append("\u0009"); // Tab character
                    }
                }
                content.append("\n");
            }

            // Write the updated content to the file
            FileUtils.writeStringToFile(new File(fileName), content.toString(), StandardCharsets.UTF_8);
            System.out.println("Table successfully updated in the file.");
        } catch (IOException e) {
            System.out.println("An error occurred while writing to the file.");
            e.printStackTrace();
        }
    }
}
