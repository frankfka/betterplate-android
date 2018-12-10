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
import android.widget.Button;
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

        // Get all the inputs
        MultiSlider calSlider = findViewById(R.id.calorieSlider);
        MultiSlider proteinSlider = findViewById(R.id.proteinSlider);
        MultiSlider fatSlider = findViewById(R.id.fatsSlider);
        MultiSlider carbSlider = findViewById(R.id.carbsSlider);
        final Button gfButton = findViewById(R.id.glutenFreeButton);
        final Button vegetarianButton = findViewById(R.id.vegetarianButton);
        final Button veganButton = findViewById(R.id.veganButton);

        // Get all the textviews
        final TextView minCaloriesText = findViewById(R.id.minCaloriesText);
        final TextView maxCaloriesText = findViewById(R.id.maxCaloriesText);
        final TextView minProteinText = findViewById(R.id.minProteinText);
        final TextView maxProteinText = findViewById(R.id.maxProteinText);
        final TextView minFatText = findViewById(R.id.minFatText);
        final TextView maxFatText = findViewById(R.id.maxFatText);
        final TextView minCarbsText = findViewById(R.id.minCarbsText);
        final TextView maxCarbsText = findViewById(R.id.maxCarbsText);

        // Get all the supporting information
        boolean isGFSelected = false;
        boolean isVegetarianSelected = false;
        boolean isVeganSelected = false;

        //TODO could put this on another thread
        int maxCalories = (int) Collections.max(allFoods, Constants.CALORIE_COMPARATOR).getNutritionalInfo().getCalories();
        int minCalories = (int) Collections.min(allFoods, Constants.CALORIE_COMPARATOR).getNutritionalInfo().getCalories();
        int maxProtein = (int) Collections.max(allFoods, Constants.PROTEIN_COMPARATOR).getNutritionalInfo().getProtein();
        int minProtein = (int) Collections.min(allFoods, Constants.PROTEIN_COMPARATOR).getNutritionalInfo().getProtein();
        int maxFat = (int) Collections.max(allFoods, Constants.FAT_COMPARATOR).getNutritionalInfo().getFat();
        int minFat = (int) Collections.min(allFoods, Constants.FAT_COMPARATOR).getNutritionalInfo().getFat();
        int maxCarbs = (int) Collections.max(allFoods, Constants.CARBS_COMPARATOR).getNutritionalInfo().getCarbohydrates();
        int minCarbs = (int) Collections.min(allFoods, Constants.CARBS_COMPARATOR).getNutritionalInfo().getCarbohydrates();

        // Initialize all the sliders
        calSlider.setMin(minCalories, true, true);
        calSlider.setMax(maxCalories, true, true);
        proteinSlider.setMin(minProtein, true, true);
        proteinSlider.setMax(maxProtein, true, true);
        fatSlider.setMin(minFat, true, true);
        fatSlider.setMax(maxFat, true, true);
        carbSlider.setMin(minCarbs, true, true);
        carbSlider.setMax(maxCarbs, true, true);

        // Initialize all the text views
        minCaloriesText.setText(String.valueOf(minCalories).concat(" Cals"));
        maxCaloriesText.setText(String.valueOf(maxCalories).concat(" Cals"));
        minFatText.setText(String.valueOf(minFat).concat(" g"));
        maxFatText.setText(String.valueOf(maxFat).concat(" g"));
        minProteinText.setText(String.valueOf(minProtein).concat(" g"));
        maxProteinText.setText(String.valueOf(maxProtein).concat(" g"));
        minCarbsText.setText(String.valueOf(minCarbs).concat(" g"));
        maxCarbsText.setText(String.valueOf(maxCarbs).concat(" g"));

        // Set Listeners
        calSlider.setOnThumbValueChangeListener(new MultiSlider.OnThumbValueChangeListener() {
            @Override
            public void onValueChanged(MultiSlider multiSlider,
                                       MultiSlider.Thumb thumb,
                                       int thumbIndex,
                                       int value)
            {
                if (thumbIndex == 0) {
                    minCaloriesText.setText(String.valueOf(value).concat(" Cals"));
                } else {
                    maxCaloriesText.setText(String.valueOf(value).concat(" Cals"));
                }
            }
        });
        proteinSlider.setOnThumbValueChangeListener(new MultiSlider.OnThumbValueChangeListener() {
            @Override
            public void onValueChanged(MultiSlider multiSlider,
                                       MultiSlider.Thumb thumb,
                                       int thumbIndex,
                                       int value)
            {
                if (thumbIndex == 0) {
                    minProteinText.setText(String.valueOf(value).concat(" g"));
                } else {
                    maxProteinText.setText(String.valueOf(value).concat(" g"));
                }
            }
        });
        carbSlider.setOnThumbValueChangeListener(new MultiSlider.OnThumbValueChangeListener() {
            @Override
            public void onValueChanged(MultiSlider multiSlider,
                                       MultiSlider.Thumb thumb,
                                       int thumbIndex,
                                       int value)
            {
                if (thumbIndex == 0) {
                    minCarbsText.setText(String.valueOf(value).concat(" g"));
                } else {
                    maxCarbsText.setText(String.valueOf(value).concat(" g"));
                }
            }
        });
        fatSlider.setOnThumbValueChangeListener(new MultiSlider.OnThumbValueChangeListener() {
            @Override
            public void onValueChanged(MultiSlider multiSlider,
                                       MultiSlider.Thumb thumb,
                                       int thumbIndex,
                                       int value)
            {
                if (thumbIndex == 0) {
                    minFatText.setText(String.valueOf(value).concat(" g"));
                } else {
                    maxFatText.setText(String.valueOf(value).concat(" g"));
                }
            }
        });
        gfButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(gfButton.getCurrentTextColor() == getColor(R.color.colorText)) {
                    gfButton.setTextColor(getColor(R.color.colorAccent));
                    gfButton.setBackground(getDrawable(R.drawable.rounded_container_contrast_outline));
                } else {
                    gfButton.setTextColor(getColor(R.color.colorText));
                    gfButton.setBackground(getDrawable(R.drawable.rounded_container_primary_outline));
                }
            }
        });
        vegetarianButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(vegetarianButton.getCurrentTextColor() == getColor(R.color.colorText)) {
                    vegetarianButton.setTextColor(getColor(R.color.colorAccent));
                    vegetarianButton.setBackground(getDrawable(R.drawable.rounded_container_contrast_outline));
                } else {
                    vegetarianButton.setTextColor(getColor(R.color.colorText));
                    vegetarianButton.setBackground(getDrawable(R.drawable.rounded_container_primary_outline));
                }
            }
        });
        veganButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(veganButton.getCurrentTextColor() == getColor(R.color.colorText)) {
                    veganButton.setTextColor(getColor(R.color.colorAccent));
                    veganButton.setBackground(getDrawable(R.drawable.rounded_container_contrast_outline));
                } else {
                    veganButton.setTextColor(getColor(R.color.colorText));
                    veganButton.setBackground(getDrawable(R.drawable.rounded_container_primary_outline));
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
