package cntn.nmandroid.finalproject.auswendiglernen;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.view.MenuItem;
import com.google.android.material.navigation.NavigationView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    public static DataAdapter dataAdapter;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle toggle;
    private NavigationView nagivationView;
    public static ArrayList<Data> dataArrayList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar_main);
        setSupportActionBar(toolbar);
        createNavigationDrawer();
        createListView();


    }
    private void createListView(){
        dataArrayList = new ArrayList<>();
        dataAdapter = new DataAdapter(this, dataArrayList);
        dataArrayList.add(new Data("Gay"));
        dataArrayList.add(new Data("Haha"));
        ListView listView = findViewById(R.id.listview_main);
        listView.setAdapter(dataAdapter);

        registerForContextMenu(listView);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {

            }
        });
    }
    private void createNavigationDrawer(){
        drawerLayout = (DrawerLayout)findViewById(R.id.activity_main);
        toggle = new ActionBarDrawerToggle(this, drawerLayout,R.string.Open, R.string.Close);

        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        nagivationView = (NavigationView)findViewById(R.id.navview_main);
        nagivationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                switch(id)
                {
                    case R.id.navbar_item_deck_main:
                        break;
                    case R.id.navbar_item_card_browser_main: {
                        Intent intent = new Intent(MainActivity.this, CardBrowserActivity.class);
                        startActivity(intent);
                        break;
                    }
                    case R.id.navbar_item_settings_main:
                        Toast.makeText(MainActivity.this, "Settings",Toast.LENGTH_SHORT).show();break;
                    default:
                        return true;
                }
                return true;
            }
        });
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = this.getMenuInflater();
        menuInflater.inflate(R.menu.actionbar_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(toggle.onOptionsItemSelected(item))
            return true;
        switch (item.getItemId()) {
            case R.id.actionbar_item_import:
                break;
            case R.id.actionbar_item_export:
                break;
            case R.id.actionbar_item_note_types:
                Intent intent = new Intent(MainActivity.this, NoteTypeActivity.class);
                startActivity(intent);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.context_menu_main, menu);

        if (v.getId() == R.id.listview_main) {
            ListView listView = (ListView) v;
            AdapterView.AdapterContextMenuInfo acmi = (AdapterView.AdapterContextMenuInfo) menuInfo;

            Data obj = (Data) listView.getItemAtPosition(acmi.position);
            MenuItem item = menu.findItem(R.id.context_menu_item_title_main);

            SpannableString s = new SpannableString(obj.getText());
            s.setSpan(new ForegroundColorSpan(Color.RED), 0, s.length(), 0);
            item.setTitle(s);
        }

    }
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        switch (item.getItemId()) {
            case R.id.context_menu_item_delete_main:
                return true;
            case R.id.context_menu_item_options_main:
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }

}

