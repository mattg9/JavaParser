package parser;
import model.RecordTable;

/**
 * This interface defines the operations for a generic data parser.
 * Implementing classes should provide functionality to parse data into a RecordTable.
 */
public interface Parser {
    
    /**
     * Parses data and populates the provided RecordTable with records.
     * @param table the RecordTable object to populate with records
     */
    void parse(RecordTable table);
}
