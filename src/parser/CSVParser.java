package parser;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import model.Record;
import model.RecordTable;

public class CSVParser implements Parser {

    private File file;
    private ArrayList<String> headers;

    public CSVParser(File file) {
        this.file = file;
        this.headers = new ArrayList<>();
    }

    @Override
    public void parse(RecordTable table) {
    
        try (BufferedReader br = new BufferedReader(new FileReader(this.file))) {
            // Read the first line to get headers
            String headerLine = br.readLine();
            if (headerLine != null) {
                parseHeaders(headerLine);
                table.updateColumns(this.headers);
            } else {
                System.err.println("CSV file is empty: " + this.file.getName());
                return;
            }

            // Process the rest of the lines as records
            String line;
            while ((line = br.readLine()) != null) {
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

    private void parseHeaders(String headerLine) {
        String [] headerArr = headerLine.split(",");
        for (String header : headerArr) {
            this.headers.add(format(header).toUpperCase());
        }
    }

    private Record processRecord(String recordLine) {
        String[] data = recordLine.split(",");
        Record record = new Record();
        // throw any index out of bounds errors
        for (int i = 0; i < this.headers.size(); i++) {
            String value = data[i];
            record.setField(this.headers.get(i), format(value));
        }
        return record;
    }

    // handle wrapped quotes
    private String format(String s) {
        s = s.trim();
        s = s.replaceAll("^\"|\"$", "");
        return s;
    }

}
