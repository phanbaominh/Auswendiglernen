package cntn.nmandroid.finalproject.auswendiglernen;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

public class NoteTypeAdapter extends ArrayAdapter<NoteType> {
    private Context context;

    public NoteTypeAdapter(Context context, ArrayList<NoteType> noteTypeArrayList) {
        super(context, 0, noteTypeArrayList);
        this.context = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        convertView = createViewWithLayout(R.layout.listview_item_note_types);

        NoteType noteType = getItem(position);
        linkNoteType(noteType, convertView, position);

        return convertView;
    }

    private void linkNoteType(NoteType noteType, final View view,final int pos) {

        TextView textViewName = view.findViewById(R.id.textview_listview_name_note_types);
        TextView textViewCount = view.findViewById(R.id.textview_listview_count_note_types);
        textViewName.setText(noteType.getName());
        MainActivity.createAllNoteArrayList();
        int count = 0;
        for (Note nt: MainActivity.allNoteArrayList){
            if (noteType.getName().equals(nt.getNoteType().getName())){
                count++;
            }
        }
        textViewCount.setText(String.valueOf(count) + " notes");
    }
    private View createViewWithLayout(int layoutID) {
        return LayoutInflater.from(this.context).inflate(layoutID, null);

    }
}
