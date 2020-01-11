package cntn.nmandroid.finalproject.auswendiglernen;

import android.content.Context;
import android.content.res.AssetManager;
import android.util.JsonReader;
import android.util.Pair;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class DataReader {
    static Pair<ArrayList<NoteType>, ArrayList<Deck>> load(
            InputStream typeInputStream,
            InputStream deckInputStream)
            throws IOException {

        JsonReader typeReader = new JsonReader(new InputStreamReader(typeInputStream));
        JsonReader deckReader = new JsonReader(new InputStreamReader(deckInputStream));

        ArrayList<NoteType> noteTypeList = NoteType.parseList(typeReader);

        // Create hash map for fast reference from ID to NoteType
        Map<String, NoteType> typeIdMap = new HashMap<>();
        for (NoteType type : noteTypeList) {
            typeIdMap.put(type.getId(), type);
        }

        ArrayList<Deck> deckList = Deck.parseList(deckReader, typeIdMap);

        return new Pair<>(noteTypeList, deckList);
    }

    static Pair<ArrayList<NoteType>, ArrayList<Deck>> initApp(Context context) throws IOException {
        AssetManager manager = context.getAssets();
        InputStream typeStream;
        try {
            // Use local file
            typeStream = context.openFileInput("note-type.json");
        } catch (FileNotFoundException e) {
            // If local file does not exist, use default file
            typeStream = manager.open("note-type.json");
        }

        InputStream deckStream;
        try {
            deckStream = context.openFileInput("deck.json");
        } catch (FileNotFoundException e) {
            deckStream = manager.open("deck.json");
        }

        return load(typeStream, deckStream);
    }
}
