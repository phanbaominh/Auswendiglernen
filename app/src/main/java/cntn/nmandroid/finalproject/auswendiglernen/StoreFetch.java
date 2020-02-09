package cntn.nmandroid.finalproject.auswendiglernen;

import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class StoreFetch {
    static private FirebaseDatabase db = FirebaseDatabase.getInstance();
    static private long lastTimestamp = 0;
    static final int PAGE_SIZE = 10;

    static public void Initialise() {
        lastTimestamp = 0;
    }

    static public void InsertType(NoteType type) {
        DatabaseReference ref = db.getReference();
        ref.child("types").child(type.getId()).setValue(type);
    }

    static public void InsertDeck(Deck deck, final Activity activity) {
        activity.findViewById(R.id.progress_bar_main).setVisibility(View.VISIBLE);
        for (Note note : deck.getNoteList()) {
            InsertType(note.getNoteType());
        }
        DatabaseReference ref = db.getReference();
        ref.child("deck-names").child(deck.getId()).setValue(new StoreDeck(deck));
        ref.child("decks")
                .child(deck.getId())
                .setValue(deck, new DatabaseReference.CompletionListener() {
                    @Override
                    public void onComplete(DatabaseError firebaseError, DatabaseReference firebase) {
                        if (firebaseError != null) {
                            Toast.makeText(activity, "Something went wrong. Please try again.", Toast.LENGTH_SHORT).show();
                            Log.e("FIREBASE", firebaseError.getMessage());
                        } else {
                            activity.findViewById(R.id.progress_bar_main).setVisibility(View.GONE);
                        }
                    }
                });
        //activity.findViewById(R.id.loading_panel_store).setVisibility(View.GONE);
    }

    static public void QueryDeckList(final ArrayList<StoreDeck> deckList, final Callback onFinish) {
        DatabaseReference ref = db.getReference();

        ref.child("deck-names")
                .orderByChild("createdAt")
                .startAt(lastTimestamp)
                .limitToFirst(PAGE_SIZE + 1)
                .addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int count = 0;
                for (DataSnapshot singleSnapshot: dataSnapshot.getChildren()) {
                    StoreDeck deck = singleSnapshot.getValue(StoreDeck.class);
                    count++;
                    if (count == PAGE_SIZE + 1) {
                        lastTimestamp = deck.getCreatedAt();
                        break;
                    }
                    deckList.add(deck);
                }
                if (count < PAGE_SIZE + 1) {
                    lastTimestamp = (long)1e15 - 1;
                }
                onFinish.run();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
