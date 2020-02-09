package cntn.nmandroid.finalproject.auswendiglernen;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class StoreDeckAdapter extends ArrayAdapter<StoreDeck> {
    private Context context;

    public StoreDeckAdapter(Context context, ArrayList<StoreDeck> deckList) {
        super(context, 0, deckList);
        this.context = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(this.context).inflate(R.layout.store_deck_list_item, null);
        }

        TextView deckName = convertView.findViewById(R.id.deck_name);
        TextView deckCount = convertView.findViewById(R.id.deck_card_count);
        Button downloadBtn = convertView.findViewById(R.id.deck_download_btn);

        final StoreDeck meta = getItem(position);
        int cardCount = meta.getCardCount();
        String msg = cardCount + " card" + (cardCount == 1 ? "" : "s");

        if (MainActivity.queryDeckById(meta.getId()) != null) {
            downloadBtn.setVisibility(View.INVISIBLE);
        } else {
            downloadBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    FirebaseDatabase.getInstance().getReference("decks").child(meta.getId()).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            Deck deck = dataSnapshot.getValue(Deck.class);
                            Map<String, NoteType> typeMap = new HashMap<>();
                            for (Note note : deck.getNoteList()) {
                                if (MainActivity.queryTypeById(note.getNoteType().getId()) != null) {
                                    continue;
                                }
                                typeMap.put(note.getNoteType().getId(), note.getNoteType());
                            }
                            for (Map.Entry<String, NoteType> entry : typeMap.entrySet()) {
                                MainActivity.noteTypesArrayList.add(entry.getValue());
                            }
                            MainActivity.deckArrayList.add(deck);
                            Toast.makeText(context, "Download finished", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }
            });
        }

        deckName.setText(meta.getName());
        deckCount.setText(msg);

        return convertView;
    }
}
