package cntn.nmandroid.finalproject.auswendiglernen;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class CardTemplateActivity extends AppCompatActivity {

    private int notetypeId;
    private ArrayList<CardTemplate> cardTemplateArrayList;
    private EditText front,back,styling;
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

        spinnerCardTemplates.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                parseDataFromChosenCardTemplate(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        front = findViewById(R.id.editText_frontTemplate);
        back = findViewById(R.id.editText_backTemplate);
        styling = findViewById(R.id.editText_styling);

        //default là card 1
        parseDataFromChosenCardTemplate(0);
    }

    private void parseDataFromChosenCardTemplate(int cardId) {
        front.setText(cardTemplateArrayList.get(cardId).getTemplateFront());
        styling.setText(cardTemplateArrayList.get(cardId).getStyling());
        back.setText(cardTemplateArrayList.get(cardId).getTemplateBack());
    }


    private String[] generateCardIdList(int size){
        ArrayList<String> strs = new ArrayList<String>();
        for(int i = 1; i<=size; i++) {
            strs.add("Card " + String.valueOf(i));
        }
        return strs.toArray(new String[strs.size()]);
    }

    // Ban đầu tạo để lấy size, nhưng sau đó đổi lại có lưu lại nên sau này refactor có thể xóa và
    // và đổi lại createSpinner ở onCreate lấy thẳng size cho đỡ tối nghĩa.
    private int cardTemplateSize(int id){
        NoteType noteType = MainActivity.noteTypesArrayList.get(id);
        cardTemplateArrayList = noteType.getTemplateList();
        return cardTemplateArrayList.size();
    }

    private void createSpinner(Spinner spinner, String[] items){

        ArrayAdapter<String> adapter=new ArrayAdapter<String>(this,R.layout.support_simple_spinner_dropdown_item,items);
        //assign adapter to the Spinner
        spinner.setAdapter(adapter);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {


        MenuInflater menuInflater = this.getMenuInflater();
        menuInflater.inflate(R.menu.actionbar_edit_card_template, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.actionbar_item_preview_card_template:
                // showNoticeDialog();
                break;
            case R.id.actionbar_item_finish_card_template:
                // showNoticeDialog();
                break;
            case R.id.actionbar_item_add_card_template:
                // showNoticeDialog();
                break;
            case R.id.actionbar_item_delete_card_template:
                // showNoticeDialog();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

}
