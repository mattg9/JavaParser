package model;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * This class represents a directory table record with any number of field-value pairs.
 */
public class Record {
    private Map<String, String> fields;

    /**
     * Constructs a new Record with an empty map of fields.
     */
    public Record() {
        this.fields = new HashMap<>();
    }

    /**
     * Sets the value of the specified field in the record.
     * 
     * @param fieldName the name of the field to set
     * @param value the value to set for the field
     */
    public void setField(String fieldName, String value) {
        this.fields.put(fieldName, value);
    }

    /**
     * Retrieves the value of the specified field from the record.
     * 
     * @param fieldName the name of the field to retrieve
     * @return the value of the specified field, or null if the field does not exist
     */
    public String getField(String fieldName) {
        return this.fields.get(fieldName);
    }

    /**
     * Sets multiple fields in the record using the provided map of field-value pairs.
     * 
     * @param fields a map containing field names as keys and their corresponding values
     */
    public void setFields(Map<String, String> fields) {
        this.fields.putAll(fields);
    }

    /**
     * Retrieves all field-value pairs in the record.
     * 
     * @return a map containing field names as keys and their corresponding values
     */
    public Map<String, String> getFields() {
        return this.fields;
    }

    /**
     * Retrieves all field names in the record.
     * 
     * @return a collection of strings representing all field names
     */
    public Collection<String> getKeys() {
        return this.fields.keySet();
    }

    /**
     * Retrieves all field values in the record.
     * 
     * @return a collection of strings representing all field values
     */
    public Collection<String> getValues() {
        return this.fields.values();
    }

    /**
     * Prints each record to the console with minor formatting around field-value pairs.
     */
    public void print() {
        System.out.println("Record: ");
        for (Map.Entry<String, String> entry : this.fields.entrySet()) {
            System.out.print("|" + entry.getKey() + ":" + entry.getValue());
        }
        System.out.println();
    }
}
