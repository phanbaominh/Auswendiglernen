package cntn.nmandroid.finalproject.auswendiglernen;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class StoreFetch {
    static private FirebaseDatabase db = FirebaseDatabase.getInstance();

    static private String getCurrentTime() {
        TimeZone tz = TimeZone.getTimeZone("UTC");
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm'Z'");
        df.setTimeZone(tz);
        return df.format(new Date());
    }

    static public void InsertType(NoteType type) {
        DatabaseReference ref = db.getReference();
        ref.child("types").child(type.getId()).setValue(type);
    }

    static public void InsertDeck(Deck deck) {
        for (Note note: deck.getNoteList()) {
            InsertType(note.getNoteType());
        }
        DatabaseReference ref = db.getReference();
        ref.child("decks").child(getCurrentTime()).setValue(deck);
    }
}
