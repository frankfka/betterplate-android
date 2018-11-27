package app.betterplate.betterplate.dao.core;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

import app.betterplate.betterplate.data.core.Food;
import app.betterplate.betterplate.data.core.MealItem;

@Dao
public interface MealDao {

    @Query("SELECT * FROM food JOIN meal_item " +
            "ON food.food_id = meal_item.food_id")
    List<Food> getCurrentMealFoods();

    @Delete
    void deleteFromMeal(MealItem mealItem);

    @Insert
    void addToMeal(MealItem mealItem);

    @Query("SELECT * FROM meal_item WHERE food_id = :foodId")
    List<MealItem> getMealItemsFromFoodId(int foodId);

    @Query("DELETE from meal_item")
    void deleteAllFoodsFromMeal();

}
