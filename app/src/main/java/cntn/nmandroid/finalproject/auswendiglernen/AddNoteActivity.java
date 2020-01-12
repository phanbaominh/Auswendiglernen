package cntn.nmandroid.finalproject.auswendiglernen;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.util.ArrayList;

public class AddNoteActivity extends AppCompatActivity {
    private AddNoteAdapter dataAdapter;
    private ArrayList<Data> dataArrayList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_note);

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
        dataArrayList = new ArrayList<>();
        dataArrayList.add( new Data("Data 1"));
        dataAdapter = new AddNoteAdapter(this, dataArrayList);
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

        setUpSpinner(spinnerDeck,convertToString());
        setUpSpinner(spinnerType,convertToString());
    }

    private ArrayList<String> convertToString(){
        ArrayList<String> strs = new ArrayList<String>();
        for(Data data : dataArrayList) {
            strs.add(data.getText());
        }
        return strs;
    }
    private void setUpSpinner(Spinner spinner, ArrayList<String> items){
        ArrayAdapter<String> adapter=new ArrayAdapter<String>(this,R.layout.support_simple_spinner_dropdown_item,items);
        spinner.setAdapter(adapter);
    }
}
