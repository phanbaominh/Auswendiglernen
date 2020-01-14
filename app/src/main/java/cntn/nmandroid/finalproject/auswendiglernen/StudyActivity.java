package cntn.nmandroid.finalproject.auswendiglernen;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.Button;
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

import org.json.JSONException;

import java.util.ArrayList;
import java.util.PriorityQueue;
import java.util.Queue;

public class StudyActivity extends AppCompatActivity {
    private ActionBarDrawerToggle toggle;
    private ViewSwitcher viewSwitcher;

    private Deck deck;

    private int index;
    final String mimeType = "text/html";
    final String encoding = "UTF-8";
    private String questionHtml;
    private String answerHtml;
    private String css;
    private String deckName;
    private Queue<Card> newQueue;
    private Queue<Card> reviewQueue;
    private Queue<Card> currentQueue;
    private PriorityQueue<Card> learningQueue;
    private Card currentCard = null;
    private TextView textViewNew;
    private TextView textViewLearning;
    private TextView textViewReview;
    private int currentQueueType = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_study);

        textViewNew = findViewById(R.id.textview_new_card_study);
        textViewLearning = findViewById(R.id.textview_learning_card_study);
        textViewReview = findViewById(R.id.textview_review_card_study);

        deckName = getIntent().getStringExtra("deckName");

        Toolbar toolbar = findViewById(R.id.toolbar_study);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        createNavigationDrawer();

        ArrayList<Card> cardArrayList;
        deck = MainActivity.getDeckWithName(deckName);
        cardArrayList = deck.getCardList();
        index = 0;

        if (index >= cardArrayList.size()) {
            finishDeck("No notes in deck");
        } else {
            newQueue = deck.getNewQueue();
            learningQueue = deck.getLearningQueue();
            reviewQueue = deck.getReviewQueue();
            if (reviewQueue.size() > 0) {
                try {
                    Log.d("review", "onCreate: " + reviewQueue.peek().toJSON().toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            currentQueue = getCurrentQueue();
            if (currentQueue == null) {
                finishDeck("No new card to learn or review");
                return;
            }
            updateTextView();

            updateQuestionHtml(currentQueue);
            changeWebViewContent(R.id.webview_question_study, questionHtml);
            changeWebViewContent(R.id.webview_question_answer_study, questionHtml);
            viewSwitcher = findViewById(R.id.viewswitcher_study);
        }
    }

    private Queue<Card> getCurrentQueue() {
        if (learningQueue.size() > 0 && learningQueue.peek().hasPassedDueDate()) {
            currentQueueType = 1;
            return learningQueue;

        }
        if (reviewQueue.size() > 0) {
            currentQueueType = 2;
            return reviewQueue;
        }
        if (newQueue.size() > 0) {
            currentQueueType = 0;
            return newQueue;
        }
        if (learningQueue.size() > 0) {
            currentQueueType = 1;
            return learningQueue;
        }
        return null;
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
                editIntent.putExtra("noteId", currentCard.noteId);
                editIntent.putExtra("deckName", deckName);
                startActivity(editIntent);
                finish();
                break;
            case R.id.action_bar_study_deleteNote:
                deck.deleteNoteById(currentCard.noteId);
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void onClickShowAnswer(View view) {
        viewSwitcher.showNext();
        updateAnswerHtml(currentQueue);
        changeWebViewContent(R.id.webview_answer_study, answerHtml);
    }

    private void changeQuestion(int age) {
        currentCard.updateState(age);
        switch (currentCard.type) {
            case 0:
                newQueue.add(currentCard);
                break;
            case 1:
                learningQueue.add(currentCard);
                break;
        }
        currentQueue = getCurrentQueue();
        if (currentQueue != null) {

            viewSwitcher.showNext();
            updateQuestionHtml(currentQueue);
            changeWebViewContent(R.id.webview_question_study, questionHtml);
            changeWebViewContent(R.id.webview_question_answer_study, questionHtml);
            updateTextView();
        } else {
            finishDeck("You have finished studying");
        }
    }

    private void updateQuestionHtml(Queue<Card> cardArrayList) {
        currentCard = cardArrayList.peek();
        questionHtml = "<div class=\"card\">" + currentCard.htmlFront + "</div>";
        css = "<style>" + currentCard.css + "</style>";
        questionHtml = css + questionHtml;

        updateTime();
    }

    private void updateAnswerHtml(Queue<Card> cardArrayList) {
        Card card = cardArrayList.poll();
        answerHtml = "<div class=\"card\">" + card.htmlBack + "</div>";
        css = "<style>" + card.css + "</style>";
        answerHtml = css + answerHtml;

    }

    private void changeWebViewContent(int id, String html) {
        WebView webView = findViewById(id);
        webView.loadDataWithBaseURL("", html, mimeType, encoding, "");
    }

    private void finishDeck(String msg) {

        AlertDialog.Builder builder = new AlertDialog.Builder(this)
                .setMessage(msg)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // Continue with delete operation
                        dialog.dismiss();
                        StudyActivity.this.finish();
                    }
                });
        builder.create().show();

    }

    public void onClickAgain(View view) {
        changeQuestion(0);
    }

    public void onClickGood(View view) {
        changeQuestion(1);
    }

    public void onClickEasy(View view) {
        changeQuestion(2);
    }

    private void updateTextView() {
        textViewNew.setText("New: " + String.valueOf(newQueue.size()) + " ");
        textViewLearning.setText("Learning: " + String.valueOf(learningQueue.size()) + " ");
        textViewReview.setText("Review: " + String.valueOf(reviewQueue.size()) + " ");

        textViewNew.setTypeface(textViewNew.getTypeface(), Typeface.NORMAL);
        textViewLearning.setTypeface(textViewLearning.getTypeface(), Typeface.NORMAL);
        textViewReview.setTypeface(textViewReview.getTypeface(), Typeface.NORMAL);

        textViewNew.setTextColor(getResources().getColor(R.color.colorOnPrimary, null));
        textViewLearning.setTextColor(getResources().getColor(R.color.colorOnPrimary, null));
        textViewReview.setTextColor(getResources().getColor(R.color.colorOnPrimary, null));

        switch (currentQueueType) {
            case 0:
                changeTextColor(textViewNew);
                textViewNew.setTypeface(textViewNew.getTypeface(), Typeface.BOLD);
                break;
            case 1:
                changeTextColor(textViewLearning);
                textViewLearning.setTypeface(textViewLearning.getTypeface(), Typeface.BOLD);
                break;
            case 2:
                changeTextColor(textViewReview);
                textViewReview.setTypeface(textViewReview.getTypeface(), Typeface.BOLD);
                break;
        }
    }

    private void changeTextColor(TextView textView) {
        textView.setTextColor(Color.parseColor("#007f00"));
    }

    private void updateTime() {
        Card currentCard = currentQueue.peek();
        if (currentCard == null) {
            return;
        }

        String a, g, e;
        switch (currentCard.type) {
            case 0:
                a = "< 1min";
                g = "< 10min";
                e = "3d";
                break;
            case 1:
                a = "< 1min";
                g = currentCard.step == 1 ? "< 10min" : "1d";
                e = "3d";
                break;
            case 2:
                a = "< 10min";
                g = "3d";
                e = "5d";
                break;
            default:
                return;
        }

        Button btnA = findViewById(R.id.button_again_study);
        Button btnG = findViewById(R.id.button_good_study);
        Button btnE = findViewById(R.id.button_easy_study);

        a = a + "\nAgain";
        g = g + "\nGood";
        e = e + "\nEasy";

        btnA.setText(a);
        btnG.setText(g);
        btnE.setText(e);
    }
}
