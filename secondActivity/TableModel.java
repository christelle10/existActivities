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

    // inserting row at specific index
	public void insertRow(int index, List<Pair> row) {
	    if (index < 0 || index > rows.size()) { 
	        throw new IndexOutOfBoundsException("Invalid index for inserting a row.");
	    }
	    rows.add(index, row);
	}


    
    public void printTable() {
        // Iterate through each row
        for (int i = 0; i < rows.size(); i++) {
            List<Pair> row = rows.get(i);

            // Iterate through each key-value pair in the row
            for (int j = 0; j < row.size(); j++) {
                Pair pair = row.get(j);
                System.out.print(pair.key + "," + pair.value + "   ");
            }
            System.out.println();
        }
    }

    public void clearTable() {
        rows.clear(); 
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
