package cntn.nmandroid.finalproject.auswendiglernen;

import android.util.JsonReader;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

public class Note {
    private NoteType noteType;
    private ArrayList<String> valueList;
    private ArrayList<Card> cardList;

    public ArrayList<Card> getCardList() {
        ArrayList<Card> ans = (ArrayList<Card>)this.cardList.clone();
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

    private Note() {
    }

    public Note(NoteType noteType, ArrayList<String> valueList) {
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
                case "noteTypeId":
                    String typeId = reader.nextString();
                    if (!typeIdMap.containsKey(typeId)) {
                        return null;
                    }
                    note.noteType = typeIdMap.get(typeId);
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
