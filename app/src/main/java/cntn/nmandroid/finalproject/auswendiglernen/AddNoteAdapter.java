package cntn.nmandroid.finalproject.auswendiglernen;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

public class AddNoteAdapter extends ArrayAdapter<Data> {
    private Context context;

    public AddNoteAdapter(Context context, ArrayList<Data> dataArrayList) {
        super(context, 0, dataArrayList);
        this.context = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        convertView = createViewWithLayout(R.layout.listview_item_add_note);

        Data data = getItem(position);
        linkData(data, convertView, position);

        return convertView;
    }

    private void linkData(Data data, final View view,final int pos) {

        TextView textViewField = view.findViewById(R.id.textview_listview_field_add_note);
        EditText editText = view.findViewById(R.id.edittext_listview_field_value_add_note);
        textViewField.setText(data.getText());
        ImageButton button = view.findViewById(R.id.button_listview_attach_media_add_note);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

    }
    private View createViewWithLayout(int layoutID) {
        return LayoutInflater.from(this.context).inflate(layoutID, null);

    }
}
