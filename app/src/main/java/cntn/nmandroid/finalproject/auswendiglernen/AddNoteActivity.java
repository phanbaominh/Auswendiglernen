package cntn.nmandroid.finalproject.auswendiglernen;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;

public class AddNoteActivity extends AppCompatActivity {
    private AddNoteAdapter dataAdapter;
    private ArrayList<String> fieldList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_note);

        fieldList = new ArrayList<String>();
        for (String string : MainActivity.noteTypesArrayList.get(0).getFieldList()){
            fieldList.add(string);
        }
        changeNoteTypes(0);

        Toolbar toolbar = findViewById(R.id.toolbar_add_note);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        creatSpinner();
        createListView();

    }


    private void createListView(){
        //dataArrayList = new ArrayList<>();
        dataAdapter = new AddNoteAdapter(AddNoteActivity.this, new ArrayList<String>(fieldList));
        // dataArrayList.add(new Data("Gay"));
        ListView listView = findViewById(R.id.listview_add_note);

        listView.setAdapter(dataAdapter);
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
                break;
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private void creatSpinner() {
        Spinner spinnerDeck = findViewById(R.id.spinner_deck_add_note);
        Spinner spinnerType = findViewById(R.id.spinner_type_add_note);

        setUpSpinner(spinnerDeck,MainActivity.getDeckListName());
        setUpSpinner(spinnerType,MainActivity.getNoteTypeListName());

        spinnerType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                fieldList.clear();
                for (String string : MainActivity.noteTypesArrayList.get(position).getFieldList()){
                    fieldList.add(string);
                }
                if (dataAdapter != null){
                    dataAdapter.clear();
                    dataAdapter.addAll(fieldList);
                }
                else{

                }
                changeNoteTypes(position);
                dataAdapter.notifyDataSetChanged();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spinnerDeck.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }
    private void changeNoteTypes(int pos){
        Button button = findViewById(R.id.button_type_add_note);
        String cardType = "Card Types:";
        for (int i = 0;i<MainActivity.noteTypesArrayList.get(pos).getTemplateList().size();i++){
            cardType += " Card" + (i+1);
        }
        button.setText(cardType);
    }
    private void setUpSpinner(Spinner spinner, String[] items){
        ArrayAdapter<String> adapter=new ArrayAdapter<String>(this,R.layout.support_simple_spinner_dropdown_item,items);
        spinner.setAdapter(adapter);
    }
}
