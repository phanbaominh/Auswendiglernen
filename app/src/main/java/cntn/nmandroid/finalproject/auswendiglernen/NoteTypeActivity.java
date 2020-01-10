package cntn.nmandroid.finalproject.auswendiglernen;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;

public class NoteTypeActivity extends AppCompatActivity {
    private DataAdapter dataAdapter;
    public static ArrayList<Data> dataArrayList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_types);
        createListView();
    }

    private void createListView(){
        dataArrayList = new ArrayList<>();
        dataAdapter = new DataAdapter(this, dataArrayList);
        dataArrayList.add(new Data("Gay"));
        ListView listView = findViewById(R.id.listview_note_types);
        listView.setAdapter(dataAdapter);
    }

}
