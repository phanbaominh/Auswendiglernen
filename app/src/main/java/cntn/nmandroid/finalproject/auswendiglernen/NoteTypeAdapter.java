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

public class NoteTypeAdapter extends ArrayAdapter<Data> {
    private Context context;

    public NoteTypeAdapter(Context context, ArrayList<Data> dataArrayList) {
        super(context, 0, dataArrayList);
        this.context = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        convertView = createViewWithLayout(R.layout.listview_item_note_types);

        Data data = getItem(position);
        linkData(data, convertView, position);

        return convertView;
    }

    private void linkData(Data data, final View view,final int pos) {

        TextView textViewName = view.findViewById(R.id.textview_listview_name_note_types);
        TextView textViewCount = view.findViewById(R.id.textview_listview_count_note_types);
        textViewName.setText(data.getText());

    }
    private View createViewWithLayout(int layoutID) {
        return LayoutInflater.from(this.context).inflate(layoutID, null);

    }
}
