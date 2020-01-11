package cntn.nmandroid.finalproject.auswendiglernen;

import android.util.JsonReader;

import java.io.IOException;
import java.util.ArrayList;

public class Note {
    private String noteTypeId;
    private ArrayList<String> valueList;
    private ArrayList<Card> cardList;

    public ArrayList<Card> getCardList() {
        return cardList;
    }
    public String getNoteTypeId() {
        return noteTypeId;
    }
    public ArrayList<String> getValueList() {
        return valueList;
    }
    public void setNoteTypeId(String noteTypeId) {
        this.noteTypeId = noteTypeId;
    }
    public void setValueList(ArrayList<String> valueList) {
        this.valueList = valueList;
    }

    private Note() {
    }

    public Note(NoteType noteType, ArrayList<String> valueList) {
        this.noteTypeId = noteType.getId();
        this.valueList = valueList;
        this.cardList = new ArrayList<>();
        for (int i = 0; i < noteType.getTemplateList().size(); ++i) {
            CardTemplate tpl = noteType.getTemplateList().get(i);
            this.cardList.add(tpl.render(noteType.getFieldList(), valueList));
        }
    }

    static Note parse(JsonReader reader) throws IOException {
        Note note = new Note();

        reader.beginObject();
        while (reader.hasNext()) {
            String name = reader.nextName();
            switch (name) {
                case "noteTypeId":
                    note.noteTypeId = reader.nextString();
                    break;
                case "valueList":
                    note.valueList = CommonParser.parseStringList(reader);
                    break;
                case "cardList":
                    note.cardList = Card.parseList(reader);
                    break;
                default:
                    reader.skipValue();
            }
        }
        reader.endObject();

        return note;
    }

    static ArrayList<Note> parseList(JsonReader reader) throws IOException {
        ArrayList<Note> ans = new ArrayList<>();

        reader.beginArray();
        while (reader.hasNext()) {
            ans.add(parse(reader));
        }
        reader.endArray();

        return ans;
    }
}
