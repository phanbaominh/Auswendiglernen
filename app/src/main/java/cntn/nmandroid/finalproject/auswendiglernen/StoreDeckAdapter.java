package cntn.nmandroid.finalproject.auswendiglernen;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class StoreDeckAdapter extends ArrayAdapter<Deck> {
    private Context context;

    public StoreDeckAdapter(Context context, ArrayList<Deck> deckList) {
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

        final Deck deck = getItem(position);
        int cardCount = deck.generateCardList().size();
        String msg = cardCount + " card" + (cardCount == 1 ? "" : "s");

        downloadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (MainActivity.queryDeckById(deck.getId()) != null) {
                    return;
                }
                Map<String, NoteType> typeMap = new HashMap<>();
                for (Note note: deck.getNoteList()) {
                    if (MainActivity.queryTypeById(note.getNoteType().getId()) != null) {
                        continue;
                    }
                    typeMap.put(note.getNoteType().getId(), note.getNoteType());
                }
                for (Map.Entry<String, NoteType> entry: typeMap.entrySet()) {
                    MainActivity.noteTypesArrayList.add(entry.getValue());
                }
                MainActivity.deckArrayList.add(deck);
            }
        });

        deckName.setText(deck.getName());
        deckCount.setText(msg);

        return convertView;
    }
}
