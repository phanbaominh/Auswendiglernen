package cntn.nmandroid.finalproject.auswendiglernen;

import android.util.JsonReader;

import androidx.annotation.NonNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.UUID;

public class NoteType {
    private String id;
    private String name;
    private ArrayList<String> fieldList;
    private ArrayList<CardTemplate> templateList;

    // Getters
    public String getId() {
        return id;
    }
    public String getName() {
        return name;
    }
    public ArrayList<String> getFieldList() {
        return fieldList;
    }
    public ArrayList<CardTemplate> getTemplateList() {
        return templateList;
    }

    // Constructors
    private NoteType() {
    }
    public NoteType(String name, ArrayList<String> fieldList, ArrayList<CardTemplate> templateList) {
        this.id = String.valueOf(UUID.randomUUID());
        this.name = name;
        this.fieldList = fieldList;
        this.templateList = templateList;
    }
    public NoteType(NoteType obj) {
        this.id = String.valueOf(UUID.randomUUID());
        this.name = obj.name;
        this.fieldList = (ArrayList<String>)obj.fieldList.clone();
        this.templateList = (ArrayList<CardTemplate>)obj.templateList.clone();
    }

    /**
     * Parse a JSON object into a NoteType object
     * @param reader JSONReader that currently points to the start of an object
     * @return a NoteType object
     * @throws IOException
     */
    static NoteType parse(JsonReader reader) throws IOException {
        NoteType noteType = new NoteType();

        reader.beginObject();
        while (reader.hasNext()) {
            String name = reader.nextName();
            switch (name) {
                case "id":
                    noteType.id = reader.nextString();
                    break;
                case "name":
                    noteType.name = reader.nextString();
                    break;
                case "fieldList":
                    noteType.fieldList = CommonParser.parseStringList(reader);
                    break;
                case "templateList":
                    noteType.templateList = CardTemplate.parseList(reader);
                    break;
            }
        }
        reader.endObject();

        return noteType;
    }

    /**
     * Parse a JSON array into an array of NoteType
     * @param reader JSONReader that currently points to the start of an array
     * @return ArrayList<NoteType>
     * @throws IOException
     */
    static ArrayList<NoteType> parseList(JsonReader reader) throws IOException {
        ArrayList<NoteType> ans = new ArrayList<>();

        reader.beginArray();
        while (reader.hasNext()) {
            ans.add(parse(reader));
        }
        reader.endArray();

        return ans;
    }
}