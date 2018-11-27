package app.betterplate.betterplate.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.Map;
import java.util.concurrent.ExecutionException;

import app.betterplate.betterplate.R;
import app.betterplate.betterplate.adapter.FoodComponentListAdapter;
import app.betterplate.betterplate.adapter.FoodNutritionListAdapter;
import app.betterplate.betterplate.data.core.Food;
import app.betterplate.betterplate.data.core.FoodComponent;
import app.betterplate.betterplate.service.MealService;
import app.betterplate.betterplate.service.DatabaseService;
import app.betterplate.betterplate.service.StringFormatterService;

public class MenuItemDetailsActivity extends AppCompatActivity {

    public static final String FOOD_ID_KEY = "FOOD_ID_KEY";
    private Toolbar toolbar;
    private Food food;
    private Button addToMealButton;
    private RecyclerView.Adapter foodNutritionListAdapter;
    private DatabaseService databaseService;
    private MealService mealService;

    private static final String LOGTAG = "MenuItemDetailsActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_item_details);

        databaseService = new DatabaseService(this);
        mealService = new MealService(this);

        // Set up toolbar
        toolbar = findViewById(R.id.toolBar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);

        food = (Food) getIntent().getSerializableExtra(FOOD_ID_KEY);

        // Title
        TextView title = findViewById(R.id.itemPageTitle);
        title.setText(food.getName());

        // Serving Size
        TextView servingSize = findViewById(R.id.itemPageTitleSubDescriptionServingSize);
        String itemDetailsString = StringFormatterService.getFoodServingSize(food);
        if(itemDetailsString.isEmpty()) {
            servingSize.setVisibility(View.GONE);
        } else {
            servingSize.setText(itemDetailsString);
            //TODO investigate this, doesn't work
            servingSize.bringToFront();
        }

        // List of food components
        RecyclerView foodComponentList = findViewById(R.id.foodComponentsList);
        foodComponentList.setHasFixedSize(true);
        foodComponentList.setLayoutManager(new LinearLayoutManager(this));
        Map<FoodComponent, Boolean> foodComponents = null;
        try {
            foodComponents = databaseService.getFoodComponentsFromFood(food.getId());
        } catch (ExecutionException | InterruptedException e) {
            Log.e(LOGTAG, "Error retrieving food components from food ID ".concat(String.valueOf(food.getId())), e);
        }
        if (foodComponents != null && !foodComponents.isEmpty()) {
            RecyclerView.Adapter foodComponentListAdapter = new FoodComponentListAdapter(food, this);
            foodComponentList.setAdapter(foodComponentListAdapter);
        } else {
            foodComponentList.setVisibility(View.GONE);
            findViewById(R.id.itemPageSectionHeaderCustomization).setVisibility(View.GONE);
        }

        // List of nutrition items
        RecyclerView foodNutritionList = findViewById(R.id.foodNutritionList);
        foodNutritionList.setHasFixedSize(true);
        foodNutritionList.setLayoutManager(new LinearLayoutManager(this));
        foodNutritionListAdapter = new FoodNutritionListAdapter(food.getNutritionalInfo());
        foodNutritionList.setAdapter(foodNutritionListAdapter);

        updateFoodInformation();

        /**
         * Set up add to meal button and snackbar
         */
        final Snackbar addToMealSnackBar = Snackbar.make(findViewById(R.id.menuItemCoordinatorLayout), "Added to Meal", Snackbar.LENGTH_LONG);
        addToMealSnackBar.setAction("Undo", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    mealService.removeFoodFromMeal(food);
                } catch (ExecutionException | InterruptedException e) {
                    Log.e(LOGTAG, "Error removing from meal. Food ID: ".concat(String.valueOf(food.getId())), e);
                }
            }
        });
        addToMealButton = findViewById(R.id.addToMealButton);
        addToMealButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                try {
                    mealService.insertFoodToMeal(food);
                    addToMealSnackBar.show();
                } catch (ExecutionException | InterruptedException e) {
                    Log.e(LOGTAG, "Error adding to meal. Food ID: ".concat(String.valueOf(food.getId())), e);
                }
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

    public void updateFoodInformation() {
        // Subtitle
        TextView nutritionSubDescription = findViewById(R.id.itemPageTitleSubDescription);
        nutritionSubDescription.setText(StringFormatterService.getFoodSubtitleDescription(food));

        // Nutritional labels and values
        foodNutritionListAdapter.notifyDataSetChanged();
    }

}
