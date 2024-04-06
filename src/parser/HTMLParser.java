package parser;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.util.ArrayList;
import java.util.List;
import java.io.IOException;
import java.io.File;
import java.io.FileNotFoundException;

import model.Record;
import model.RecordTable;

public class HTMLParser implements Parser {

    private File file;
    private List<String> headers;

    public HTMLParser(File file) {
        this.file = file;
        this.headers = new ArrayList<>();
    }

    @Override
    public void parse(RecordTable table) {
        try {
            
            Document doc = Jsoup.parse(this.file, "UTF-8", "");

            // Parse HTML table headers
            parseHeaders(doc);

            // Grab all the table rows after the column names
            Elements rows = doc.select("tr:gt(0)");
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

    private void parseHeaders(Document doc) {
        Elements elements = doc.select("th");
        for (Element th : elements) {
            this.headers.add(th.text());        
        }
    }

    private Record processRecord(Element row) {
        Record record = new Record();
        Elements tds = row.select("td");
        for (int i = 0; i < this.headers.size(); i++) {
            record.setField(this.headers.get(i), tds.get(i).text());
        }
        return record;
    }
}
