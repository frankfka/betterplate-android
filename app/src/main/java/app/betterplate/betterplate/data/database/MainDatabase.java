package app.betterplate.betterplate.data.database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import app.betterplate.betterplate.dao.core.FoodComponentDao;
import app.betterplate.betterplate.dao.core.FoodDao;
import app.betterplate.betterplate.dao.core.MealDao;
import app.betterplate.betterplate.dao.core.MenuDao;
import app.betterplate.betterplate.dao.core.RestaurantDao;
import app.betterplate.betterplate.dao.preferences.FavoriteRestaurantsDao;
import app.betterplate.betterplate.data.core.Food;
import app.betterplate.betterplate.data.core.FoodComponent;
import app.betterplate.betterplate.data.core.FoodComponentJoin;
import app.betterplate.betterplate.data.core.MealItem;
import app.betterplate.betterplate.data.core.Menu;
import app.betterplate.betterplate.data.core.Restaurant;
import app.betterplate.betterplate.data.preferences.FavoriteRestaurant;
import app.betterplate.betterplate.data.preferences.PrefFavoriteRestaurant;

@Database(entities = {Food.class, FoodComponent.class,
        FoodComponentJoin.class, Menu.class, Restaurant.class, MealItem.class
        , FavoriteRestaurant.class}, version = 2,
        exportSchema = false)
public abstract class MainDatabase extends RoomDatabase {
    private static volatile MainDatabase instance;

    public static synchronized MainDatabase getInstance(Context context) {
        if (instance == null) {
            instance = create(context);
        }
        return instance;
    }

    public abstract FoodDao foodDao();

    public abstract RestaurantDao restaurantDao();

    public abstract MenuDao menuDao();

    public abstract MealDao mealDao();

    public abstract FoodComponentDao foodComponentDao();

    public abstract FavoriteRestaurantsDao favoriteRestaurantsDao();

    private static MainDatabase create(final Context context) {
        List<PrefFavoriteRestaurant> userFavoriteRestaurants = new ArrayList<>(PreferencesDatabase.getInstance(context).prefFavoriteRestaurantsDao().getAllFavorites());
        PreferencesDatabase.getInstance(context).close();
        MainDatabase mainDatabase = (MainDatabase) RoomPreloadUtil.getPreloadedDatabaseBuilder(context, MainDatabase.class, "main_database.db", true).allowMainThreadQueries().build();
        mainDatabase.favoriteRestaurantsDao().deleteAllFavorites();
        for (PrefFavoriteRestaurant prefFavoriteRestaurant: userFavoriteRestaurants) {
            mainDatabase.favoriteRestaurantsDao().insertToFavorites(new FavoriteRestaurant(prefFavoriteRestaurant.getRestaurantId()));
        }
        return mainDatabase;
    }

    @Override
    public void close() {
        super.close();
        instance = null;
    }

}
