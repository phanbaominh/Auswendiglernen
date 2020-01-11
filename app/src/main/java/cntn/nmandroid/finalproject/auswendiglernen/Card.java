package cntn.nmandroid.finalproject.auswendiglernen;

import android.util.JsonReader;

import java.io.IOException;
import java.util.ArrayList;

public class Card {
    public String htmlFront;
    public String htmlBack;
    public String css;

    static Card parse(JsonReader reader) throws IOException {
        Card card = new Card();

        reader.beginObject();
        while (reader.hasNext()) {
            String name = reader.nextName();
            switch (name) {
                case "htmlFront":
                    card.htmlFront = reader.nextString();
                    break;
                case "htmlBack":
                    card.htmlBack = reader.nextString();
                    break;
                case "css":
                    card.css = reader.nextString();
                    break;
                default:
                    reader.skipValue();
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
