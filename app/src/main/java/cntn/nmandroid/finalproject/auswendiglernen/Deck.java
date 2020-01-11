package cntn.nmandroid.finalproject.auswendiglernen;

import android.util.JsonReader;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

public class Deck {
    private String name;
    private ArrayList<Note> noteList;

    public ArrayList<Note> getNoteList() {
        return noteList;
    }
    public void setNoteList(ArrayList<Note> noteList) {
        this.noteList = noteList;
    }

    static Deck parse(JsonReader reader, Map<String, NoteType> typeIdMap) throws IOException {
        Deck deck = new Deck();

        reader.beginObject();
        while (reader.hasNext()) {
            String name = reader.nextName();
            switch (name) {
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
}
