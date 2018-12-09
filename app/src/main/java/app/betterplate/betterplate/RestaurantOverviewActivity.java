package app.betterplate.betterplate;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.ExecutionException;

import app.betterplate.betterplate.activity.CurrentMealActivity;
import app.betterplate.betterplate.activity.RestaurantMenusActivity;
import app.betterplate.betterplate.adapter.MenuListAdapter;
import app.betterplate.betterplate.dao.core.FoodDao;
import app.betterplate.betterplate.data.core.Food;
import app.betterplate.betterplate.data.core.Restaurant;
import app.betterplate.betterplate.data.preferences.FavoriteRestaurant;
import app.betterplate.betterplate.service.DatabaseService;
import app.betterplate.betterplate.service.FoodFinderService;
import io.apptik.widget.MultiSlider;

import static app.betterplate.betterplate.activity.RestaurantMenusActivity.RESTAURANT_ID_KEY;

public class RestaurantOverviewActivity extends AppCompatActivity {

    private DatabaseService databaseService;
    private Restaurant restaurant;
    private static String LOGTAG = "RestaurantOverviewActivity";
    private List<Food> allFoods;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_overview);

        databaseService = new DatabaseService(this);
        // Get Restaurant and their menus
        final int restaurantId = (int) getIntent().getExtras().get(RESTAURANT_ID_KEY);

        List<Food> featuredFoods = new ArrayList<>();
        try {
            restaurant = databaseService.getRestaurantFromId(restaurantId);
            allFoods = databaseService.getAllFoodsFromRestaurant(restaurantId);
            for (Food food : allFoods) {
                if (1 == food.getIsFeatured()) {
                    featuredFoods.add(food);
                }
            }

        } catch (ExecutionException | InterruptedException e) {
            Log.e(LOGTAG, "Error retrieving menus from database for restaurant ID ".concat(String.valueOf(restaurantId)), e);
        }

        // Initialize favorites
        List<Restaurant> favoriteRestaurants = new ArrayList<>();
        try {
            favoriteRestaurants = databaseService.getAllFavoriteRestaurants();
        } catch (ExecutionException | InterruptedException e) {
            Log.e(LOGTAG, "Could not get all favorite restaurants", e);
        }
        final boolean isFavorited = favoriteRestaurants.contains(restaurant);

        // Set title and favorites
        ((TextView) findViewById(R.id.restaurantTitleTextView)).setText(restaurant.getName());
        final ImageView isFavoriteIcon = findViewById(R.id.restaurantOverviewAddToFavorites);
        if(isFavorited) {
            isFavoriteIcon.setImageResource(R.drawable.favorite_filled);
        } else {
            isFavoriteIcon.setImageResource(R.drawable.favorite_unfilled);
        }

        setSupportActionBar((Toolbar) findViewById(R.id.toolBar));
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle(restaurant.getName());
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);

        ImageView headerImage = findViewById(R.id.logoHeaderImage);
        headerImage.setBackgroundColor(Color.parseColor(restaurant.getBrandColor()));
        try {
            Bitmap restaurantLogo = BitmapFactory.decodeStream(getAssets().open(Constants.RESTAURANT_HEADER_IMAGE_FOLDER.concat(restaurant.getImageKey())));
            headerImage.setImageBitmap(restaurantLogo);
        } catch (IOException e) {
            Log.e(LOGTAG, "Error retrieving header image for restaurant ID".concat(String.valueOf(restaurantId)), e);
        }

        /**
         * Create adapter to display featured foods
         */
        RecyclerView featuredFoodsRecycler = findViewById(R.id.restaurantOverviewFeaturedFoodsRecycler);
        featuredFoodsRecycler.setHasFixedSize(true);
        featuredFoodsRecycler.setLayoutManager(new LinearLayoutManager(this));
        featuredFoodsRecycler.setAdapter(new MenuListAdapter(featuredFoods));


        /**
         * Create on click listener to launch into menu
         */
        findViewById(R.id.restaurantOverviewViewMenuTextView).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), RestaurantMenusActivity.class);
                intent.putExtra(RestaurantMenusActivity.RESTAURANT_ID_KEY, restaurantId);
                startActivity(intent);
            }
        });
        /**
         * Create on click listener to launch current meal
         */
        findViewById(R.id.currentMealButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), CurrentMealActivity.class));
            }
        });
        /**
         * Create on click listener to add/remove to favorites
         */
        isFavoriteIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                List<Restaurant> favoriteRestaurants = new ArrayList<>();
                try {
                    favoriteRestaurants = databaseService.getAllFavoriteRestaurants();
                } catch (ExecutionException | InterruptedException e) {
                    Log.e(LOGTAG, "Could not get all favorite restaurants", e);
                }
                final boolean isFavorited = favoriteRestaurants.contains(restaurant);

                if(isFavorited) {
                    isFavoriteIcon.setImageResource(R.drawable.favorite_unfilled);
                    try {
                        databaseService.removeRestaurantFromFavorites(restaurantId);
                    } catch (ExecutionException | InterruptedException e) {
                        Log.e(LOGTAG, "Could not remove restaurant from favorites. ID: ".concat(String.valueOf(restaurantId)), e);
                    }
                } else {
                    isFavoriteIcon.setImageResource(R.drawable.favorite_filled);
                    try {
                        databaseService.addRestaurantToFavorites(restaurantId);
                    } catch (ExecutionException | InterruptedException e) {
                        Log.e(LOGTAG, "Could not add restaurant to favorites. ID: ".concat(String.valueOf(restaurantId)), e);
                    }
                }
            }
        });

        setUpFoodFinder();
    }

    /**
     * Food finder service
     */
    private void setUpFoodFinder() {

        // Get all the sliders
        MultiSlider calSlider = findViewById(R.id.calorieSlider);

        int maxCalories = (int) Collections.max(allFoods, Constants.CALORIE_COMPARATOR).getNutritionalInfo().getCalories();
        int minCalories = (int) Collections.min(allFoods, Constants.CALORIE_COMPARATOR).getNutritionalInfo().getCalories();
        final TextView minCaloriesText = findViewById(R.id.minCaloriesText);
        final TextView maxCaloriesText = findViewById(R.id.maxCaloriesText);
        calSlider.setMin(minCalories, true, true);
        calSlider.setMax(maxCalories, true, true);
        minCaloriesText.setText(String.valueOf(minCalories));
        maxCaloriesText.setText(String.valueOf(maxCalories));
        calSlider.setOnThumbValueChangeListener(new MultiSlider.OnThumbValueChangeListener() {
            @Override
            public void onValueChanged(MultiSlider multiSlider,
                                       MultiSlider.Thumb thumb,
                                       int thumbIndex,
                                       int value)
            {
                if (thumbIndex == 0) {
                    minCaloriesText.setText(String.valueOf(value));
                } else {
                    maxCaloriesText.setText(String.valueOf(value));
                }
            }
        });
//        FoodFinderService foodFinderService = new FoodFinderService();
//        foodFinderService.setMinCalories(200);
//        foodFinderService.setMaxCalories(600);
//        foodFinderService.setMaxCarbs(50);
//        foodFinderService.setMinProtein(15);
//        foodFinderService.setMaxFat(15);
//        foodFinderService.findFoods(allFoods);
        //TODO!
    }

    // Allow for back navigation using the toolbar
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

}
