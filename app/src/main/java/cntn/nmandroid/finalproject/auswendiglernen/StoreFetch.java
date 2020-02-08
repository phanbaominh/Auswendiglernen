package cntn.nmandroid.finalproject.auswendiglernen;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class StoreFetch {
    static private FirebaseDatabase db = FirebaseDatabase.getInstance();

    static public void InsertType(NoteType type) {
        DatabaseReference ref = db.getReference();
        ref.child("types").child(type.getId()).setValue(type);
    }

    static public void InsertDeck(DeckWithTimestamp deck) {
        for (Note note: deck.getNoteList()) {
            InsertType(note.getNoteType());
        }
        DatabaseReference ref = db.getReference();
        ref.child("decks").child(deck.getId()).setValue(deck);
    }

    static public void QueryDeckList(final ArrayList<Deck> deckList, final StoreDeckAdapter adapter) {
        DatabaseReference ref = db.getReference();

        ref.child("decks")
                .orderByChild("createAt")
                .addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot singleSnapshot: dataSnapshot.getChildren()) {
                    DeckWithTimestamp deck = singleSnapshot.getValue(DeckWithTimestamp.class);
                    deckList.add(deck);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
