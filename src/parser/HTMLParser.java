package parser;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.util.ArrayList;
import java.io.IOException;
import java.io.File;
import java.io.FileNotFoundException;
import model.Record;
import model.RecordTable;

/**
 * This class is for parsing directory table values from HTML files and adding them to a Record Table object.
 */
public class HTMLParser implements Parser {

    private File file;
    private ArrayList<String> headers;

    /**
     * Constructs a HTMLParser object with the specified file.
     * Initializes the file and creates an empty list for headers.
     * 
     * @param file the HTML file to parse
     */
    public HTMLParser(File file) {
        this.file = file;
        this.headers = new ArrayList<>();
    }

    /**
     * Parses the HTML file and populates the provided RecordTable with records.
     * 
     * @param table the RecordTable object to populate with parsed records
     */
    @Override
    public void parse(RecordTable table) {
        try {
            
            Document doc = Jsoup.parse(this.file, "UTF-8", "");
            if (doc == null) {
                System.err.println("HTML file is empty: " + this.file.getName());
                return;
            }

            Element directory = doc.getElementById("directory");
            if (directory == null) {
                System.err.println("HTML file contains no directory: " + this.file.getName());
                return;
            }

            // Parse HTML table headers
            parseHeaders(directory);
            table.updateColumns(this.headers);

            // Grab all the table rows below the table headers
            Elements rows = directory.select("tr:gt(0)");
            // Process each record, updating any existing IDs
            for (Element row : rows) {
                Record r = processRecord(row);
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
     * Parses header elements from an HTML directory table and adds them to our list of headers.
     * Modifies all text found in 'th' elements to uppercase.
     * 
     * @param directory the HTML table containing th elements
     */
    private void parseHeaders(Element directory) {
        Elements elements = directory.select("th");
        for (Element th : elements) {
            this.headers.add(th.text().toUpperCase());
        }
    }

    /**
     * Processes a single record represented by an HTML table row element.
     * 
     * @param row the HTML table row element representing the record
     * @return the processed Record object
     */
    private Record processRecord(Element row) {
        Record record = new Record();
        Elements tds = row.select("td");
        try {
            for (int i = 0; i < this.headers.size(); i++) {
                String value = tds.get(i).text();
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
     * Can be customized further for handling unique table values found in an HTML file.
     * 
     * @param s the string to be formatted
     * @return the formatted string
     */
    private static String format(String s) {
        return s.trim()
            .replaceAll("^\"|\"$", "");
    }

}
