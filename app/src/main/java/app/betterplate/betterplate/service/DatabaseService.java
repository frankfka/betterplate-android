package app.betterplate.betterplate.service;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import app.betterplate.betterplate.dao.core.FoodComponentDao;
import app.betterplate.betterplate.data.core.Food;
import app.betterplate.betterplate.data.core.FoodComponent;
import app.betterplate.betterplate.data.database.MainDatabase;
import app.betterplate.betterplate.data.core.Menu;
import app.betterplate.betterplate.data.core.Restaurant;
import app.betterplate.betterplate.data.preferences.FavoriteRestaurant;

public class DatabaseService {

    private MainDatabase mainDatabase;

    public DatabaseService(Context context) {
        this.mainDatabase = MainDatabase.getInstance(context);
    }

    public List<Restaurant> getAllRestaurants() throws ExecutionException, InterruptedException {
        class GetAllRestaurantsTask extends AsyncTask<Void,Void,List<Restaurant>>
        {
            @Override
            protected List<Restaurant> doInBackground(Void... url) {
                return mainDatabase.restaurantDao().getAllRestaurants();
            }
        }
        return new GetAllRestaurantsTask().execute().get();
    }

    public Restaurant getRestaurantFromId(final int restaurantId) throws ExecutionException, InterruptedException {
        class GetRestaurantFromIdTask extends AsyncTask<Integer,Void,Restaurant>
        {
            @Override
            protected Restaurant doInBackground(Integer... restaurantId) {
                return mainDatabase.restaurantDao().getRestaurantFromId(restaurantId[0]);
            }
        }
        return new GetRestaurantFromIdTask().execute(restaurantId).get();
    }

    public List<Menu> getAllMenusFromRestaurant(final int restaurantId) throws ExecutionException, InterruptedException {
        class GetAllMenusTask extends AsyncTask<Integer,Void,List<Menu>>
        {
            @Override
            protected List<Menu> doInBackground(Integer... restaurantId) {
                return mainDatabase.menuDao().getMenus(restaurantId[0]);
            }
        }
        return new GetAllMenusTask().execute(restaurantId).get();
    }

    /**
     * THIS WILL FILTER ABOVE 10 CALS
     *
     * @param restaurantId
     * @return
     * @throws ExecutionException
     * @throws InterruptedException
     */
    public List<Food> getAllFoodsFromRestaurant(final int restaurantId) throws ExecutionException, InterruptedException {
        class GetAllFoodsFromRestaurantTask extends AsyncTask<Integer,Void,List<Food>>
        {
            @Override
            protected List<Food> doInBackground(Integer... restaurantId) {
                List<Food> allFoods = mainDatabase.foodDao().getAllFoodsForRestaurant(restaurantId[0]);
                List<Food> foodsAboveThreshold = new ArrayList<>();
                for (Food food : allFoods) {
                    if (food.getNutritionalInfo().getCalories() > 10) {
                        foodsAboveThreshold.add(food);
                    }
                }
                return foodsAboveThreshold;
            }
        }
        return new GetAllFoodsFromRestaurantTask().execute(restaurantId).get();
    }

    public List<Food> getFoodFromMenuId(final int menuId) throws ExecutionException, InterruptedException {
        class GetAllFoodFromMenuTask extends AsyncTask<Integer,Void,List<Food>>
        {
            @Override
            protected List<Food> doInBackground(Integer... menuId) {
                return mainDatabase.menuDao().getFoods(menuId[0]);
            }
        }
        return new GetAllFoodFromMenuTask().execute(menuId).get();
    }

    public Map<FoodComponent, Boolean> getFoodComponentsFromFood(final int foodId) throws ExecutionException, InterruptedException {
        class GetAllFoodComponentsTask extends AsyncTask<Integer,Void,Map<FoodComponent, Boolean>>
        {
            @Override
            protected Map<FoodComponent, Boolean> doInBackground(Integer... foodId) {
                FoodComponentDao foodComponentDao = mainDatabase.foodComponentDao();
                Map<FoodComponent, Boolean> foodComponents = new HashMap<>();
                for(FoodComponent foodComponent: foodComponentDao.getFoodComponentsFromFoodId(foodId[0])) {
                    foodComponents.put(foodComponent, false);
                }
                for(FoodComponent foodComponent: foodComponentDao.getDefaultFoodComponentsFromFoodId(foodId[0])) {
                    foodComponents.put(foodComponent, true);
                }
                return foodComponents;
            }
        }
        return new GetAllFoodComponentsTask().execute(foodId).get();
    }

    //TODO NEVER DELETE A RESTAURANT, NEED TO FIGURE OUT HOW TO NAVIGATE THIS
    public List<Restaurant> getAllFavoriteRestaurants() throws ExecutionException, InterruptedException {
        class GetAllFavoriteRestaurantsTask extends AsyncTask<Void,Void,List<Restaurant>>
        {
            @Override
            protected List<Restaurant> doInBackground(Void... url) {
                return mainDatabase.favoriteRestaurantsDao().getAllFavoriteRestaurants();
            }
        }
        return new GetAllFavoriteRestaurantsTask().execute().get();
    }

    public void addRestaurantToFavorites(final int restaurantId) throws ExecutionException, InterruptedException {
        class addRestaurantToFavoritesTask extends AsyncTask<Integer,Void,Void>
        {
            @Override
            protected Void doInBackground(Integer... restaurantId) {
                mainDatabase.favoriteRestaurantsDao().insertToFavorites(new FavoriteRestaurant(restaurantId[0]));
                return null;
            }
        }
        new addRestaurantToFavoritesTask().execute(restaurantId).get();
    }

    public void removeRestaurantFromFavorites(final int restaurantId) throws ExecutionException, InterruptedException {
        class removeRestaurantFromFavoritesTask extends AsyncTask<Integer,Void,Void>
        {
            @Override
            protected Void doInBackground(Integer... restaurantId) {
                mainDatabase.favoriteRestaurantsDao().deleteFromFavorites(new FavoriteRestaurant(restaurantId[0]));
                return null;
            }
        }
        new removeRestaurantFromFavoritesTask().execute(restaurantId).get();
    }

}
