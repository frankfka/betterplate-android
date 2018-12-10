package app.betterplate.betterplate.activity;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import java.util.List;

import app.betterplate.betterplate.R;
import app.betterplate.betterplate.adapter.MenuListAdapter;
import app.betterplate.betterplate.data.core.Food;

public class FoodSearchResultsActivity extends AppCompatActivity {

    public static String SEARCH_FOODS_KEY = "SEARCH_FOODS_KEY";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_search_results);

        // Set up toolbar
        Toolbar toolbar = findViewById(R.id.toolBar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);

        List<Food> foods = (List<Food>) getIntent().getExtras().getSerializable(SEARCH_FOODS_KEY);
        RecyclerView foodRecycler = findViewById(R.id.foodSearchResultsRecycler);


        if(foods == null || foods.isEmpty()) {
            foodRecycler.setVisibility(View.GONE);
            findViewById(R.id.nothingFoundTextView).setVisibility(View.VISIBLE);
        } else {
            foodRecycler.setHasFixedSize(true);
            foodRecycler.setLayoutManager(new LinearLayoutManager(this));
            // Send the Menu ID to the adapter
            MenuListAdapter foodsAdapter = new MenuListAdapter(foods);

            foodRecycler.setAdapter(foodsAdapter);
        }
    }

    // Allow for back navigation using the toolbar
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

}
