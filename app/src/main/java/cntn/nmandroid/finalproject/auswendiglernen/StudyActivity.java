package cntn.nmandroid.finalproject.auswendiglernen;

import android.content.Intent;
import android.os.Bundle;
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
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;

public class StudyActivity extends AppCompatActivity {
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle toggle;
    ViewSwitcher viewSwitcher;
    Intent intentMain;
    final String mimeType = "text/html";
    final String encoding = "UTF-8";
    String questionHtml;
    String anwserHtml;
    private NavigationView nagivationView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_study);
        intentMain = getIntent();

        Toolbar toolbar = findViewById(R.id.toolbar_study);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        createNavigationDrawer();

        questionHtml = "\"<br /><br />Read the handouts please for tomorrow.<br /><br /><!--homework help homework\" +\n" +
                "                \"help help with homework homework assignments elementary school high school middle school\" +\n" +
                "                \"// --><font color='#60c000' size='4'><strong>Please!</strong></font>\"";
        changeWebViewContent(R.id.webview_question_study,questionHtml);
        viewSwitcher=findViewById(R.id.viewswitcher_study);


    }
    private void createNavigationDrawer(){
        drawerLayout = (DrawerLayout)findViewById(R.id.activity_study);
        toggle = new ActionBarDrawerToggle(this, drawerLayout,R.string.Open, R.string.Close);

        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        nagivationView = (NavigationView)findViewById(R.id.navview_study);
        nagivationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                Intent intent;
                switch(id)
                {
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
                        Toast.makeText(StudyActivity.this, "Settings",Toast.LENGTH_SHORT).show();break;
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
        textView.setText(intentMain.getStringExtra("deckName"));
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(toggle.onOptionsItemSelected(item))
            return true;
        switch (item.getItemId()) {
            case R.id.actionbar_item_do_study:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void onClickShowAnswer(View view) {
        viewSwitcher.showNext();
        anwserHtml = "\"<br /><br />Read the handouts please for tomorrow.<br /><br /><!--homework help homework\" +\n" +
                "                \"help help with homework homework assignments elementary school high school middle school\" +\n" +
                "                \"// --><font color='#60c000' size='4'><strong>Please!</strong></font>\"";
        changeWebViewContent(R.id.webview_answer_study,anwserHtml);
        changeWebViewContent(R.id.webview_question_anwser_study,questionHtml);
    }
    private void hideAnwser(){
        viewSwitcher.showNext();
        changeWebViewContent(R.id.webview_question_study,questionHtml);
    }
    public void onClickAgain(View view) {
        hideAnwser();
    }

    public void onClickGood(View view) {
        hideAnwser();
    }

    public void onClickEasy(View view) {
        hideAnwser();
    }
    private void changeWebViewContent(int id, String html){
        WebView webView = findViewById(id);
        webView.loadDataWithBaseURL("",html,mimeType,encoding,"");
    }
}
