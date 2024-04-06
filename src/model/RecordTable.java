package model;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Objects;
import java.util.stream.Collectors;

public class RecordTable {
    private ArrayList<Record> records;
    private ArrayList <String> columns;

    public RecordTable() {
        this.records = new ArrayList<Record>();
        this.columns = new ArrayList <String>();
    }

    public boolean containsID(String Id) {
        return this.records.stream()
            .filter(r -> r != null && 
                r.getField("ID") != null && 
                Objects.equals(r.getField("ID"), Id))
            .findFirst()
            .isPresent();
    }

    public Record getRecord(String Id) throws RecordNotFoundException {
        return this.records.stream()
            .filter(r -> r != null && 
                r.getField("ID") != null && 
                Objects.equals(r.getField("ID"), Id))
            .findFirst()
            .orElseThrow(() -> new RecordNotFoundException("Record with ID " + Id + " not found."));
    }

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
        // sort my headers
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
        sort("ID");
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
            if (! this.records.isEmpty()) {
                // Write headers
                writer.write(this.columns.stream().collect(Collectors.joining(",")));
                writer.newLine();
                // Write records
                for (Record record : records) {
                    writer.write(record.getValues().stream().collect(Collectors.joining(",")));
                    writer.newLine();
                }
                System.out.println("Records have been written to " + filename);
            } else { 
                System.err.println("No record found to export");
            }
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