package cntn.nmandroid.finalproject.auswendiglernen;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

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

        Deck deck = getItem(position);
        TextView deckName = convertView.findViewById(R.id.deck_name);
        TextView deckCount = convertView.findViewById(R.id.deck_card_count);

        deckName.setText(deck.getName());

        int cardCount = deck.generateCardList().size();
        String msg = cardCount + " card" + (cardCount == 1 ? "" : "s");
        deckCount.setText(msg);

        return convertView;
    }
}
