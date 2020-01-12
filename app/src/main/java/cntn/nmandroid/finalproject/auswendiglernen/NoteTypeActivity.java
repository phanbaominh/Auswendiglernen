package cntn.nmandroid.finalproject.auswendiglernen;

import android.app.Dialog;
import android.graphics.Color;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.DialogFragment;

public class NoteTypeActivity extends AppCompatActivity
        implements AddNoteTypesDialogFragment.AddNoteTypesDialogListener {
    private NoteTypeAdapter noteTypeAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_types);

        Toolbar toolbar = findViewById(R.id.toolbar_note_types);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);



        createListView();
    }

    private void createListView(){
        //dataArrayList = new ArrayList<>();
        noteTypeAdapter = new NoteTypeAdapter(this, MainActivity.noteTypesArrayList);
       // dataArrayList.add(new Data("Gay"));

        ListView listView = findViewById(R.id.listview_note_types);
        registerForContextMenu(listView);

        listView.setAdapter(noteTypeAdapter);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {


        MenuInflater menuInflater = this.getMenuInflater();
        menuInflater.inflate(R.menu.actionbar_note_types, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.actionbar_item_add_note_types:
                showNoticeDialog();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.context_menu_note_types, menu);

        if (v.getId() == R.id.listview_note_types) {
            ListView listView = (ListView) v;
            AdapterView.AdapterContextMenuInfo acmi = (AdapterView.AdapterContextMenuInfo) menuInfo;

            Data obj = (Data) listView.getItemAtPosition(acmi.position);
            MenuItem item = menu.findItem(R.id.context_menu_item_title_note_types);

            SpannableString s = new SpannableString(obj.getText());
            s.setSpan(new ForegroundColorSpan(Color.RED), 0, s.length(), 0);
            item.setTitle(s);
        }

    }
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        switch (item.getItemId()) {
            case R.id.context_menu_item_delete_note_types:
                return true;
            case R.id.context_menu_item_rename_note_types:
                return true;
            case R.id.context_menu_item_edit_note_types:
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }
    public void showNoticeDialog() {
        // Create an instance of the dialog fragment and show it

        DialogFragment dialog = new AddNoteTypesDialogFragment();
        dialog.show(getSupportFragmentManager(), "AddNoteTypesDialogFragment");


    }

    // The dialog fragment receives a reference to this Activity through the
    // Fragment.onAttach() callback, which it uses to call the following methods
    // defined by the NoticeDialogFragment.NoticeDialogListener interface
    @Override
    public void onDialogPositiveClick(DialogFragment dialog) {
        // User touched the dialog's positive button
        Dialog dialogView = dialog.getDialog();
        EditText et = dialogView.findViewById(R.id.rename_dialog_note_types);
        Spinner spin = dialogView.findViewById(R.id.spinner_note_types_dialog_note_types);
        String base = spin.getSelectedItem().toString();
        Log.d("debug_add_notetype", et.getText().toString());
        Log.d("debug_add_notetype", base);

        // find notetype
        NoteType baseNoteType = null;
        for(NoteType noteType : MainActivity.noteTypesArrayList) {

            // if exists, do nothing
            if (noteType.getName().equals(et.getText().toString())){
                return ;
            }

            // found
            if (noteType.getName().equals(base)){
                baseNoteType = noteType;
            }
        }
        NoteType newNoteType = new NoteType(et.getText().toString(), baseNoteType.getFieldList(), baseNoteType.getTemplateList());

        // add it
        MainActivity.noteTypesArrayList.add(newNoteType);
        noteTypeAdapter.notifyDataSetChanged();

    }

    @Override
    public void onDialogNegativeClick(DialogFragment dialog) {
        // User touched the dialog's negative button
    }
}
