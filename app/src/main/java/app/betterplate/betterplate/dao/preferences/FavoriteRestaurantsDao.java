package app.betterplate.betterplate.dao.preferences;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

import app.betterplate.betterplate.data.core.Restaurant;
import app.betterplate.betterplate.data.preferences.FavoriteRestaurant;

@Dao
public interface FavoriteRestaurantsDao {

    @Insert
    void insertToFavorites(FavoriteRestaurant favoriteRestaurant);

    @Delete
    void deleteFromFavorites(FavoriteRestaurant favoriteRestaurant);

    @Query("SELECT * FROM restaurant INNER JOIN pref_favorite_restaurants ON pref_favorite_restaurants.restaurant_id = restaurant.restaurant_id")
    List<Restaurant> getAllFavoriteRestaurants();

}
