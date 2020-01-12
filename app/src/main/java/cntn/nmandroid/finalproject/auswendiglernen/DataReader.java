package cntn.nmandroid.finalproject.auswendiglernen;

import android.content.Context;
import android.content.res.AssetManager;
import android.util.JsonReader;
import android.util.Pair;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class DataReader {
    private static Pair<ArrayList<NoteType>, ArrayList<Deck>> load(InputStream inputStream) throws IOException {
        ArrayList<NoteType> typeList = null;
        ArrayList<Deck> deckList = null;

        // Hash map for fast reference from ID to NoteType
        Map<String, NoteType> typeIdMap = new HashMap<>();

        JsonReader reader = new JsonReader(new InputStreamReader(inputStream));
        reader.beginObject();
        while (reader.hasNext()) {
            String name = reader.nextName();
            switch (name) {
                case "typeList":
                    typeList = NoteType.parseList(reader);
                    // Initialise typeIdMap
                    for (NoteType type : typeList) {
                        typeIdMap.put(type.getId(), type);
                    }
                    break;
                case "deckList":
                    deckList = Deck.parseList(reader, typeIdMap);
                    break;
                default:
                    reader.skipValue();
                    break;
            }
        }
        reader.endObject();

        return new Pair<>(typeList, deckList);
    }

    static Pair<ArrayList<NoteType>, ArrayList<Deck>> initialiseApp(Context context) throws IOException {
        AssetManager manager = context.getAssets();
        InputStream inputStream;
        try {
            // Use local file
            inputStream = context.openFileInput("data.json");
        } catch (FileNotFoundException e) {
            // If local file does not exist, use default file
            inputStream = manager.open("default.json");
        }

        return load(inputStream);
    }

    static Pair<ArrayList<NoteType>, ArrayList<Deck>> importFrom(InputStream inputStream) throws IOException {
        return load(inputStream);
    }
}