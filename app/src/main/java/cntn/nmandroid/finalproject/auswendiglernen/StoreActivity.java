package cntn.nmandroid.finalproject.auswendiglernen;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;

public class StoreActivity extends AppCompatActivity {
    private ActionBarDrawerToggle toggle;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store);

        Toolbar toolbar = findViewById(R.id.toolbar_store);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(false);
        getSupportActionBar().setTitle("Online Store");
        
        createNavigationDrawer();
        initialiseDeckList();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = this.getMenuInflater();
        menuInflater.inflate(R.menu.actionbar_store, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (toggle.onOptionsItemSelected(item))
            return true;
        switch (item.getItemId()) {
            case R.id.store_action_refresh:
                firstFetch(this);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void createNavigationDrawer() {
        DrawerLayout drawerLayout = findViewById(R.id.activity_store);
        toggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.Open, R.string.Close);

        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.navview_store);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                switch (id) {
                    case R.id.navbar_item_deck_main:
                        Intent intent = new Intent(StoreActivity.this, MainActivity.class);
                        startActivity(intent);
                        break;
                    case R.id.navbar_item_card_browser_main:
                        Intent intent2 = new Intent(StoreActivity.this, CardBrowserActivity.class);
                        startActivity(intent2);
                        break;
                    case R.id.navbar_item_store_main:

                        break;
                    case R.id.navbar_item_settings_main:
                        Toast.makeText(StoreActivity.this, "Settings", Toast.LENGTH_SHORT).show();
                        break;
                    default:
                        return true;
                }
                return true;
            }
        });
    }

    private boolean isLoading;
    private ArrayList<StoreDeck> deckList;
    private StoreDeckAdapter deckAdapter;

    private void firstFetch(final StoreActivity ctx) {
        deckList.clear();
        deckAdapter.notifyDataSetChanged();
        ctx.findViewById(R.id.loading_panel_store).setVisibility(View.VISIBLE);
        isLoading = true;
        StoreFetch.Initialise();
        StoreFetch.QueryDeckList(deckList, new Callback() {
            @Override
            public void run() {
                deckAdapter.notifyDataSetChanged();
                ctx.findViewById(R.id.loading_panel_store).setVisibility(View.GONE);
                isLoading = false;
            }
        });
    }

    private void initialiseDeckList() {
        deckList = new ArrayList<>();
        deckAdapter = new StoreDeckAdapter(this, deckList);
        final StoreActivity context = this;
        firstFetch(this);

        ListView listView = findViewById(R.id.deck_list);
        listView.setAdapter(deckAdapter);
        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (isLoading) {
                    return;
                }

                Log.d("scroll", (totalItemCount  - visibleItemCount) + " " + (firstVisibleItem + StoreFetch.PAGE_SIZE));
                if (totalItemCount - visibleItemCount <= firstVisibleItem + StoreFetch.PAGE_SIZE) {
                    isLoading = true;
                    StoreFetch.QueryDeckList(deckList, new Callback() {
                        @Override
                        public void run() {
                            isLoading = false;
                            deckAdapter.notifyDataSetChanged();
                            context.findViewById(R.id.loading_panel_store).setVisibility(View.GONE);
                        }
                    });
                }
            }
        });
    }
}
