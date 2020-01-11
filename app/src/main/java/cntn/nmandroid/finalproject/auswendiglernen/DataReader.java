package cntn.nmandroid.finalproject.auswendiglernen;

import android.util.JsonReader;
import android.util.Pair;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class DataReader {
    static Pair<ArrayList<NoteType>, ArrayList<Deck>> load(InputStream typeInputStream, InputStream deckInputStream) throws IOException {
        JsonReader typeReader = new JsonReader(new InputStreamReader(typeInputStream));
        JsonReader deckReader = new JsonReader(new InputStreamReader(deckInputStream));

        ArrayList<NoteType> noteTypeList = NoteType.parseList(typeReader);

        // Create hash map for fast reference from ID to NoteType
        Map<String, NoteType> typeIdMap = new HashMap<>();
        for (NoteType type: noteTypeList) {
            typeIdMap.put(type.getId(), type);
        }

        ArrayList<Deck> deckList = Deck.parseList(deckReader, typeIdMap);

        return new Pair<>(noteTypeList, deckList);
    }
}
