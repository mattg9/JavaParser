import java.io.File;
import model.RecordTable;
import parser.CSVParser;
import parser.HTMLParser;
import parser.Parser;


public class RecordMerger {

	public static final String FILENAME_COMBINED = "combined.csv";

	/**
	 * Entry point of this test.
	 *
	 * @param args command line arguments: first.html and second.csv.
	 * @throws Exception bad things had happened.
	 */
	public static void main(final String[] args) throws Exception {

		if (args.length == 0) {
			System.err.println("Usage: java RecordMerger file1 [ file2 [...] ]");
			System.exit(1);
		}

		RecordTable rt = new RecordTable();

        for (String filename : args) {
            File file = new File(filename);
            String extension = filename.substring(filename.lastIndexOf('.') + 1);
            Parser parser;
            switch (extension.toLowerCase()) {
                case "html":
                    parser = new HTMLParser(file);
                    break;
                case "csv":
                    parser = new CSVParser(file);
                    break;
                default:
                    System.out.println("Unsupported file type: " + filename);
                    continue; // Skip to the next iteration
            }
            parser.parse(rt);
		}
        
        //rt.print();
        rt.exportToCSV(FILENAME_COMBINED);
	}
}
