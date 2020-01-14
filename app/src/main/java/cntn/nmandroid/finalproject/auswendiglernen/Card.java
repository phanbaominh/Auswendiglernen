package cntn.nmandroid.finalproject.auswendiglernen;

import android.util.JsonReader;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class Card {
    public int step;
    public int type;
    public Date dueDate;

    public String noteId;
    public String htmlFront;
    public String htmlBack;
    public String css;

    private static final String dateFormat = "dd-MM-yyyy, HH:mm:ss";

    public final static int DAYS = 0;
    public final static int HOURS = 1;
    public final static int MINUTES = 2;
    public final static int SECONDS = 3;

    /**
     * Check if a card has passed its due date
     * @return True/False whether the card has passed its due date
     */
    public boolean hasPassedDueDate() {
        return new Date().compareTo(dueDate) >= 0;
    }

    /**
     * Update dueDate to NOW() + (a time interval)
     * @param amount - Amount of time to add
     * @param interval - Type of time interval: Card.DAYS | Card.HOURS | Card.MINUTES | Card.SECONDS
     */
    public void updateDueDate(int amount, int interval) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());

        switch (interval) {
            case DAYS:
                calendar.add(Calendar.DAY_OF_MONTH, amount);
                break;
            case HOURS:
                calendar.add(Calendar.HOUR_OF_DAY, amount);
                break;
            case MINUTES:
                calendar.add(Calendar.MINUTE, amount);
                break;
            case SECONDS:
                calendar.add(Calendar.SECOND, amount);
                break;
        }

        dueDate = calendar.getTime();
    }

    static Card parse(JsonReader reader) throws IOException {
        Card card = new Card();
        SimpleDateFormat formatter = new SimpleDateFormat(dateFormat, Locale.ENGLISH);

        card.step = 1;
        card.type = 0;
        card.dueDate = new Date();

        reader.beginObject();
        while (reader.hasNext()) {
            String name = reader.nextName();
            switch (name) {
                case "step":
                    card.step = reader.nextInt();
                    break;
                case "type":
                    card.type = reader.nextInt();
                    break;
                case "dueDate":
                    try {
                        card.dueDate = formatter.parse(reader.nextString());
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
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

    public JSONObject toJSON() throws JSONException {
        SimpleDateFormat formatter = new SimpleDateFormat(dateFormat, Locale.ENGLISH);
        JSONObject obj = new JSONObject();
        obj.put("step", step);
        obj.put("type", type);
        obj.put("dueDate", formatter.format(dueDate));
        return obj;
    }
}
