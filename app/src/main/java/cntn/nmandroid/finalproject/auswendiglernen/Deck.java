package cntn.nmandroid.finalproject.auswendiglernen;

import android.util.JsonReader;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;
import java.util.UUID;

public class Deck {
    private String id;
    private String name;
    private ArrayList<Note> noteList;

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<Note> getNoteList() {
        return noteList;
    }

    public ArrayList<Card> getCardList() {
        ArrayList<Card> cardArrayList = new ArrayList<Card>();
        for (Note note : noteList) {
            cardArrayList.addAll(note.getCardList());
        }
        return cardArrayList;
    }

    public void setNoteList(ArrayList<Note> noteList) {
        this.noteList = noteList;
    }

    public void addNode(Note newNote) {
        noteList.add(newNote);
    }

    private Deck() {
    }

    public Deck(String name) {
        this.id = String.valueOf(UUID.randomUUID());
        this.name = name;
        noteList = new ArrayList<>();
    }

    JSONObject toJSON() throws JSONException {
        JSONObject obj = new JSONObject();
        obj.put("id", id);
        obj.put("name", name);

        JSONArray noteList = new JSONArray();
        for (int i = 0; i < this.noteList.size(); ++i) {
            noteList.put(this.noteList.get(i).toJSON());
        }
        obj.put("noteList", noteList);
        return obj;
    }

    static Deck parse(JsonReader reader, Map<String, NoteType> typeIdMap) throws IOException {
        Deck deck = new Deck();

        reader.beginObject();
        while (reader.hasNext()) {
            String name = reader.nextName();
            switch (name) {
                case "id":
                    deck.id = reader.nextString();
                    break;
                case "name":
                    deck.name = reader.nextString();
                    break;
                case "noteList":
                    deck.noteList = Note.parseList(reader, typeIdMap);
                    break;
                default:
                    reader.skipValue();
            }
        }
        reader.endObject();

        return deck;
    }

    static ArrayList<Deck> parseList(JsonReader reader, Map<String, NoteType> typeIdMap) throws IOException {
        ArrayList<Deck> deckList = new ArrayList<>();

        reader.beginArray();
        while (reader.hasNext()) {
            deckList.add(parse(reader, typeIdMap));
        }
        reader.endArray();

        return deckList;
    }

    public void deleteNoteById(String noteId) {
        int index = -1;
        for (int i = 0; i < noteList.size(); ++i) {
            if (noteList.get(i).getId().equals(noteId)) {
                index = i;
                break;
            }
        }
        if (index != -1) {
            noteList.remove(index);
        }
    }

    public Note getNoteById(String noteId) {
        for (int i = 0; i < noteList.size(); ++i) {
            if (noteList.get(i).getId().equals(noteId)) {
                return noteList.get(i);
            }
        }
        return null;
    }
}