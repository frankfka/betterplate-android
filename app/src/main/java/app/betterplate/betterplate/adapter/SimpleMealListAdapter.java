package app.betterplate.betterplate.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import app.betterplate.betterplate.R;
import app.betterplate.betterplate.data.core.Food;
import app.betterplate.betterplate.service.StringFormatterService;

public class SimpleMealListAdapter extends RecyclerView.Adapter<SimpleMealListAdapter.MealListHolder> {

    private List<Food> foods;
    private static String LOGTAG = "SimpleMealListAdapter";

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
    public SimpleMealListAdapter(List<Food> foodsInMeal) {
        foods = foodsInMeal;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public MealListHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.simple_current_meal_item, parent, false);
        MealListHolder vh = new MealListHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(MealListHolder holder, final int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        TextView itemTitleView = holder.view.findViewById(R.id.mealItemTitle);
        TextView itemNutritionView = holder.view.findViewById(R.id.mealItemNutrition);

        final Food food = foods.get(position);
        itemTitleView.setText(food.getName());
        itemNutritionView.setText(StringFormatterService.getSimpleFoodListDescription(food));
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return foods.size();
    }

}
