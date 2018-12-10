package app.betterplate.betterplate.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import app.betterplate.betterplate.R;
import app.betterplate.betterplate.activity.AllRestaurantsActivity;
import app.betterplate.betterplate.activity.CurrentMealActivity;
import app.betterplate.betterplate.adapter.RestaurantListAdapter;
import app.betterplate.betterplate.adapter.SimpleMealListAdapter;
import app.betterplate.betterplate.data.core.Food;
import app.betterplate.betterplate.data.core.Restaurant;
import app.betterplate.betterplate.data.preferences.FavoriteRestaurant;
import app.betterplate.betterplate.service.MealService;
import app.betterplate.betterplate.service.DatabaseService;
import app.betterplate.betterplate.service.SortService;
import app.betterplate.betterplate.service.StringFormatterService;

public class MainActivity extends AppCompatActivity {

    private DatabaseService databaseService;
    private MealService mealService;
    private List<Food> foodsInMeal = new ArrayList<>();

    private static String LOGTAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        databaseService = new DatabaseService(this);
        mealService = new MealService(this);
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /**
         * Deal with toolbar
         */
        setSupportActionBar((android.support.v7.widget.Toolbar) findViewById(R.id.toolBar));
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowTitleEnabled(false);

        /**
         * Populate featured restaurants
         */
        updateFavoriteRestaurantsView();
        /**
         * Populate current meal
         */
        updateMealView();

        /**
         * Set all the On Click Listeners
         */
        findViewById(R.id.mainActivityViewAllRestaurants).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), AllRestaurantsActivity.class));
            }
        });

        findViewById(R.id.mainActivityViewMeal).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), CurrentMealActivity.class));
            }
        });
        findViewById(R.id.currentMealButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), CurrentMealActivity.class);
                startActivity(intent);
            }
        });
        findViewById(R.id.contactUsButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent feedbackEmail = new Intent(Intent.ACTION_SEND);
                feedbackEmail.setType("text/email");
                feedbackEmail.putExtra(Intent.EXTRA_EMAIL, new String[]{"frank@betterplate.app"});
                feedbackEmail.putExtra(Intent.EXTRA_SUBJECT, "Betterplate Feedback");
                startActivity(Intent.createChooser(feedbackEmail, "Send Feedback:"));
            }

        });

    }

    @Override
    public void onResume() {
        super.onResume();
        updateMealView();
        updateFavoriteRestaurantsView();
    }

    private void updateMealView() {
        try {
            foodsInMeal = mealService.getCurrentMeal();
        } catch (ExecutionException | InterruptedException e) {
            Log.e(LOGTAG, "Error getting current meal", e);
        }

        RecyclerView mealFoodsList = findViewById(R.id.mainActivityMealRecycler);
        TextView mealNutrition = findViewById(R.id.mainActivityMealOverview);
        TextView mealHelpPrompt = findViewById(R.id.mainActivityNoMealTextView);
        // If there are no foods in the meal, display help prompt
        if (foodsInMeal != null && !foodsInMeal.isEmpty()) {
            mealFoodsList.setVisibility(View.VISIBLE);
            mealNutrition.setVisibility(View.VISIBLE);
            mealHelpPrompt.setVisibility(View.GONE);
            // Populates list of current meal items
            final SimpleMealListAdapter mealListAdapter = new SimpleMealListAdapter(foodsInMeal);
            mealFoodsList.setHasFixedSize(true);
            mealFoodsList.setLayoutManager(new LinearLayoutManager(this));
            mealFoodsList.setAdapter(mealListAdapter);
            // Populates nutrition
            (mealNutrition).setText(StringFormatterService.getMealSubtitleDescription(mealService.getMealNutrition(foodsInMeal)));
        } else {
            mealFoodsList.setVisibility(View.GONE);
            mealNutrition.setVisibility(View.GONE);
            mealHelpPrompt.setVisibility(View.VISIBLE);
        }
    }

    private void updateFavoriteRestaurantsView() {
        List<Restaurant> featuredRestaurants = new ArrayList<>();
        try {
            featuredRestaurants = SortService.sortRestaurantByAlphabeticalOrder(databaseService.getAllFavoriteRestaurants());
        } catch (ExecutionException | InterruptedException e) {
            Log.e(LOGTAG, "Error getting featured restaurants", e);
        }
        // Initialize list of restaurants
        RecyclerView featuredRestaurantsRecyclerView = findViewById(R.id.mainActivityFavoriteRestaurantsRecycler);
        featuredRestaurantsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        RecyclerView.Adapter allRestaurantsAdapter = new RestaurantListAdapter(featuredRestaurants, this);
        // Give this content to the restaurant list adapter
        featuredRestaurantsRecyclerView.setAdapter(allRestaurantsAdapter);
    }

}
