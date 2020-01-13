package cntn.nmandroid.finalproject.auswendiglernen;

import android.app.Dialog;
import android.content.Intent;
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

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.DialogFragment;

import java.util.ArrayList;

public class FieldActivity extends AppCompatActivity implements NameDialogFragment.NameDialogListener {
    private FieldAdapter dataAdapter;
    private ArrayList<String> fieldList;
    private int dialogMarker = 0;
    private int currentPosition = 0;
    private int noteTypePosition = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_field);
        Intent intent = getIntent();
        noteTypePosition = intent.getIntExtra("position",0);
        fieldList = new ArrayList<String>();
        for (String string : MainActivity.noteTypesArrayList.get(noteTypePosition).getFieldList()){
            fieldList.add(string);
        }

        Toolbar toolbar = findViewById(R.id.toolbar_field);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Edit Fields");
        createListView();

    }


    private void createListView(){
        //dataArrayList = new ArrayList<>();
        dataAdapter = new FieldAdapter(this, new ArrayList<String>(fieldList));
        // dataArrayList.add(new Data("Gay"));
        ListView listView = findViewById(R.id.listview_field);

        listView.setAdapter(dataAdapter);
        registerForContextMenu(listView);

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {


        MenuInflater menuInflater = this.getMenuInflater();
        menuInflater.inflate(R.menu.actionbar_add_note, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.actionbar_item_add_add_note:
                dialogMarker = 2;
                showNameDialog("Choose Name");
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
        inflater.inflate(R.menu.context_menu_field, menu);

        if (v.getId() == R.id.listview_field) {
            ListView listView = (ListView) v;
            AdapterView.AdapterContextMenuInfo acmi = (AdapterView.AdapterContextMenuInfo) menuInfo;

            String obj = fieldList.get(acmi.position);
            MenuItem item = menu.findItem(R.id.context_menu_item_title_field);

            SpannableString s = new SpannableString(obj);
            s.setSpan(new ForegroundColorSpan(Color.RED), 0, s.length(), 0);
            item.setTitle(s);
        }

    }
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        currentPosition = info.position;
        switch (item.getItemId()) {
            case R.id.context_menu_item_delete_field:
                fieldList.remove(info.position);
                updateAdapter();
                break;
            case R.id.context_menu_item_rename_field:
                dialogMarker = 0;
                showNameDialog("Choose name:");
                break;
            case R.id.context_menu_item_reposition_field:
                dialogMarker = 1;
                showNameDialog("Choose position (0-" + (fieldList.size()-1) + "):");
                break;

        }

        return super.onContextItemSelected(item);

    }
    public void showNameDialog(String name) {
        // Create an instance of the dialog fragment and show it
        DialogFragment dialog = new NameDialogFragment();
        Bundle args = new Bundle();
        args.putString("title",name);
        dialog.setArguments(args);

        dialog.show(getSupportFragmentManager(), "NameDialogFragment");


    }

    // The dialog fragment receives a reference to this Activity through the
    // Fragment.onAttach() callback, which it uses to call the following methods
    // defined by the NoticeDialogFragment.NoticeDialogListener interface
    @Override
    public void onDialogPositiveClick(DialogFragment dialog) {
        // User touched the dialog's positive button
        Dialog dialogView = dialog.getDialog();
        EditText et = dialogView.findViewById(R.id.edittext_name_dialog);
        if (dialogMarker == 0){
            fieldList.set(currentPosition,et.getText().toString());
        }
        else if (dialogMarker == 1){
            int newPos = Integer.parseInt(et.getText().toString());
            if (newPos >=0 && newPos < fieldList.size()){
                reposition(currentPosition,newPos);
            }

        }
        else{
            fieldList.add(et.getText().toString());
        }
        updateNoteTypeArray();
        updateAdapter();
    }

    @Override
    public void onDialogNegativeClick(DialogFragment dialog) {
        // User touched the dialog's negative button
    }
    private void updateAdapter(){
        dataAdapter.clear();
        dataAdapter.addAll(fieldList);
        dataAdapter.notifyDataSetChanged();
    }
    private void updateNoteTypeArray(){
        NoteType tmpNoteType = new NoteType(MainActivity.noteTypesArrayList.get(noteTypePosition));
        tmpNoteType.setFieldList(fieldList);
        MainActivity.noteTypesArrayList.set(noteTypePosition,new NoteType(tmpNoteType));
    }
    private void reposition(int iniPos, int newPos){
        String tmp = fieldList.get(iniPos);
        fieldList.set(iniPos,fieldList.get(newPos));
        fieldList.set(newPos,tmp);
    }
}