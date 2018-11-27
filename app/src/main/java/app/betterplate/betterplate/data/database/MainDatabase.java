package app.betterplate.betterplate.data.database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import com.huma.room_for_asset.RoomAsset;

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
        return RoomAsset.databaseBuilder(
                context, MainDatabase.class, "main_database.db").fallbackToDestructiveMigration().build();
    }

}
