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
import java.util.HashMap;
import java.util.Set;

public class CardBrowserAdapter extends ArrayAdapter<Data> {
    private Context context;
    private HashMap<Integer, Boolean> mSelection = new HashMap<Integer, Boolean>();
    public CardBrowserAdapter(Context context, ArrayList<Data> dataArrayList) {
        super(context, 0, dataArrayList);
        this.context = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        convertView = createViewWithLayout(R.layout.listview_item_card_browser);

        Data data = getItem(position);
        linkData(data, convertView, position);

        return convertView;
    }

    private void linkData(Data data, final View view,final int pos) {

        TextView textViewQuestion = view.findViewById(R.id.textview_listview_question_card_browser);
        TextView textViewField = view.findViewById(R.id.textview_listview_field_card_browser);

        textViewQuestion.setText(data.getText());
        textViewField.setText(data.getText());

    }
    private View createViewWithLayout(int layoutID) {
        return LayoutInflater.from(this.context).inflate(layoutID, null);

    }
    public void setNewSelection(int position, boolean value) {
        mSelection.put(position, value);
    }

    public boolean isPositionChecked(int position) {
        Boolean result = mSelection.get(position);
        return result == null ? false : result;
    }

    public Set<Integer> getCurrentCheckedPosition() {
        return mSelection.keySet();
    }

    public void removeSelection(int position) {
        mSelection.remove(position);
    }

    public void clearSelection() {
        mSelection = new HashMap<Integer, Boolean>();
    }
}
