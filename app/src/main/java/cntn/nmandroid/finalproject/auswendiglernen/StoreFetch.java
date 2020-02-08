package cntn.nmandroid.finalproject.auswendiglernen;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;

import com.google.firebase.FirebaseError;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class StoreFetch {
    static private FirebaseDatabase db = FirebaseDatabase.getInstance();

    static public void InsertType(NoteType type) {
        DatabaseReference ref = db.getReference();
        ref.child("types").child(type.getId()).setValue(type);
    }

    static public void InsertDeck(DeckWithTimestamp deck, final Activity activity) {
        activity.findViewById(R.id.progress_bar_main).setVisibility(View.VISIBLE);
        for (Note note: deck.getNoteList()) {
            InsertType(note.getNoteType());
        }
        DatabaseReference ref = db.getReference();
        ref.child("decks").child(deck.getId()).setValue(deck,new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError firebaseError, DatabaseReference firebase) {
                if (firebaseError != null) {
                    System.out.println("Data could not be saved. " + firebaseError.getMessage());
                } else {
                    activity.findViewById(R.id.progress_bar_main).setVisibility(View.GONE);
                }
            }
        });
        //activity.findViewById(R.id.loading_panel_store).setVisibility(View.GONE);
    }

    static public void QueryDeckList(final ArrayList<Deck> deckList, final StoreDeckAdapter adapter, final Activity activity) {
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
                activity.findViewById(R.id.loading_panel_store).setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
