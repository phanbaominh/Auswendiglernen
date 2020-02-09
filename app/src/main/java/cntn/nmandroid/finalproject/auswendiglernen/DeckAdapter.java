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

public class DeckAdapter extends ArrayAdapter<Deck> {
    private Context context;

    public DeckAdapter(Context context, ArrayList<Deck> deckArrayList) {
        super(context, 0, deckArrayList);
        this.context = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        convertView = createViewWithLayout(R.layout.listview_item_main);

        Deck deck = getItem(position);
        linkDeck(deck, convertView, position);

        return convertView;
    }

    private void linkDeck(Deck deck, final View view,final int pos) {
        int countNew = deck.queryNewQueue().size();
        int countLearning = deck.queryLearningQueue().size();
        int countReview = deck.queryReviewQueue().size();
        TextView textViewTitle = view.findViewById(R.id.textview_listview_item_main);
        TextView textViewCount = view.findViewById(R.id.textview_listview_count_main);
        textViewTitle.setText(deck.getName());
        textViewCount.setText("New: " + countNew + "  " + "Learning: " + countLearning + "  " + "Review: " + countReview + "  ");


    }
    private View createViewWithLayout(int layoutID) {
        return LayoutInflater.from(this.context).inflate(layoutID, null);

    }
}
