package cntn.nmandroid.finalproject.auswendiglernen;

import androidx.appcompat.app.AppCompatActivity;

import android.content.res.AssetManager;
import android.os.Bundle;
import android.util.JsonReader;
import android.widget.TextView;

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
            TextView textView = findViewById(R.id.testTextView);
            textView.setText(arrayList.get(0).toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
