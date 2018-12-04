package app.betterplate.betterplate.dao.preferences;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

import app.betterplate.betterplate.data.preferences.PrefFavoriteRestaurant;

@Dao
public interface PrefFavoriteRestaurantsDao {

    @Insert
    void insertToFavorites(PrefFavoriteRestaurant favoriteRestaurant);

    @Delete
    void deleteFromFavorites(PrefFavoriteRestaurant favoriteRestaurant);

    @Query("SELECT * from favorite_restaurants")
    List<PrefFavoriteRestaurant> getAllFavorites();

}
