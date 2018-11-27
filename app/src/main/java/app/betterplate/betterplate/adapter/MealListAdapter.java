package app.betterplate.betterplate.adapter;

import android.support.design.widget.Snackbar;
import android.support.v7.widget.AppCompatImageButton;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;
import java.util.concurrent.ExecutionException;

import app.betterplate.betterplate.R;
import app.betterplate.betterplate.activity.CurrentMealActivity;
import app.betterplate.betterplate.data.core.Food;
import app.betterplate.betterplate.service.MealService;
import app.betterplate.betterplate.service.StringFormatterService;

public class MealListAdapter extends RecyclerView.Adapter<MealListAdapter.MealListHolder> {

    private List<Food> foods;
    private MealService mealService;
    private CurrentMealActivity parentActivity;
    private static String LOGTAG = "MealListAdapter";

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public class MealListHolder extends RecyclerView.ViewHolder {
        public View view;

        public MealListHolder(View v) {
            super(v);
            view = v;
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public MealListAdapter(List<Food> foodsInMeal, CurrentMealActivity parentActivity) {
        foods = foodsInMeal;
        this.parentActivity = parentActivity;
        mealService = new MealService(parentActivity.getApplicationContext());
    }

    // Create new views (invoked by the layout manager)
    @Override
    public MealListHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.current_meal_item, parent, false);
        MealListHolder vh = new MealListHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(MealListHolder holder, final int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        TextView itemTitleView = holder.view.findViewById(R.id.mealItemTitle);
        TextView itemDetailsView = holder.view.findViewById(R.id.mealItemDetails);
        TextView itemNutritionView = holder.view.findViewById(R.id.mealItemNutrition);
        final AppCompatImageButton removeFromMealButton = holder.view.findViewById(R.id.deleteMealItemButton);

        final Food food = foods.get(position);

        itemTitleView.setText(food.getName());

        String itemDetailsString = StringFormatterService.getFoodServingSize(food);
        if(itemDetailsString.isEmpty()) {
            itemDetailsView.setVisibility(View.GONE);
        } else {
            itemDetailsView.setText(itemDetailsString);
        }


        itemDetailsView.setText(itemDetailsString.trim());
        itemNutritionView.setText(StringFormatterService.getFoodMenuDescription(food));

        final Snackbar removeFromMealSnackbar = Snackbar.make(parentActivity.findViewById(R.id.currentMealCoordinatorLayout), "Removed from Meal", Snackbar.LENGTH_LONG);
        removeFromMealSnackbar.setAction("Undo", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    mealService.insertFoodToMeal(food);
                    updateCurrentMeal();
                    parentActivity.updateCurrentMeal();
                } catch (ExecutionException | InterruptedException e) {
                    Log.e(LOGTAG, "Error undoing remove from meal. Food ID: ".concat(String.valueOf(food.getId())), e);
                }
            }
        });

        removeFromMealButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    mealService.removeFoodFromMeal(food);
                    updateCurrentMeal();
                    parentActivity.updateCurrentMeal();
                } catch (ExecutionException | InterruptedException e) {
                    Log.e(LOGTAG, "Error removing from meal. Food ID: ".concat(String.valueOf(food.getId())), e);
                }
                removeFromMealSnackbar.show();
            }
        });
    }

    public void updateCurrentMeal() {
        try {
            foods = mealService.getCurrentMeal();
        } catch (ExecutionException | InterruptedException e) {
            Log.e(LOGTAG, "Error getting current meal ", e);
        }
        notifyDataSetChanged();
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return foods.size();
    }

}
