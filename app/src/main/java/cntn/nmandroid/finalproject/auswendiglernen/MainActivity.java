package cntn.nmandroid.finalproject.auswendiglernen;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.DialogFragment;

import android.Manifest;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
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
import android.widget.EditText;
import android.widget.ListView;
import android.view.MenuItem;
import com.google.android.material.navigation.NavigationView;
import android.widget.Toast;

import org.json.JSONException;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity
            implements NameDialogFragment.NameDialogListener {
    public static DeckAdapter dataAdapter;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle toggle;
    private NavigationView nagivationView;

    public static Pair<ArrayList<NoteType>, ArrayList<Deck>> dataPair;
    public static ArrayList<Deck> deckArrayList;
    public static ArrayList<NoteType> noteTypesArrayList;
    public static ArrayList<Note> allNoteArrayList;
    private static boolean isInit = true;

    private final int IMPORT_REQUEST_CODE = 69;
    private final int EXPORT_REQUEST_CODE = 420;
    private final int PERMISSION_REQUEST_CODE = 42069;

    private int dialogMarker = 0;
    private int currentPosition = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (isInit) {
            setUpData();
            isInit = false;
        }

        Toolbar toolbar = findViewById(R.id.toolbar_main);
        setSupportActionBar(toolbar);
        createNavigationDrawer();
        createListView();

        requestPermission(Manifest.permission.READ_EXTERNAL_STORAGE);
        requestPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE);
    }
    @Override
    protected void onStart(){
        super.onStart();
        dataAdapter.notifyDataSetChanged();
    }
    @Override
    protected void onPause() {
        super.onPause();
        try {
            DataWriter.save(this, noteTypesArrayList, deckArrayList);
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
    }

    private void requestPermission(String permission) {
        if (checkSelfPermission(permission) == PackageManager.PERMISSION_DENIED) {
            requestPermissions(new String[]{permission}, PERMISSION_REQUEST_CODE);
        }
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
    }

    private void createListView() {

        dataAdapter = new DeckAdapter(this, deckArrayList);
        ListView listView = findViewById(R.id.listview_main);
        listView.setAdapter(dataAdapter);

        registerForContextMenu(listView);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Intent intent = new Intent(MainActivity.this, StudyActivity.class);
                intent.putExtra("deckName", deckArrayList.get(position).getName());
                startActivity(intent);
            }
        });
    }

    private void createNavigationDrawer() {
        drawerLayout = (DrawerLayout) findViewById(R.id.activity_main);
        toggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.Open, R.string.Close);

        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        nagivationView = (NavigationView) findViewById(R.id.navview_main);
        nagivationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                switch (id) {
                    case R.id.navbar_item_deck_main:
                        break;
                    case R.id.navbar_item_card_browser_main: {
                        Intent intent = new Intent(MainActivity.this, CardBrowserActivity.class);
                        startActivity(intent);
                        break;
                    }
                    case R.id.navbar_item_settings_main:
                        Toast.makeText(MainActivity.this, "Settings", Toast.LENGTH_SHORT).show();
                        break;
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
        if (toggle.onOptionsItemSelected(item))
            return true;
        switch (item.getItemId()) {
            case R.id.actionbar_item_import:
                Intent importIntent = new Intent(Intent.ACTION_GET_CONTENT);
                importIntent.setType("*/*");
                startActivityForResult(importIntent, IMPORT_REQUEST_CODE);
                break;
            case R.id.actionbar_item_export:
                Intent exportIntent = new Intent(Intent.ACTION_CREATE_DOCUMENT);
                exportIntent.setType("*/*");
                startActivityForResult(exportIntent, EXPORT_REQUEST_CODE);
                break;
            case R.id.actionbar_item_note_types:
                Intent intent = new Intent(MainActivity.this, NoteTypeActivity.class);
                startActivity(intent);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case IMPORT_REQUEST_CODE:
                if (resultCode == RESULT_OK) {
                    try {
                        InputStream inputStream = getContentResolver().openInputStream(data.getData());
                        Pair<ArrayList<NoteType>, ArrayList<Deck>> tmp = DataReader.importFrom(inputStream);
                        inputStream.close();

                        noteTypesArrayList.clear();
                        noteTypesArrayList.addAll(tmp.first);

                        deckArrayList.clear();
                        deckArrayList.addAll(tmp.second);
                        createAllNoteArrayList();

                        dataAdapter.notifyDataSetChanged();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                break;
            case EXPORT_REQUEST_CODE:
                if (resultCode == RESULT_OK) {
                    try {
                        OutputStream outputStream = getContentResolver().openOutputStream(data.getData());
                        DataWriter.exportTo(outputStream, noteTypesArrayList, deckArrayList);
                        outputStream.close();
                    } catch (IOException | JSONException e) {
                        e.printStackTrace();
                    }
                }
                break;
        }
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
        currentPosition = info.position;
        switch (item.getItemId()) {
            case R.id.context_menu_item_delete_main:
                deckArrayList.remove(info.position);
                createAllNoteArrayList();
                dataAdapter.notifyDataSetChanged();
                break;
            case R.id.context_menu_item_options_main:

                break;
            case R.id.context_menu_item_rename_main:
                dialogMarker = 1;
                showNameDialog("Enter deck's new name");

                break;
        }
        return super.onContextItemSelected(item);
    }

    public void showNameDialog(String name) {
        // Create an instance of the dialog fragment and show it
        DialogFragment dialog = new NameDialogFragment();
        Bundle args = new Bundle();
        args.putString("title", name);
        dialog.setArguments(args);

        dialog.show(getSupportFragmentManager(), "NameDialogFragment");


    }

    // The dialog fragment receives a reference to this Activity through the
    // Fragment.onAttach() callback, which it uses to call the following methods
    // defined by the NoticeDialogFragment.NoticeDialogListener interface
    @Override
    public void onDialogPositiveClick(DialogFragment dialog) {
        // User touched the dialog's positive button
        Dialog dialogView = dialog.getDialog();
        EditText et = dialogView.findViewById(R.id.edittext_name_dialog);
        Log.d("debug_add_deck", et.getText().toString());
        Boolean end = false;
        // find notetype
        NoteType baseNoteType = null;
        for (Deck deck : MainActivity.deckArrayList) {

            // if exists, do nothing
            if (deck.getName().equals(et.getText().toString())) {
                AlertDialog.Builder builder = new AlertDialog.Builder(this)
                        .setMessage("Already existed deck with that name")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // Continue with delete operation
                                dialog.dismiss();
                            }
                        });
                end = true;
            }
            if (end) {
                return;
            }

        }
        if (dialogMarker == 0) {
            Deck newDeck = new Deck(et.getText().toString());
            // add it
            MainActivity.deckArrayList.add(newDeck);
        } else {
            deckArrayList.get(currentPosition).setName(et.getText().toString());
        }
        dataAdapter.notifyDataSetChanged();
    }

    @Override
    public void onDialogNegativeClick(DialogFragment dialog) {
        // User touched the dialog's negative button
    }

    public void onClickFABAddDeck(View view) {
        dialogMarker = 0;
        showNameDialog("Enter deck's name");
    }

    public static String[] getDeckListName() {
        ArrayList<String> nameList = new ArrayList<>();
        for (Deck deck : MainActivity.deckArrayList) {
            nameList.add(deck.getName());
        }
        return nameList.toArray(new String[nameList.size()]);
    }

    public static Deck getDeckWithName(String name) {
        for (Deck deck : MainActivity.deckArrayList) {
            if (deck.getName().equals(name)) {
                return deck;
            }
        }
        return null;
    }

    public static String[] getNoteTypeListName() {
        ArrayList<String> strs = new ArrayList<String>();
        for (NoteType noteType : noteTypesArrayList) {
            strs.add(noteType.getName());
        }
        return strs.toArray(new String[strs.size()]);
    }

    public static void createAllNoteArrayList() {
        allNoteArrayList.clear();
        for (Deck deck : deckArrayList) {
            allNoteArrayList.addAll(deck.getNoteList());
        }

    }
}