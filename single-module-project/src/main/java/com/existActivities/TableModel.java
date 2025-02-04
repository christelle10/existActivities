package com.existActivities; 
import java.util.ArrayList;
import java.util.List;
public class TableModel {

    // each row (also a list) IS a list of key-value pairs
    private List<List<Pair>> rows;

    // constructor to initialize the Table with rows
    public TableModel() {
        this.rows = new ArrayList<>();
    }

    // getter for row
    public List<List<Pair>> getRows() {
        return rows;
    }

    
    public void addRow(List<Pair> row) {
        rows.add(row); 
    }


    // Pair class to represent a key-value pair
    public static class Pair {
        String key;
        String value;

        Pair(String key, String value) {
            this.key = key;
            this.value = value;
        }
    }
}
