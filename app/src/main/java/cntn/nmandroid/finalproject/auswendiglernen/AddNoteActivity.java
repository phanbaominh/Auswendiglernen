package cntn.nmandroid.finalproject.auswendiglernen;

import android.os.Bundle;
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

import java.util.ArrayList;

public class AddNoteActivity extends AppCompatActivity {
    private AddNoteAdapter dataAdapter;
    private ArrayList<String> fieldList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_note);

        fieldList = new ArrayList<>();
        fieldList.addAll(MainActivity.noteTypesArrayList.get(0).getFieldList());
        renderTemplateList(0);

        Toolbar toolbar = findViewById(R.id.toolbar_add_note);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        createSpinner();
        createListView();
    }

    private void createListView(){
        dataAdapter = new AddNoteAdapter(AddNoteActivity.this, fieldList);
        ListView listView = findViewById(R.id.listview_add_note);
        listView.setAdapter(dataAdapter);
        listView.setDivider(null);
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

    private void createSpinner() {
        Spinner spinnerDeck = findViewById(R.id.spinner_deck_add_note);
        Spinner spinnerType = findViewById(R.id.spinner_type_add_note);

        setUpSpinner(spinnerDeck,MainActivity.getDeckListName());
        setUpSpinner(spinnerType,MainActivity.getNoteTypeListName());

        spinnerType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                fieldList.clear();
                fieldList.addAll(MainActivity.noteTypesArrayList.get(position).getFieldList());
                dataAdapter.notifyDataSetChanged();
                renderTemplateList(position);
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

    private void renderTemplateList(int pos) {
        ArrayList<CardTemplate> templateList = MainActivity.noteTypesArrayList
                .get(pos)
                .getTemplateList();

        StringBuilder builder = new StringBuilder();
        builder.append("Card Types:");
        for (int i = 0; i < templateList.size(); ++i) {
            if (i > 0) {
                builder.append(',');
            }
            builder.append(" Card ").append(i + 1);
        }

        Button button = findViewById(R.id.button_type_add_note);
        button.setText(builder.toString());
    }

    private void setUpSpinner(Spinner spinner, String[] items){
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                R.layout.support_simple_spinner_dropdown_item,
                items
        );
        spinner.setAdapter(adapter);
    }
}
