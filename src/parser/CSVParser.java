package parser;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import model.Record;
import model.RecordTable;

/**
 * This class is for parsing directory table values from CSV files and adding them to a Record Table object.
 */
public class CSVParser implements Parser {

    private File file;
    private ArrayList<String> headers;

    /**
     * Constructs a CSVParser object with the specified file.
     * Initializes the file and creates an empty list for headers.
     * 
     * @param file the CSV file to parse
     */
    public CSVParser(File file) {
        this.file = file;
        this.headers = new ArrayList<>();
    }

    /**
     * Parses the CSV file and populates the provided RecordTable with records.
     * 
     * @param table the RecordTable object to populate with parsed records
     */
    @Override
    public void parse(RecordTable table) {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(
            new FileInputStream(this.file), "UTF-8"))) {
            // Read the first line to get the table headers
            String headerLine = reader.readLine();
            if (headerLine != null) {
                parseHeaders(headerLine);
                table.updateColumns(this.headers);
            } else {
                System.err.println("CSV file is empty: " + this.file.getName());
                return;
            }

            // Process the rest of the lines as records, updating any existing IDs
            String line;
            while ((line = reader.readLine()) != null) {
                Record r = processRecord(line);
                if (table.containsID(r.getField("ID"))) {
                    Record old = table.getRecord(r.getField("ID"));
                    old.setFields(r.getFields());
                } else {
                    table.addRecord(r);
                }
            }
        } catch (FileNotFoundException e) {
            System.err.println("File not found: " + e.getMessage());
        } catch (IOException e) {
            System.err.println("Error reading file: " + e.getMessage());
        }
    }

    /**
     * Parses header values from the first comma-separated line.
     * Modifies all text found to uppercase.
     * 
     * @param headerLine the comma-separated string representing the headers
     */
    private void parseHeaders(String headerLine) {
        String [] headerArr = headerLine.split(",");
        for (String header : headerArr) {
            this.headers.add(format(header).toUpperCase());
        }
    }

    /**
     * Processes a single record represented comma-separated line.
     * 
     * @param recordLine the comma-separated string representing the record
     * @return the processed Record object
     */
    private Record processRecord(String recordLine) {
        String[] data = recordLine.split(",");
        Record record = new Record();
        try {
            for (int i = 0; i < this.headers.size(); i++) {
                String value = data[i];
                record.setField(this.headers.get(i), format(value));
            }
        } catch (IndexOutOfBoundsException e) {
            System.err.println("Error reading record: " + e.getMessage() + ".");
            System.err.println("Field for this record will be assigned a value of null");
        }
        return record;
    }

    /**
     * Formats a string by removing leading and trailing whitespace and optional wrapped double quotes.
     * Can be customized further for handling unique table values found in a CSV file.
     * 
     * @param s the string to be formatted
     * @return the formatted string
     */
    private static String format(String s) {
        return s.trim()
            .replaceAll("^\"|\"$", "");
    }

}
