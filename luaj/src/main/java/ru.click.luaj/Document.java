package ru.click.luaj;

import jdk.jfr.SettingDefinition;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.TwoArgFunction;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.*;


@Data
public class Document {

    private String id;
    private String rootId;
    private String fileDest;
    private Map<String, List<String>> attributes;

    public Document() {
        this.id = "doc_id";
        this.rootId = "doc_root_id";
        this.fileDest = "/var/exchange/file.txt";
        attributes = new HashMap<>();
        attributes.put("attr_1", new ArrayList<>(Arrays.asList("value_1_1")));
        attributes.put("attr_2", new ArrayList<>(Arrays.asList("value_2_1", "value_2_2", "value_2_3")));
        attributes.put("attr_3", new ArrayList<>(Arrays.asList("value_3_1", "value_3_2")));
        attributes.put("attr_4", new ArrayList<>(Arrays.asList("value_4_1")));
    }

    public void addValue(String key, String value) {
        List<String> values = attributes.getOrDefault(key, new ArrayList<>());
        values.add(value);
        this.attributes.put(key, values);
    }

    public List<String> getValues(String key) {
        return attributes.get(key);
    }

    public String getValue(String key) {
        List<String> values = attributes.get(key);
        if (values == null) return null;
        if (values.size() > 0) return values.get(0);
        return null;
    }

    public String save(String fileDest) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileDest))) {
            writer.write(this.toString());
            return "";
        } catch (Exception e) {
            return e.getMessage();
        }
    }
}
