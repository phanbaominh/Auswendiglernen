package cntn.nmandroid.finalproject.auswendiglernen;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;

public class CardBrowserActivity extends AppCompatActivity {
    private DataAdapter dataAdapter;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle toggle;
    private NavigationView nagivationView;
    public static ArrayList<Data> dataArrayList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card_browser);
        createSortOptions();
        createNavigationDrawer();
        createListView();
    }

    private void createListView(){
        dataArrayList = new ArrayList<>();
        dataAdapter = new DataAdapter(this, dataArrayList);
        dataArrayList.add(new Data("Gay"));
        ListView listView = findViewById(R.id.listview_card_browser);
        listView.setAdapter(dataAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        String[] items={"Gay lord phan bao minh hahahahahhhhhhhhhhhhhhhhhh"};

        MenuInflater menuInflater = this.getMenuInflater();
        menuInflater.inflate(R.menu.actionbar_card_browser, menu);

        MenuItem mitem = menu.findItem(R.id.actionbar_item_spinner_card_browser);
        Spinner spin =(Spinner) mitem.getActionView();
        createSpinner(spin,items);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(toggle.onOptionsItemSelected(item))
            return true;
        switch (item.getItemId()) {
            case R.id.actionbar_item_add_card_browser:
                break;
            case R.id.actionbar_item_search_card_browser:
                break;
            case R.id.actionbar_item_note_types:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void createNavigationDrawer(){
        drawerLayout = (DrawerLayout)findViewById(R.id.activity_card_browser);
        toggle = new ActionBarDrawerToggle(this, drawerLayout,R.string.Open, R.string.Close);

        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(false);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        nagivationView = (NavigationView)findViewById(R.id.navview_card_browser);
        nagivationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                switch(id)
                {
                    case R.id.navbar_item_deck_main:
                        Intent intent = new Intent(CardBrowserActivity.this, MainActivity.class);
                        startActivity(intent);
                        break;
                    case R.id.navbar_item_card_browser_main:
                        break;
                    case R.id.navbar_item_settings_main:
                        Toast.makeText(CardBrowserActivity.this, "Settings",Toast.LENGTH_SHORT).show();break;
                    default:
                        return true;
                }
                return true;
            }
        });
    }
    private void createSortOptions(){
        Spinner spin1 = findViewById(R.id.spinner_question_card_browser);
        Spinner spin2 = findViewById(R.id.spinner_type_card_browser);
        String[] questionItems = {"Question"};
        String[] typeItems = {"Answer"};

        createSpinner(spin1,questionItems);
        createSpinner(spin2,typeItems);
    }
    private void createSpinner(Spinner spinner,String[] items){

        ArrayAdapter<String> adapter=new ArrayAdapter<String>(this,R.layout.support_simple_spinner_dropdown_item,items);
        //assign adapter to the Spinner
        spinner.setAdapter(adapter);
    }

}
