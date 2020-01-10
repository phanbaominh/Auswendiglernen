package cntn.nmandroid.finalproject.auswendiglernen;

import androidx.appcompat.app.AppCompatActivity;

import android.content.res.AssetManager;
import android.os.Bundle;
import android.util.JsonReader;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        loadNoteTypeData();
    }

    private void loadNoteTypeData() {
        AssetManager manager = this.getAssets();

        try {
            InputStream inputStream = manager.open("note-type.json");
            JsonReader reader = new JsonReader(new InputStreamReader(inputStream));

            ArrayList<NoteType> arrayList = NoteType.parseList(reader);
            NoteType noteType = arrayList.get(0);
            ArrayList<String> valueList = new ArrayList<>();
            valueList.add("front value");
            valueList.add("back value");
            valueList.add("page number");

//            Toast.makeText(this, String.valueOf(noteType.fieldList.size()), Toast.LENGTH_SHORT).show();

            Card card = noteType.templateList.get(0).render(noteType.fieldList, valueList);
            TextView textView = findViewById(R.id.testTextView);
            textView.setText(card.htmlFront);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
