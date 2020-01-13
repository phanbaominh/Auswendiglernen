package cntn.nmandroid.finalproject.auswendiglernen;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

public class FieldAdapter extends ArrayAdapter<String> {
    private Context context;

    public FieldAdapter(Context context, ArrayList<String> stringArrayList) {
        super(context, 0, stringArrayList);
        this.context = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        convertView = createViewWithLayout(R.layout.listview_item_field);

        String string = getItem(position);
        linkString(string, convertView, position);

        return convertView;
    }

    private void linkString(String string, final View view,final int pos) {

        TextView textViewField = view.findViewById(R.id.textview_field_name_field);
        textViewField.setText(string);

    }
    private View createViewWithLayout(int layoutID) {
        return LayoutInflater.from(this.context).inflate(layoutID, null);

    }
}
