package app.betterplate.betterplate.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import app.betterplate.betterplate.R;
import app.betterplate.betterplate.activity.MenuItemDetailsActivity;
import app.betterplate.betterplate.data.core.Food;
import app.betterplate.betterplate.data.core.FoodComponent;
import app.betterplate.betterplate.service.DatabaseService;
import app.betterplate.betterplate.service.StringFormatterService;

public class FoodComponentListAdapter extends RecyclerView.Adapter<FoodComponentListAdapter.FoodCustomizationListHolder> {

    private MenuItemDetailsActivity parentActivity;
    private Food food;
    private List<FoodComponent> foodComponents;
    private Map<FoodComponent, Boolean> foodComponentsMap;
    private static String LOGTAG = "FoodComponentListAdapter";

    // Provide a suitable constructor (depends on the kind of dataset)
    public FoodComponentListAdapter(Food food, MenuItemDetailsActivity parentActivity) {
        this.food = food;
        this.parentActivity = parentActivity;
        try {
            foodComponentsMap = new DatabaseService(parentActivity.getApplicationContext()).getFoodComponentsFromFood(food.getId());
            foodComponents = new ArrayList<>(foodComponentsMap.keySet());
        } catch (ExecutionException | InterruptedException e) {
            Log.e(LOGTAG, "Error retrieving food components for Food ID ".concat(String.valueOf(food.getId())), e);
        }
    }

    public class FoodCustomizationListHolder extends RecyclerView.ViewHolder {
        public View view;

        public FoodCustomizationListHolder(View v) {
            super(v);
            view = v;
        }
    }

    @NonNull
    @Override
    public FoodCustomizationListHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int position) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.menu_item_customization_item, viewGroup, false);

        FoodCustomizationListHolder vh = new FoodCustomizationListHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull final FoodCustomizationListHolder foodCustomizationListHolder, int position) {
        TextView foodCustomizationTitle = foodCustomizationListHolder.view.findViewById(R.id.foodCustomizationItemTitle);
        TextView foodCustomizationDetails = foodCustomizationListHolder.view.findViewById(R.id.foodCustomizationItemDetails);
        Switch includeFoodToggle = foodCustomizationListHolder.view.findViewById(R.id.foodCustomizationItemSwitch);
        final FoodComponent foodComponent = foodComponents.get(position);

        includeFoodToggle.setChecked(foodComponentsMap.get(foodComponent));
        foodCustomizationTitle.setText(foodComponent.getName());
        foodCustomizationDetails.setText(StringFormatterService.getFoodComponentDescription(foodComponent));

        includeFoodToggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            if(!isChecked) {
                food.removeFoodComponent(foodComponent);
            } else {
                food.addFoodComponent(foodComponent);
            }
            parentActivity.updateFoodInformation();
            }
        });
    }

    @Override
    public int getItemCount() {
        return foodComponents.size();
    }

}
