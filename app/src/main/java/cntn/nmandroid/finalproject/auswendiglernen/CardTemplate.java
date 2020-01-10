package cntn.nmandroid.finalproject.auswendiglernen;

import android.util.JsonReader;

import androidx.annotation.NonNull;

import java.io.IOException;
import java.util.ArrayList;

public class CardTemplate {
    private String name;
    private String templateFront;
    private String templateBack;
    private String styling;

    static CardTemplate parse(JsonReader reader) throws IOException {
        CardTemplate tpl = new CardTemplate();

        reader.beginObject();
        while (reader.hasNext()) {
            String name = reader.nextName();
            switch (name) {
                case "name": tpl.name = reader.nextString(); break;
                case "templateFront": tpl.templateFront = reader.nextString(); break;
                case "templateBack": tpl.templateBack = reader.nextString(); break;
                case "styling": tpl.styling = reader.nextString(); break;
                default: reader.skipValue();
            }
        }
        reader.endObject();

        return tpl;
    }

    static ArrayList<CardTemplate> parseList(JsonReader reader) throws IOException {
        ArrayList<CardTemplate> ans = new ArrayList<>();

        reader.beginArray();
        while (reader.hasNext()) {
            ans.add(parse(reader));
        }
        reader.endArray();

        return ans;
    }

    @NonNull
    @Override
    public String toString() {
        return name + ": " + templateFront + " " + templateBack + " " + styling;
    }
}
