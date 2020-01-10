package cntn.nmandroid.finalproject.auswendiglernen;

import android.util.JsonReader;

import java.io.IOException;
import java.util.ArrayList;

public class CommonParser {
    static ArrayList<String> parseStringList(JsonReader reader) throws IOException {
        ArrayList<String> ans = new ArrayList<>();
        reader.beginArray();
        while (reader.hasNext()) {
            ans.add(reader.nextString());
        }
        reader.endArray();
        return ans;
    }
}
