package cntn.nmandroid.finalproject.auswendiglernen;

import android.content.Intent;
import android.os.Bundle;

import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;

public class CardBrowserActivity extends AppCompatActivity {
    private CardBrowserAdapter dataAdapter;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle toggle;
    private NavigationView nagivationView;
    //public static ArrayList<Data> dataArrayList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card_browser);

        Toolbar toolbar = findViewById(R.id.toolbar_card_browser);
        setSupportActionBar(toolbar);

        createSortOptions();
        createNavigationDrawer();
        createListView();
    }

    private void createListView(){
        dataAdapter = new CardBrowserAdapter(this,MainActivity.dataArrayList);

        ListView listView = findViewById(R.id.listview_card_browser);
        listView.setAdapter(dataAdapter);

        listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
        listView.setMultiChoiceModeListener(new AbsListView.MultiChoiceModeListener() {

            private int count = 0;
            private ActionMode actionMode = null;

            @Override
            public void onItemCheckedStateChanged(ActionMode mode, int position,
                                                  long id, boolean checked) {
                if (checked) {
                    count++;
                    dataAdapter.setNewSelection(position, checked);
                } else {
                    count--;
                    dataAdapter.removeSelection(position);
                }
                mode.setTitle(count + " selected");
            }

            @Override
            public boolean onCreateActionMode(ActionMode actionMode, Menu menu) {
                MenuInflater inflater = actionMode.getMenuInflater();
                inflater.inflate(R.menu.actionbar_listview_card_browser, menu);
                this.actionMode = actionMode;
                return true;
            }

            @Override
            public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                return false;
            }

            @Override
            public boolean onActionItemClicked(ActionMode actionMode, MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.actionbar_listview_item_suspend:
                }
                closeActionMode();
                return true;
            }

            @Override
            public void onDestroyActionMode(ActionMode actionMode) {
                actionMode=null;
                count=0;
                dataAdapter.clearSelection();
            }

            public void closeActionMode(){
                if(actionMode!=null){
                    actionMode.finish();
                }
                count=0;
                dataAdapter.clearSelection();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {


        MenuInflater menuInflater = this.getMenuInflater();
        menuInflater.inflate(R.menu.actionbar_card_browser, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(toggle.onOptionsItemSelected(item))
            return true;
        switch (item.getItemId()) {
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
        Spinner spinQuestion = findViewById(R.id.spinner_question_card_browser);
        Spinner spinType = findViewById(R.id.spinner_type_card_browser);
        Spinner spinDeck = findViewById(R.id.spinner_actionbar_card_browser);
        String[] questionItems = {"Question"};
        String[] typeItems = {"Answer"};
        String[] deckItems={"Gay lord phan bao minh hahahahahhhhhhhhhhhhhhhhhh"};

        createSpinner(spinQuestion,questionItems);
        createSpinner(spinType,typeItems);
        createSpinner(spinDeck,deckItems);

        spinType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinDeck.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }
    private void createSpinner(Spinner spinner,String[] items){

        ArrayAdapter<String> adapter=new ArrayAdapter<String>(this,R.layout.support_simple_spinner_dropdown_item,items);
        //assign adapter to the Spinner
        spinner.setAdapter(adapter);
    }
       /* <item
    android:id="@+id/actionbar_item_spinner_card_browser"
    android:visible="true"
    app:showAsAction="collapseActionView"
    android:title="Decks"
    app:actionViewClass="android.widget.Spinner" />*/
}
