package cntn.nmandroid.finalproject.auswendiglernen;

import android.util.JsonReader;

import androidx.annotation.NonNull;

import java.io.IOException;
import java.util.ArrayList;

public class NoteType {
    public String id;
    public String name;
    public ArrayList<String> fieldList;
    public ArrayList<CardTemplate> templateList;

    static NoteType parse(JsonReader reader) throws IOException {
        NoteType noteType = new NoteType();

        reader.beginObject();
        while (reader.hasNext()) {
            String name = reader.nextName();
            switch (name) {
                case "id": noteType.id = reader.nextString(); break;
                case "name": noteType.name = reader.nextString(); break;
                case "fieldList": noteType.fieldList = CommonParser.parseStringList(reader); break;
                case "templateList": noteType.templateList = CardTemplate.parseList(reader); break;
            }
        }
        reader.endObject();

        return noteType;
    }

    static ArrayList<NoteType> parseList(JsonReader reader) throws IOException {
        ArrayList<NoteType> ans = new ArrayList<>();

        reader.beginArray();
        while (reader.hasNext()) {
            ans.add(parse(reader));
        }
        reader.endArray();

        return ans;
    }

    @NonNull
    @Override
    public String toString() {
        String ans = id + " " + name;
        for (int i = 0; i < fieldList.size(); ++i) {
            ans = ans + " " + fieldList.get(i);
        }
        for (int i = 0; i < templateList.size(); ++i) {
            ans = ans + " " + templateList.get(i).toString();
        }
        return ans;
    }
}
