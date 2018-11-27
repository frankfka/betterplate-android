package app.betterplate.betterplate.dao.core;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

import app.betterplate.betterplate.data.core.Food;


@Dao
public interface FoodDao {

    @Insert
    void insertOneFood(Food food);

    @Insert
    void insertFoods(List<Food> foods);

    @Query("SELECT * FROM food WHERE food_id = :id")
    Food getFoodFromId(int id);

    @Query("SELECT * FROM food JOIN menu " +
            "ON food.menu_id = menu.menu_id " +
            "WHERE restaurant_id = :restaurantId")
    List<Food> getAllFoodsForRestaurant(int restaurantId);

    @Update
    void updateFood(Food food);

    @Delete
    void deleteFood(Food food);


}
