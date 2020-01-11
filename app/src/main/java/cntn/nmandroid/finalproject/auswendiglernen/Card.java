package cntn.nmandroid.finalproject.auswendiglernen;

import android.util.JsonReader;

import java.io.IOException;
import java.util.ArrayList;

public class Card {
    public String attr;

    public String htmlFront;
    public String htmlBack;
    public String css;

    static Card parse(JsonReader reader) throws IOException {
        Card card = new Card();

        reader.beginObject();
        while (reader.hasNext()) {
            String name = reader.nextName();
            switch (name) {
                case "attr":
                    card.attr = reader.nextString();
                    break;
                default:
                    reader.skipValue();
                    break;
            }
        }
        reader.endObject();

        return card;
    }

    static ArrayList<Card> parseList(JsonReader reader) throws IOException {
        ArrayList<Card> ans = new ArrayList<>();

        reader.beginArray();
        while (reader.hasNext()) {
            ans.add(parse(reader));
        }
        reader.endArray();

        return ans;
    }
}
