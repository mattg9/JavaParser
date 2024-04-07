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
 * This class represents a table of records
 * It maintains a list of records and keeps track of a list of columns in the table
 * 
 */
public class RecordTable {
    private ArrayList<Record> records;
    private ArrayList <String> columns;

    /**
     * Constructs a new RecordTable with an empty list of records and columns.
     */
    public RecordTable() {
        this.records = new ArrayList<Record>();
        this.columns = new ArrayList <String>();
    }

    /**
     * Determine whether a record already exists with the given ID
     * 
     * @param Id the unique identifier to search for in the table
     * @return true if a record is found matching criteria, false otherwise
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
     * Retrieves a record with matching Id in the table
     * 
     * @param Id the Id of the record to retrieve
     * @return the Record object with specified Id
     * @throws RecordNotFoundException if the record with the specified ID is not found
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

    /**
     * Adds a record to our table.
     * 
     * @param record the Record object to be added
     */
    public void addRecord(Record record) {
        this.records.add(record);
    }

    /**
     * Retrieves all records in the table
     * 
     * @return the ArrayList containing records
     */
    public ArrayList<Record> getRecords() {
        return this.records;
    }

    /**
     * Sorts the records based on the specified field name.
     * Arranges columns so this field name is at beginning of column list.
     * 
     * @param Id name of the field based on which records are to be sorted (e.g. ID)
     */
    private void sort(String Id) {
        // sort my columns
        if (this.columns.contains(Id)) {
            this.columns.remove(Id);
        }
        this.columns.add(0, Id);
        // sort my records
        Collections.sort(this.records, 
            Comparator.comparing(r -> r.getField(Id)));
    }

    /**
     * Sorts the records by ID, then prints each record.
     */
    public void print() {
        sort("ID");
        for (Record record : this.records) {
            record.print();
        }
    }

    /**
     * Sorts the records by ID, then exports them to specified file.
     * 
     * @param filename name of the file to write the records to.
     */
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

/**
 * Exception thrown when a record is not found.
 */
class RecordNotFoundException extends RuntimeException {
    /**
     * Constructs a new RecordNotFoundException with the specified message.
     * 
     * @param message the exception text to display
     */
    public RecordNotFoundException(String message) {
        super(message);
    }
}