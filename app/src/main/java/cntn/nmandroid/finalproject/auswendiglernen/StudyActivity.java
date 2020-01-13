package cntn.nmandroid.finalproject.auswendiglernen;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewSwitcher;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;

public class StudyActivity extends AppCompatActivity {
    private ActionBarDrawerToggle toggle;
    private ViewSwitcher viewSwitcher;

    private Deck deck;
    private ArrayList<Card> cardArrayList;
    private int index;
    final String mimeType = "text/html";
    final String encoding = "UTF-8";
    private String questionHtml;
    private String answerHtml;
    private String css;
    private String deckName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_study);

        deckName = getIntent().getStringExtra("deckName");

        Toolbar toolbar = findViewById(R.id.toolbar_study);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        createNavigationDrawer();

        deck = MainActivity.getDeckWithName(deckName);
        cardArrayList = deck.getCardList();
        index = 0;

        if (index >= cardArrayList.size()) {
            finishDeck("No notes in deck");
        } else {
            updateQuestionHtml(index);
            changeWebViewContent(R.id.webview_question_study, questionHtml);
            changeWebViewContent(R.id.webview_question_answer_study, questionHtml);
            viewSwitcher = findViewById(R.id.viewswitcher_study);
        }
    }

    private void createNavigationDrawer() {
        DrawerLayout drawerLayout = findViewById(R.id.activity_study);
        toggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.Open, R.string.Close);

        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        NavigationView navigationView = findViewById(R.id.navview_study);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                Intent intent;
                switch (id) {
                    case R.id.navbar_item_deck_main:
                        intent = new Intent(StudyActivity.this, MainActivity.class);
                        startActivity(intent);
                        break;
                    case R.id.navbar_item_card_browser_main: {
                        intent = new Intent(StudyActivity.this, CardBrowserActivity.class);
                        startActivity(intent);
                        break;
                    }
                    case R.id.navbar_item_settings_main:
                        Toast.makeText(StudyActivity.this, "Settings", Toast.LENGTH_SHORT).show();
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
        menuInflater.inflate(R.menu.actionbar_study, menu);
        TextView textView = findViewById(R.id.textview_deck_name_actionbar);
        textView.setText(deckName);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (toggle.onOptionsItemSelected(item))
            return true;
        switch (item.getItemId()) {
            case R.id.action_bar_study_editNote:
                Intent editIntent = new Intent(this, AddNoteActivity.class);
                editIntent.putExtra("noteId", cardArrayList.get(index).noteId);
                editIntent.putExtra("deckName", deckName);
                startActivity(editIntent);
                finish();
                break;
            case R.id.action_bar_study_deleteNote:
                deck.deleteNoteById(cardArrayList.get(index).noteId);
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void onClickShowAnswer(View view) {
        viewSwitcher.showNext();
        updateAnswerHtml(index);
        changeWebViewContent(R.id.webview_answer_study, answerHtml);
    }

    private void changeQuestion() {
        if (++index < cardArrayList.size()) {
            viewSwitcher.showNext();
            updateQuestionHtml(index);
            changeWebViewContent(R.id.webview_question_study, questionHtml);
            changeWebViewContent(R.id.webview_question_answer_study, questionHtml);
        }
        else{
            finishDeck("You have finished studying");
        }
    }

    private void updateQuestionHtml(int i) {
        questionHtml = cardArrayList.get(i).htmlFront;
        css = "<style>" + cardArrayList.get(i).css + "</style>";
        questionHtml = css + questionHtml;
    }

    private void updateAnswerHtml(int i) {
        answerHtml = cardArrayList.get(i).htmlBack;
        css = "<style>" + cardArrayList.get(i).css + "</style>";
        answerHtml = css + answerHtml;
    }

    private void changeWebViewContent(int id, String html) {
        WebView webView = findViewById(id);
        webView.loadDataWithBaseURL("", html, mimeType, encoding, "");
    }
    private void finishDeck(String msg){
        // TODO: revise this function: shouldn't this activity just close, giving control back to
        //  the parent activity (MainActivity)
        AlertDialog.Builder builder = new AlertDialog.Builder(this)
                .setMessage(msg)
                .setPositiveButton(android.R.string.yes,new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // Continue with delete operation
                        dialog.dismiss();
                        StudyActivity.this.finish();
                    }
                })
                ;
        builder.create().show();

    }

    public void onClickAgain(View view) {
        changeQuestion();
    }

    public void onClickGood(View view) {
        changeQuestion();
    }

    public void onClickEasy(View view) {
        changeQuestion();
    }

}
