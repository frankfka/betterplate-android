package app.betterplate.betterplate.dao.core;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

import app.betterplate.betterplate.data.core.Restaurant;

@Dao
public interface RestaurantDao {

    @Insert
    void insertOneRestaurant(Restaurant restaurant);

    @Insert
    void insertRestaurants(List<Restaurant> restaurants);

    @Query("SELECT * FROM restaurant WHERE restaurant_id = :id")
    Restaurant getRestaurantFromId(int id);

    @Query("SELECT * FROM restaurant WHERE name = :restaurantName")
    Restaurant getRestaurantFromName(String restaurantName);

    @Query("SELECT * FROM restaurant WHERE is_featured = 1")
    List<Restaurant> getAllFeaturedRestaurants();

    @Query("SELECT * FROM restaurant")
    List<Restaurant> getAllRestaurants();

    @Update
    void updateRestaurant(Restaurant restaurant);

    @Delete
    void deleteRestaurant(Restaurant restaurant);

}
