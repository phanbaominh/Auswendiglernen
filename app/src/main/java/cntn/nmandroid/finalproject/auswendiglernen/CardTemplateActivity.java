package cntn.nmandroid.finalproject.auswendiglernen;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class CardTemplateActivity extends AppCompatActivity {

    private int notetypeId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card_template);
        Toolbar toolbar = findViewById(R.id.toolbar_field);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Edit Cards");

        Intent intent = getIntent();
        notetypeId = intent.getIntExtra("notetypeId",0);
        Log.d("debug_getintextra", String.valueOf(notetypeId));

        Spinner spinnerCardTemplates = findViewById(R.id.spinner_choose_card_id);
        createSpinner(spinnerCardTemplates,generateCardIdList(cardTemplateSize(notetypeId)));

    }


    private String[] generateCardIdList(int size){
        ArrayList<String> strs = new ArrayList<String>();
        for(int i = 1; i<=size; i++) {
            strs.add("Card " + String.valueOf(i));
        }
        return strs.toArray(new String[strs.size()]);
    }
    private int cardTemplateSize(int id){
        NoteType noteType = MainActivity.noteTypesArrayList.get(id);
        ArrayList<CardTemplate> cardTemplateArrayList = noteType.getTemplateList();
        return cardTemplateArrayList.size();
    }

    private void createSpinner(Spinner spinner, String[] items){

        ArrayAdapter<String> adapter=new ArrayAdapter<String>(this,R.layout.support_simple_spinner_dropdown_item,items);
        //assign adapter to the Spinner
        spinner.setAdapter(adapter);
    }



    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

}
