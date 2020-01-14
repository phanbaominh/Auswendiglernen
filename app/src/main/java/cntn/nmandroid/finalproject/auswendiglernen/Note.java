package cntn.nmandroid.finalproject.auswendiglernen;

import android.util.JsonReader;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;
import java.util.UUID;

public class Note {
    private String id;
    private NoteType noteType;
    private ArrayList<String> valueList;
    private ArrayList<Card> cardList;

    public String getId() {
        return id;
    }
    public NoteType getNoteType() {
        return noteType;
    }
    public void setNoteType(NoteType noteType) {
        this.noteType = noteType;
    }
    public ArrayList<Card> getCardList() {
        ArrayList<Card> ans = this.cardList;
        for (int i = 0; i < noteType.getTemplateList().size(); ++i) {
            CardTemplate tpl = noteType.getTemplateList().get(i);
            Card renderedCard = tpl.render(noteType.getFieldList(), valueList);
            ans.get(i).htmlFront = renderedCard.htmlFront;
            ans.get(i).htmlBack = renderedCard.htmlBack;
            ans.get(i).css = renderedCard.css;
        }
        return ans;
    }
    public ArrayList<String> getValueList() {
        return valueList;
    }
    public void setValueList(ArrayList<String> valueList) {
        this.valueList = valueList;
    }

    public void resetCardsLearningState() {
        // TODO: reset cards' learning state after the parent note is edited.
        for (Card card: this.cardList) {
            card.type = 0;
            card.step = 1;
            card.dueDate = new Date();
        }
    }

    private Note() {
    }

    public Note(NoteType noteType, ArrayList<String> valueList) {
        this.id = String.valueOf(UUID.randomUUID());
        this.noteType = noteType;
        this.valueList = valueList;
        this.cardList = new ArrayList<>();
        for (int i = 0; i < noteType.getTemplateList().size(); ++i) {
            CardTemplate tpl = noteType.getTemplateList().get(i);
            this.cardList.add(tpl.render(noteType.getFieldList(), valueList));
        }
    }

    static Note parse(JsonReader reader, Map<String, NoteType> typeIdMap) throws IOException {
        Note note = new Note();

        reader.beginObject();
        while (reader.hasNext()) {
            String name = reader.nextName();
            switch (name) {
                case "id":
                    note.id = reader.nextString();
                    break;
                case "noteTypeId":
                    String typeId = reader.nextString();
                    note.noteType = typeIdMap.get(typeId);
                    break;
                case "valueList":
                    note.valueList = CommonParser.parseStringList(reader);
                    break;
                case "cardList":
                    note.cardList = Card.parseList(reader);
                    for (int i = 0; i < note.cardList.size(); ++i) {
                        note.cardList.get(i).noteId = note.id;
                    }
                    break;
                default:
                    reader.skipValue();
            }
        }
        reader.endObject();

        return note;
    }

    static ArrayList<Note> parseList(JsonReader reader, Map<String, NoteType> typeIdMap) throws IOException {
        ArrayList<Note> ans = new ArrayList<>();

        reader.beginArray();
        while (reader.hasNext()) {
            Note note = parse(reader, typeIdMap);
            if (note != null) {
                ans.add(note);
            }
        }
        reader.endArray();

        return ans;
    }

    public JSONObject toJSON() throws JSONException {
        JSONObject obj = new JSONObject();

        obj.put("id", id);
        obj.put("noteTypeId", noteType.getId());
        obj.put("valueList", CommonParser.stringArrayToJSON(valueList));

        JSONArray cardList = new JSONArray();
        for (int i = 0; i < this.cardList.size(); ++i) {
            cardList.put(this.cardList.get(i).toJSON());
        }
        obj.put("cardList", cardList);

        return obj;
    }
}
