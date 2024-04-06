package model;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class Record {
    private Map<String, String> fields;

    public Record() {
        this.fields = new HashMap<>();
    }

    public void setField(String fieldName, String value) {
        String formatField = fieldName.trim()
            .replaceAll("^\"|\"$", "")
            .replaceAll("\u00A0", "");
        String formatValue = value.trim()
            .replaceAll("^\"|\"$", "")
            .replaceAll("\u00A0", "");
        this.fields.put(formatField, formatValue);
    }

    public String getField(String fieldName) {
        return this.fields.get(fieldName);
    }

    public void setFields(Map<String, String> fields) {
        this.fields.putAll(fields);
    }

    public Map<String, String> getFields() {
        return this.fields;
    }

    public Collection<String> getKeys() {
        return this.fields.keySet();
    }

    public Collection<String> getValues() {
        return this.fields.values();
    }

    public void print() {
        System.out.println("Record: ");
        for (Map.Entry<String, String> entry : this.fields.entrySet()) {
            System.out.print("|" + entry.getKey() + ":" + entry.getValue());
        }
        System.out.println();
    }
}
