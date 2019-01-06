package app.betterplate.betterplate.activity;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import java.util.List;

import app.betterplate.betterplate.R;
import app.betterplate.betterplate.adapter.MenuListAdapter;
import app.betterplate.betterplate.data.core.Food;
import app.betterplate.betterplate.service.SortService;

public class FoodSearchResultsActivity extends AppCompatActivity {

    public static String SEARCH_FOODS_KEY = "SEARCH_FOODS_KEY";
    private static String SORT_INC_CALORIES = "Calories (Low to High)";
    private static String SORT_DEC_PROTEIN =  "Protein (High to Low)";
    private static String SORT_INC_CARBS = "Carbohydrates (Low to High)";
    private static String SORT_INC_FAT = "Fat (Low to High)";
    private static String SORT_DEC_HEALTH = "Health Score (High to Low)";

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

        final List<Food> foods = (List<Food>) getIntent().getExtras().getSerializable(SEARCH_FOODS_KEY);
        final RecyclerView foodRecycler = findViewById(R.id.foodSearchResultsRecycler);


        final String[] items = new String[]{SORT_INC_CALORIES, SORT_DEC_PROTEIN, SORT_INC_CARBS, SORT_INC_FAT, SORT_DEC_HEALTH};
        Spinner sortBySpinner = findViewById(R.id.sortBySpinner);
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(this, R.layout.sort_spinner_item, items);
        sortBySpinner.setAdapter(spinnerAdapter);

        if(foods == null || foods.isEmpty()) {
            foodRecycler.setVisibility(View.GONE);
            findViewById(R.id.nothingFoundTextView).setVisibility(View.VISIBLE);
        } else {
            foodRecycler.setHasFixedSize(true);
            foodRecycler.setLayoutManager(new LinearLayoutManager(this));
            // Send the Menu ID to the adapter
            MenuListAdapter foodsAdapter = new MenuListAdapter(foods);
            foodRecycler.setAdapter(foodsAdapter);
            sortBySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                    String selection = items[position];
                    if(selection.equals(SORT_INC_CALORIES)) {
                        foodRecycler.setAdapter(new MenuListAdapter(SortService.sortFoods(foods, SortService.SORT_BY_INC_CALORIES)));
                    } else if (selection.equals(SORT_DEC_PROTEIN)) {
                        foodRecycler.setAdapter(new MenuListAdapter(SortService.sortFoods(foods, SortService.SORT_BY_DEC_PROTEIN)));
                    } else if (selection.equals(SORT_INC_CARBS)) {
                        foodRecycler.setAdapter(new MenuListAdapter(SortService.sortFoods(foods, SortService.SORT_BY_INC_CARBS)));
                    } else if (selection.equals(SORT_INC_FAT)) {
                        foodRecycler.setAdapter(new MenuListAdapter(SortService.sortFoods(foods, SortService.SORT_BY_INC_FAT)));
                    } else if (selection.equals(SORT_DEC_HEALTH)) {
                        foodRecycler.setAdapter(new MenuListAdapter(SortService.sortFoods(foods, SortService.SORT_BY_DEC_HEALTH)));
                    }

                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        }
    }

    // Allow for back navigation using the toolbar
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

}
