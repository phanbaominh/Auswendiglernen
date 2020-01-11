package cntn.nmandroid.finalproject.auswendiglernen;

import android.content.Context;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class DataWriter {
    static void writeType(ArrayList<NoteType> typeList, Context context) throws IOException, JSONException {
        FileOutputStream out = context.openFileOutput("note-type.json", Context.MODE_PRIVATE);

        JSONArray jsonArray = new JSONArray();
        for (int i = 0; i < typeList.size(); ++i) {
            jsonArray.put(typeList.get(i).toJSON());
        }
        out.write(jsonArray.toString().getBytes());

        out.close();
    }
}
