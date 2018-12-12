package app.betterplate.betterplate.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import app.betterplate.betterplate.R;
import app.betterplate.betterplate.adapter.FoodComponentListAdapter;
import app.betterplate.betterplate.adapter.FoodNutritionListAdapter;
import app.betterplate.betterplate.data.core.Food;
import app.betterplate.betterplate.data.core.FoodComponent;
import app.betterplate.betterplate.data.core.Nutrition;
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
        Nutrition nutrition = food.getNutritionalInfo();

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
        foodNutritionListAdapter = new FoodNutritionListAdapter(nutrition);
        foodNutritionList.setAdapter(foodNutritionListAdapter);
        updateFoodInformation();

        // Nutrition breakdown charting
        PieChart chart = findViewById(R.id.nutrition_breakdown_chart);
        List<PieEntry> entries = new ArrayList<>();

        double totalMacros = nutrition.getCarbohydrates() + nutrition.getFat() + nutrition.getProtein();
        float percentageCarbs = (float) (nutrition.getCarbohydrates() / totalMacros);
        float percentageFat = (float) (nutrition.getFat() / totalMacros);
        float percentageProtein = (float) (nutrition.getProtein() / totalMacros);
        entries.add(new PieEntry(percentageProtein, "Protein"));
        entries.add(new PieEntry(percentageCarbs, "Carbohydrates"));
        entries.add(new PieEntry(percentageFat, "Fat"));

        PieDataSet nutritionBreakdownDataset = new PieDataSet(entries, "");
        nutritionBreakdownDataset.setColors(ColorTemplate.MATERIAL_COLORS);
        nutritionBreakdownDataset.setSelectionShift(0);
        PieData nutritionData = new PieData(nutritionBreakdownDataset);
        if(percentageCarbs < 0.1 && percentageFat < 0.1 || percentageCarbs < 0.1 && percentageProtein < 0.1
                || percentageFat < 0.1 && percentageProtein < 0.1) {
            nutritionData.setDrawValues(false);
        }
        nutritionData.setValueTextColor(Color.WHITE);
        nutritionData.setValueTextSize(16);
        nutritionData.setValueFormatter(new PercentFormatter());
        chart.setDrawEntryLabels(false);
        chart.getDescription().setEnabled(false);
        chart.setHoleRadius(20f);
        chart.getLegend().setTextSize(16);
        chart.getLegend().setFormSize(16);
        chart.getLegend().setWordWrapEnabled(true);
        chart.getLegend().setPosition(Legend.LegendPosition.RIGHT_OF_CHART_CENTER);
        chart.setEntryLabelColor(Color.WHITE);
        chart.setTransparentCircleRadius(0);
        chart.setUsePercentValues(true);
        chart.setTouchEnabled(false);
        chart.setData(nutritionData);
        chart.invalidate();

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
