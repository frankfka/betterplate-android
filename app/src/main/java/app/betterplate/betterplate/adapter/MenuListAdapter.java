package app.betterplate.betterplate.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import app.betterplate.betterplate.R;
import app.betterplate.betterplate.activity.MenuItemDetailsActivity;
import app.betterplate.betterplate.data.core.Food;
import app.betterplate.betterplate.service.StringFormatterService;

public class MenuListAdapter extends RecyclerView.Adapter<MenuListAdapter.MenuListHolder> {

    private List<Food> foods;
    private List<Food> searchableFoods;
    private Context context;
    private static String LOGTAG = "MenuListAdapter";

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public class MenuListHolder extends RecyclerView.ViewHolder {
        public View view;

        public MenuListHolder(View v) {
            super(v);
            view = v;
            context = v.getContext();
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public MenuListAdapter(List<Food> foodsInMenu) {
        foods = foodsInMenu;
        searchableFoods = new ArrayList<>(foods);
    }

    // Create new views (invoked by the layout manager)
    @Override
    public MenuListHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.menu_item, parent, false);
        MenuListHolder vh = new MenuListHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(MenuListHolder holder, final int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        TextView itemTitleView = holder.view.findViewById(R.id.menuItemTitle);
        TextView itemDetailsView = holder.view.findViewById(R.id.menuItemDetails);
        TextView itemNutritionView = holder.view.findViewById(R.id.menuItemNutrition);
        ConstraintLayout parentLayout = holder.view.findViewById(R.id.menuItemMainConstraintLayout);

        final Food food = searchableFoods.get(position);

        itemTitleView.setText(food.getName());

        String itemDetailsString = StringFormatterService.getFoodServingSize(food);
        if(itemDetailsString.isEmpty()) {
            itemDetailsView.setVisibility(View.GONE);
        } else {
            itemDetailsView.setText(itemDetailsString);
        }

        itemDetailsView.setText(itemDetailsString.trim());
        itemNutritionView.setText(StringFormatterService.getFoodMenuDescription(food));

        parentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, MenuItemDetailsActivity.class);
                intent.putExtra(MenuItemDetailsActivity.FOOD_ID_KEY, food);
                context.startActivity(intent);
            }
        });

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return searchableFoods.size();
    }

    /**
     * This allows filtering via the search bar
     *
     * @param queryText
     */
    public void filter(String queryText) {
        searchableFoods.clear();
        if(queryText == null || queryText.isEmpty()){
            searchableFoods.addAll(foods);
        } else {
            queryText = queryText.trim().toLowerCase();

            for(Food food : foods){
                if(food.getName().toLowerCase().contains(queryText)){
                    searchableFoods.add(food);
                }
            }
        }
        notifyDataSetChanged();
    }

}
