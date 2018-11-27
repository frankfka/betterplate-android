package app.betterplate.betterplate.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import app.betterplate.betterplate.R;
import app.betterplate.betterplate.data.core.Nutrition;

public class FoodNutritionListAdapter extends RecyclerView.Adapter<FoodNutritionListAdapter.FoodNutritionListHolder> {

    private Nutrition nutrition;
    private List<String> availableNutritionLabels;
    private List<String> availableNutritionValues;
    private static String LOGTAG = "FoodNutritionListAdapter";

    // Provide a suitable constructor (depends on the kind of dataset)
    public FoodNutritionListAdapter(Nutrition nutrition) {
        this.nutrition = nutrition;
        updateVariables();
    }

    public class FoodNutritionListHolder extends RecyclerView.ViewHolder {
        public View view;

        public FoodNutritionListHolder(View v) {
            super(v);
            view = v;
        }
    }

    @NonNull
    @Override
    public FoodNutritionListHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int position) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.menu_item_nutrition_item, viewGroup, false);

        FoodNutritionListHolder vh = new FoodNutritionListHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull final FoodNutritionListHolder foodCustomizationListHolder, int position) {

        updateVariables();

        TextView nutritionLabelView = foodCustomizationListHolder.view.findViewById(R.id.nutritionItemTitle);
        TextView nutritionValueView = foodCustomizationListHolder.view.findViewById(R.id.nutritionItemValue);

        final String nutritionLabel = availableNutritionLabels.get(position);
        final String nutritionValue = availableNutritionValues.get(position);

        nutritionLabelView.setText(nutritionLabel);
        nutritionValueView.setText(nutritionValue);
    }

    @Override
    public int getItemCount() {
        return availableNutritionValues.size();
    }

    private void updateVariables() {
        LinkedHashMap<String,String> nutritionalInfo = nutrition.getAvailableNutritionDetails();
        this.availableNutritionLabels = new ArrayList<>(nutritionalInfo.keySet());
        this.availableNutritionValues = new ArrayList<>(nutritionalInfo.values());
    }

}
