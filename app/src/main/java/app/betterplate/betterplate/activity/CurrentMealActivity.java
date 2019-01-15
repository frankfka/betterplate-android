package app.betterplate.betterplate.activity;

import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;
import android.widget.TextView;

import com.github.mikephil.charting.charts.PieChart;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutionException;

import app.betterplate.betterplate.R;
import app.betterplate.betterplate.adapter.FoodNutritionListAdapter;
import app.betterplate.betterplate.adapter.MealListAdapter;
import app.betterplate.betterplate.data.core.Food;
import app.betterplate.betterplate.data.core.Nutrition;
import app.betterplate.betterplate.service.FoodService;
import app.betterplate.betterplate.service.MealService;
import app.betterplate.betterplate.service.StringFormatterService;
import app.betterplate.betterplate.service.ViewHelperService;

public class CurrentMealActivity extends AppCompatActivity {

    private static String LOGTAG = "CurrentMealActivity";
    private MealService mealService;
    private List<Food> currentMealFoods;
    private Nutrition currentMealNutrition;
    private FoodNutritionListAdapter foodNutritionListAdapter;
    private MealListAdapter mealListAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_current_meal);

        mealService = new MealService(this);
        updateCurrentMeal();

        setSupportActionBar((Toolbar) findViewById(R.id.toolBar));
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);

        RecyclerView mealFoodsList = findViewById(R.id.currentMealRecyclerView);
        mealListAdapter = new MealListAdapter(currentMealFoods, this);
        mealFoodsList.setHasFixedSize(true);
        mealFoodsList.setLayoutManager(new LinearLayoutManager(this));
        mealFoodsList.setAdapter(mealListAdapter);

        findViewById(R.id.clearMealTextView).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                /**
                 * Set up undo
                 */
                final List<Food> previousMealFoods = new ArrayList<>(currentMealFoods);
                final Snackbar removeFromMealSnackbar = Snackbar.make(findViewById(R.id.currentMealNestedScrollview), "Cleared Meal", Snackbar.LENGTH_LONG);
                removeFromMealSnackbar.setAction("Undo", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try {
                            for(Food mealItem : previousMealFoods) {
                                mealService.insertFoodToMeal(mealItem);
                            }
                            updateCurrentMeal();
                        } catch (ExecutionException | InterruptedException e) {
                            Log.e(LOGTAG, "Error undoing remove all meal items", e);
                        }
                    }
                });

                try {
                    mealService.removeAllFoodsFromMeal();
                    removeFromMealSnackbar.show();
                } catch (ExecutionException | InterruptedException e) {
                    Log.e(LOGTAG, "Error removing all foods from meal", e);
                }

                updateCurrentMeal();
            }
        });

    }

    // This allows for back navigation
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    // Called whenever the meal is updated
    public void updateCurrentMeal() {
        try {
            currentMealFoods = mealService.getCurrentMeal();
        } catch (ExecutionException | InterruptedException e) {
            Log.e(LOGTAG, "Error removing getting current meal", e);

        }
        // Help prompt if there are no foods
        TextView mealItemsHelpPrompt = findViewById(R.id.mealActivityNoMealTextView);
        if (currentMealFoods.isEmpty()) {
            mealItemsHelpPrompt.setVisibility(View.VISIBLE);
        } else {
            mealItemsHelpPrompt.setVisibility(View.GONE);
        }
        currentMealNutrition = mealService.getMealNutrition(currentMealFoods);
        ((TextView) findViewById(R.id.mealSummarySubtext)).setText(StringFormatterService.getMealSubtitleDescription(currentMealNutrition));
        // List of nutrition items
        foodNutritionListAdapter = new FoodNutritionListAdapter(currentMealNutrition);
        RecyclerView mealNutritionList = findViewById(R.id.mealNutritionRecyclerView);
        mealNutritionList.setHasFixedSize(true);
        mealNutritionList.setLayoutManager(new LinearLayoutManager(this));
        mealNutritionList.setAdapter(foodNutritionListAdapter);

        if(mealListAdapter != null) {
            mealListAdapter.updateCurrentMeal();
        }

        // Pie chart
        PieChart chart = findViewById(R.id.nutrition_breakdown_chart);
        chart.setNoDataText("Add items to your meal to see the macronutrient breakdown.");
        chart.setNoDataTextColor(R.color.colorText);
        if(!currentMealFoods.isEmpty()) {
            SparseArray<Double> macros = FoodService.getMacrosInPercent(currentMealFoods);
            ViewHelperService.setUpNutritionPieChart(chart,
                    macros.get(FoodService.CARBS).floatValue(),
                    macros.get(FoodService.FAT).floatValue(),
                    macros.get(FoodService.PROTEIN).floatValue());
        } else {
            chart.setData(null);
            chart.invalidate();
        }

    }


}
