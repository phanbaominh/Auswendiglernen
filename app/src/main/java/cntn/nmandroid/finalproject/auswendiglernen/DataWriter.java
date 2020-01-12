package cntn.nmandroid.finalproject.auswendiglernen;

import android.content.Context;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;

public class DataWriter {
    private static void write(ArrayList<NoteType> typeList, ArrayList<Deck> deckList, OutputStream out) throws JSONException, IOException {
        JSONArray typeArr = new JSONArray();
        for (int i = 0; i < typeList.size(); ++i) {
            typeArr.put(typeList.get(i).toJSON());
        }

        JSONArray deckArr = new JSONArray();
        for (int i = 0; i < deckList.size(); ++i) {
            deckArr.put(deckList.get(i).toJSON());
        }

        JSONObject data = new JSONObject();
        data.put("typeList", typeArr);
        data.put("deckList", deckArr);

        out.write(data.toString().getBytes());
    }

    static void exportTo(OutputStream outputStream, ArrayList<NoteType> typeList, ArrayList<Deck> deckList) throws IOException, JSONException {
        write(typeList, deckList, outputStream);
    }

    static void save(Context context, ArrayList<NoteType> typeList, ArrayList<Deck> deckList) throws IOException, JSONException {
        FileOutputStream outputStream = context.openFileOutput("data.json", Context.MODE_PRIVATE);
        write(typeList, deckList, outputStream);
        outputStream.close();
    }
}
