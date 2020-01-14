package cntn.nmandroid.finalproject.auswendiglernen;

import android.content.Intent;
import android.os.Bundle;

import android.util.Log;
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
    private ActionBarDrawerToggle toggle;

    // ID of the current deck, or null if user chooses "All decks"
    private String currentDeckId;

    // List of Note to display
    private ArrayList<Note> noteList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card_browser);

        Toolbar toolbar = findViewById(R.id.toolbar_card_browser);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(false);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        currentDeckId = null;
        noteList = new ArrayList<>();

        createSortOptions();
        createNavigationDrawer();
        createListView();
    }

    @Override
    protected void onStart() {
        super.onStart();

        initialiseDeckSpinner();
        initialiseNoteList();

        Log.d("START", "Start");
    }

    private void intialiseAllDecks() {
        noteList.clear();
        for (Deck deck : MainActivity.deckArrayList) {
            noteList.addAll(deck.getNoteList());
        }
    }

    private boolean deckSpinnerInitialised;

    private void initialiseDeckSpinner() {
        // Get all deck names with "All decks"
        ArrayList<String> deckList = new ArrayList<>();
        deckList.add("All decks");
        for (Deck deck : MainActivity.deckArrayList) {
            deckList.add(deck.getName());
        }

        final Spinner spinner = findViewById(R.id.spinner_actionbar_card_browser);
        createSpinnerActionBar(spinner, deckList);

        deckSpinnerInitialised = false;

        int currentPosition = 0;
        if (currentDeckId != null) {
            for (int i = 0; i < MainActivity.deckArrayList.size(); ++i) {
                if (MainActivity.deckArrayList.get(i).getId().equals(currentDeckId)) {
                    currentPosition = i + 1;
                    break;
                }
            }
        }

        spinner.setSelection(currentPosition);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (deckSpinnerInitialised) {
                    if (position != 0) {
                        String deckName = spinner.getSelectedItem().toString();
                        noteList.clear();
                        noteList.addAll(MainActivity.getDeckWithName(deckName).getNoteList());
                        currentDeckId = MainActivity.getDeckWithName(deckName).getId();
                    } else {
                        intialiseAllDecks();
                        currentDeckId = null;
                    }
                    dataAdapter.notifyDataSetChanged();
                } else {
                    deckSpinnerInitialised = true;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void initialiseNoteList() {
        int position = -1;
        if (currentDeckId != null) {
            for (int i = 0; i < MainActivity.deckArrayList.size(); ++i) {
                if (MainActivity.deckArrayList.get(i).getId().equals(currentDeckId)) {
                    position = i;
                    break;
                }
            }
        }

        if (position != -1) {
            String deckName = MainActivity.deckArrayList.get(position).getName();
            noteList.clear();
            noteList.addAll(MainActivity.getDeckWithName(deckName).getNoteList());
        } else {
            intialiseAllDecks();
        }
        dataAdapter.notifyDataSetChanged();
    }

    private void createListView() {
        dataAdapter = new CardBrowserAdapter(CardBrowserActivity.this, noteList);
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
                    dataAdapter.setNewSelection(position, true);
                } else {
                    count--;
                    dataAdapter.removeSelection(position);
                }
                mode.setTitle(count + " selected");
                mode.invalidate();
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
                boolean enableEdit = dataAdapter.getCurrentCheckedPosition().size() == 1;
                menu.findItem(R.id.actionbar_listview_item_edit).setVisible(enableEdit);
                return true;
            }

            @Override
            public boolean onActionItemClicked(ActionMode actionMode, MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.actionbar_listview_item_edit:
                        for (int position : dataAdapter.getCurrentCheckedPosition()) {
                            String noteId = noteList.get(position).getId();

                            // Find note's parent deck
                            for (Deck deck : MainActivity.deckArrayList) {
                                if (deck.getNoteById(noteId) != null) {
                                    Intent editIntent = new Intent(CardBrowserActivity.this, AddNoteActivity.class);
                                    editIntent.putExtra("noteId", noteId);
                                    editIntent.putExtra("deckName", deck.getName());
                                    startActivity(editIntent);
                                    break;
                                }
                            }
                        }
                        break;
                    case R.id.actionbar_listview_item_delete:
                        ArrayList<Note> noteToDeleteList = new ArrayList<>();
                        for (int position : dataAdapter.getCurrentCheckedPosition()) {
                            Note note = noteList.get(position);
                            noteToDeleteList.add(note);
                            String noteId = note.getId();
                            for (Deck deck: MainActivity.deckArrayList) {
                                deck.deleteNoteById(noteId);
                            }
                        }
                        noteList.removeAll(noteToDeleteList);
                        dataAdapter.notifyDataSetChanged();
                        break;
                }
                closeActionMode();
                return true;
            }

            @Override
            public void onDestroyActionMode(ActionMode actionMode) {
                actionMode = null;
                count = 0;
                dataAdapter.clearSelection();
            }

            public void closeActionMode() {
                if (actionMode != null) {
                    actionMode.finish();
                }
                count = 0;
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
        if (toggle.onOptionsItemSelected(item))
            return true;
        switch (item.getItemId()) {
            case R.id.actionbar_item_search_card_browser:
                break;
            case R.id.actionbar_item_add_card_browser:
                Intent intent = new Intent(CardBrowserActivity.this, AddNoteActivity.class);
                startActivity(intent);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void createNavigationDrawer() {
        DrawerLayout drawerLayout = findViewById(R.id.activity_card_browser);
        toggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.Open, R.string.Close);

        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.navview_card_browser);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                switch (id) {
                    case R.id.navbar_item_deck_main:
                        Intent intent = new Intent(CardBrowserActivity.this, MainActivity.class);
                        startActivity(intent);
                        break;
                    case R.id.navbar_item_card_browser_main:
                        break;
                    case R.id.navbar_item_settings_main:
                        Toast.makeText(CardBrowserActivity.this, "Settings", Toast.LENGTH_SHORT).show();
                        break;
                    default:
                        return true;
                }
                return true;
            }
        });
    }

    private void createSortOptions() {
        Spinner spinQuestion = findViewById(R.id.spinner_question_card_browser);
        Spinner spinType = findViewById(R.id.spinner_type_card_browser);
        String[] questionItems = {"Question"};
        String[] typeItems = {"Answer"};

        createSpinner(spinQuestion, questionItems);
        createSpinner(spinType, typeItems);

        spinType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void createSpinner(Spinner spinner, String[] items) {
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item, items);
        spinner.setAdapter(adapter);
    }

    private void createSpinner(Spinner spinner, ArrayList<String> items) {
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item, items);
        spinner.setAdapter(adapter);
    }

    private void createSpinnerActionBar(Spinner spinner, ArrayList<String> items) {
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.spinner_in_action_bar, items);
        adapter.setDropDownViewResource(R.layout.spinner_dropdown_actionbar);
        spinner.setAdapter(adapter);
    }
}
