package cntn.nmandroid.finalproject.auswendiglernen;

import android.util.JsonReader;

import org.json.JSONArray;
import org.json.JSONObject;

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

    static String render(String template, ArrayList<String> fieldList, ArrayList<String> valueList) {
        String ans = String.valueOf(template);
        for (int i = 0; i < fieldList.size(); ++i) {
            String field = "{{" + fieldList.get(i) + "}}";
            String value = valueList.get(i);
            ans = ans.replace(field, value);
        }
        return ans;
    }

    static JSONArray stringArrayToJSON(ArrayList<String> arr) {
        JSONArray jsonArray = new JSONArray();
        for (int i = 0; i < arr.size(); ++i) {
            jsonArray.put(arr.get(i));
        }
        return jsonArray;
    }
}
