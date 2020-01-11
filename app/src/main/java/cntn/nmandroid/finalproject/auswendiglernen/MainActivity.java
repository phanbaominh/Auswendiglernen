package cntn.nmandroid.finalproject.auswendiglernen;

import androidx.appcompat.app.AppCompatActivity;

import android.content.res.AssetManager;
import android.os.Bundle;
import android.util.Pair;
import android.widget.TextView;

import org.json.JSONException;

import java.io.IOException;
import java.io.InputStream;
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
            InputStream typeIs = manager.open("note-type.json");
            InputStream deckIs = manager.open("deck.json");

            Pair<ArrayList<NoteType>, ArrayList<Deck>> tmp = DataReader.load(typeIs, deckIs);
            ArrayList<NoteType> typeList = tmp.first;
            ArrayList<Deck> deckList = tmp.second;

            Deck deck0 = deckList.get(1);
            Note note0 = deck0.getNoteList().get(0);
            Card card = note0.getCardList().get(0);
            TextView textView = findViewById(R.id.testTextView);
            textView.setText(card.htmlFront);

            DataWriter.writeType(typeList, this);
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
    }
}
