package cntn.nmandroid.finalproject.auswendiglernen;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.DialogFragment;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.util.Pair;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.view.MenuItem;
import com.google.android.material.navigation.NavigationView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity
            implements NameDialogFragment.NameDialogListener{
    public static DeckAdapter dataAdapter;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle toggle;
    private NavigationView nagivationView;
    public static Pair<ArrayList<NoteType>, ArrayList<Deck>> dataPair;
    public static ArrayList<Deck> deckArrayList;
    public static ArrayList<NoteType> noteTypesArrayList;
    public static ArrayList<Note> allNoteArrayList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setUpData();


        Toolbar toolbar = findViewById(R.id.toolbar_main);
        setSupportActionBar(toolbar);
        createNavigationDrawer();
        createListView();


    }

    private void setUpData() {
        try {
            dataPair = DataReader.initialiseApp(this);
        } catch (IOException e) {
            e.printStackTrace();
        }

        deckArrayList = dataPair.second;
        noteTypesArrayList = dataPair.first;
        allNoteArrayList = new ArrayList<Note>();
        createAllNoteArrayList();
        Log.d("allnotearray", "createAllNoteArrayList: " + allNoteArrayList.toString());
    }

    private void createListView(){

        dataAdapter = new DeckAdapter(this, deckArrayList);
        ListView listView = findViewById(R.id.listview_main);
        listView.setAdapter(dataAdapter);

        registerForContextMenu(listView);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Intent intent = new Intent(MainActivity.this, StudyActivity.class);
                intent.putExtra("deckName",deckArrayList.get(position).getName());
                startActivity(intent);
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

            Deck obj = (Deck) listView.getItemAtPosition(acmi.position);
            MenuItem item = menu.findItem(R.id.context_menu_item_title_main);

            SpannableString s = new SpannableString(obj.getName());
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
    public void showNameDialog() {
        // Create an instance of the dialog fragment and show it
        DialogFragment dialog = new NameDialogFragment();
        dialog.show(getSupportFragmentManager(), "NameDialogFragment");


    }

    // The dialog fragment receives a reference to this Activity through the
    // Fragment.onAttach() callback, which it uses to call the following methods
    // defined by the NoticeDialogFragment.NoticeDialogListener interface
    @Override
    public void onDialogPositiveClick(DialogFragment dialog) {
        // User touched the dialog's positive button
    }

    @Override
    public void onDialogNegativeClick(DialogFragment dialog) {
        // User touched the dialog's negative button
    }

    public void onClickFABAddDeck(View view) {
        showNameDialog();
    }

    public static String[] getDeckListName(){
        ArrayList<String> strs = new ArrayList<String>();
        strs.add("All Decks");
        for(Deck deck : MainActivity.deckArrayList) {
            strs.add(deck.getName());
        }
        return strs.toArray(new String[strs.size()]);
    }
    public static Deck getDeckWithName(String name){
        for (Deck deck: MainActivity.deckArrayList){
            if (deck.getName() == name){
                return deck;
            }
        }
        return null;
    }
    public static void createAllNoteArrayList(){
        allNoteArrayList.clear();
        for (Deck deck: deckArrayList){
            allNoteArrayList.addAll(deck.getNoteList());
        }

    }
}

