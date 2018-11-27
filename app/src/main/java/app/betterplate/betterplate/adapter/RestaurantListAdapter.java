package app.betterplate.betterplate.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import app.betterplate.betterplate.Constants;
import app.betterplate.betterplate.R;
import app.betterplate.betterplate.RestaurantOverviewActivity;
import app.betterplate.betterplate.activity.RestaurantMenusActivity;
import app.betterplate.betterplate.data.core.Restaurant;

public class RestaurantListAdapter extends RecyclerView.Adapter<RestaurantListAdapter.RestaurantListHolder> {

    private Context context;
    private List<Restaurant> allRestaurants;
    private List<Restaurant> searchableRestaurants;
    private Activity parentActivity;
    private static String LOGTAG = "RestaurantListAdapter";

    // Provide a suitable constructor (depends on the kind of dataset)
    public RestaurantListAdapter(List<Restaurant> allRestaurants, Activity activity) {
        this.allRestaurants = allRestaurants;
        this.searchableRestaurants = new ArrayList<>(allRestaurants);
        this.parentActivity = activity;
    }

    public class RestaurantListHolder extends RecyclerView.ViewHolder {
        public View view;

        public RestaurantListHolder(View v) {
            super(v);
            view = v;
            context = v.getContext();
        }
    }

    @NonNull
    @Override
    public RestaurantListAdapter.RestaurantListHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int position) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.restaurant_list_item, viewGroup, false);

        RestaurantListHolder vh = new RestaurantListHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull RestaurantListAdapter.RestaurantListHolder restaurantListHolder, int position) {

        ConstraintLayout container = restaurantListHolder.view.findViewById(R.id.restaurantListItem);
        TextView restaurantTitle = restaurantListHolder.view.findViewById(R.id.restaurantListItemTitle);
        // TODO deal with description
//        TextView restaurantDescription = restaurantListHolder.view.findViewById(R.id.restaurantListItemDescription);
        ImageView restaurantLogo = restaurantListHolder.view.findViewById(R.id.restaurantListItemImage);
        final Restaurant restaurant = searchableRestaurants.get(position);

        restaurantTitle.setText(restaurant.getName());
//        restaurantDescription.setText(StringFormatterService.getRestaurantDescription(restaurant));

        try {
            Bitmap testBitmap = BitmapFactory.decodeStream(parentActivity.getAssets().open(Constants.RESTAURANT_HEADER_IMAGE_FOLDER.concat(restaurant.getImageKey())));
            restaurantLogo.setImageBitmap(testBitmap);
        } catch (Exception e) {
            Log.e(LOGTAG, "Error retrieving header image for restaurant ID ".concat(String.valueOf(restaurant.getId())), e);
        }

        container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, RestaurantOverviewActivity.class);
                intent.putExtra(RestaurantMenusActivity.RESTAURANT_ID_KEY, restaurant.getId());
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return searchableRestaurants.size();
    }

    /**
     * This allows searching
     *
     * @param queryText
     */
    public void filter(String queryText) {
        searchableRestaurants.clear();
        if(queryText == null || queryText.isEmpty()){
            searchableRestaurants.addAll(allRestaurants);
        } else {
            queryText = queryText.trim().toLowerCase();

            for (Restaurant restaurant : allRestaurants) {
                if (restaurant.getName().toLowerCase().contains(queryText)) {
                    searchableRestaurants.add(restaurant);
                }
            }
        }
        notifyDataSetChanged();
    }

}
