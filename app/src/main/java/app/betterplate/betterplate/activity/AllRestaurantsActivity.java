package app.betterplate.betterplate.activity;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.SearchView;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import app.betterplate.betterplate.R;
import app.betterplate.betterplate.adapter.RestaurantListAdapter;
import app.betterplate.betterplate.data.core.Restaurant;
import app.betterplate.betterplate.service.DatabaseService;
import app.betterplate.betterplate.service.SortService;

public class AllRestaurantsActivity extends AppCompatActivity {

    DatabaseService databaseService;
    RecyclerView restaurantListRecyclerView;
    private static final String LOGTAG = "AllRestaurantsActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_restaurants);
        databaseService = new DatabaseService(getApplicationContext());

        setSupportActionBar((Toolbar) findViewById(R.id.toolBar));
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setTitle("All Restaurants");

        // Get Available restaurants from database
        List<Restaurant> allRestaurants = new ArrayList<>();
        try {
            allRestaurants = SortService.sortRestaurantByAlphabeticalOrder(databaseService.getAllRestaurants());
        } catch (ExecutionException | InterruptedException e) {
            Log.e(LOGTAG, "Error retrieving all restaurants from database", e);
        }

        // Initialize list of restaurants
        restaurantListRecyclerView = findViewById(R.id.mainActivityRestaurantsList);
        restaurantListRecyclerView.setFocusable(false);
        restaurantListRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        final RestaurantListAdapter allRestaurantsAdapter = new RestaurantListAdapter(allRestaurants, this);
        // Give this content to the restaurant list adapter
        restaurantListRecyclerView.setAdapter(allRestaurantsAdapter);

        // Initialize search bar
        final EditText searchBar = findViewById(R.id.searchByRestaurant);
        final ImageButton clearTextButton = findViewById(R.id.clearSearchButton);
        searchBar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String input = searchBar.getText().toString();
                if (input.isEmpty()) {
                    clearTextButton.setVisibility(View.GONE);
                } else {
                    clearTextButton.setVisibility(View.VISIBLE);
                }
                allRestaurantsAdapter.filter(input);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        clearTextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchBar.setText("");
            }
        });


        findViewById(R.id.currentMealButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), CurrentMealActivity.class);
                startActivity(intent);
            }
        });

    }

    // This allows for back navigation
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

}
