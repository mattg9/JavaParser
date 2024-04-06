package parser;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.util.ArrayList;
import java.io.IOException;
import java.io.File;
import java.io.FileNotFoundException;
import java.text.Normalizer;
import model.Record;
import model.RecordTable;

public class HTMLParser implements Parser {

    private File file;
    private ArrayList<String> headers;

    public HTMLParser(File file) {
        this.file = file;
        this.headers = new ArrayList<>();
    }

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

            // Grab all the table rows after the column names
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

    private void parseHeaders(Element directory) {
        Elements elements = directory.select("th");
        for (Element th : elements) {
            this.headers.add(th.text().toUpperCase());
        }
    }

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

    // handle html encodings
    private String format(String s) {
        s = s.trim();
        s = s.replaceAll("^\"|\"$", "");
        s = s.replaceAll("\u00A0", "");
        return s;
    }

}
