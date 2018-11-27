package app.betterplate.betterplate.dao.core;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Query;

import java.util.List;

import app.betterplate.betterplate.data.core.FoodComponent;

@Dao
public interface FoodComponentDao {

    @Query("SELECT * FROM food_component JOIN food_component_join " +
            "ON food_component.food_component_id = food_component_join.food_component_id " +
            "WHERE food_id = :foodId")
    List<FoodComponent> getFoodComponentsFromFoodId(int foodId);

    @Query("SELECT * FROM food_component JOIN food_component_join " +
            "ON food_component.food_component_id = food_component_join.food_component_id " +
            "WHERE food_id = :foodId AND include_by_default = 1")
    List<FoodComponent> getDefaultFoodComponentsFromFoodId(int foodId);

}
