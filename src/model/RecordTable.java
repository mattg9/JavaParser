package model;
import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * This class represents a record table
 * Holds a list of the records in the table
 * Keeps track of the list of columns in the table
 * 
 */
public class RecordTable {
    private ArrayList<Record> records;
    private ArrayList <String> columns;

    
    /**
     * Constructs a Record table with an empty list of records and columns.
     */
    public RecordTable() {
        this.records = new ArrayList<Record>();
        this.columns = new ArrayList <String>();
    }

    /**
     * Determine whether a record already exists with the given ID
     * @param Id - Id to search for in the table
     * @return true if record found, false if no record is found
     */
    public boolean containsID(String Id) {
        return this.records.stream()
            .filter(r -> r != null && 
                r.getField("ID") != null && 
                Objects.equals(r.getField("ID"), Id))
            .findFirst()
            .isPresent();
    }

    /**
     * Return a record with matching Id in the table
     * @param Id - Id to search for in the table
     * @throws Exception no record found
     * @return a Record that matched the Id filter
     */
    public Record getRecord(String Id) throws RecordNotFoundException {
        return this.records.stream()
            .filter(r -> r != null && 
                r.getField("ID") != null && 
                Objects.equals(r.getField("ID"), Id))
            .findFirst()
            .orElseThrow(() -> new RecordNotFoundException("Record with ID " + Id + " not found."));
    }

    /**
     * Return a record with matching Id in the table
     * @param Id - Id to search for in the table
     * @return a Record that matched the Id filter, exception thrown otherwise
     */
    public void updateColumns(ArrayList<String> columns) {
        columns.stream()
                .filter(c -> !this.columns.contains(c))
                .forEach(this.columns::add);
    }

    public void addRecord(Record record) {
        this.records.add(record);
    }

    public ArrayList<Record> getRecords() {
        return this.records;
    }

    public void sort(String Id) {
        // sort my columns
        if (this.columns.contains(Id)) {
            this.columns.remove(Id);
        }
        this.columns.add(0, Id);
        // sort my records
        Collections.sort(this.records, 
            Comparator.comparing(r -> r.getField(Id)));
    }

    public void print() {
        sort("ID");
        for (Record record : this.records) {
            record.print();
        }
    }

    public void exportToCSV(String filename){
        // Check first before modifying an existing file
        if (this.records.isEmpty()) {
            System.err.println("No records found to export");
            return;
        }

        sort("ID");
        try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(
            new FileOutputStream(filename), "UTF-8"))) {
            // Write columns
            writer.write(this.columns.stream().collect(Collectors.joining(",")));
            writer.newLine();
            // Write records
            for (Record record : this.records) {
                writer.write(this.columns.stream()
                    .map(c -> record.getField(c))
                    .collect(Collectors.joining(",")));
                writer.newLine();
            }
            System.out.println("Records have been written to " + filename);
        } catch (IOException e) {
            System.err.println("Error writing records to CSV: " + e.getMessage());
        }
    }
}

class RecordNotFoundException extends RuntimeException {
    public RecordNotFoundException(String message) {
        super(message);
    }
}