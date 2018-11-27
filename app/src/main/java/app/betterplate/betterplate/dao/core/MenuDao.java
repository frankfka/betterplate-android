package app.betterplate.betterplate.dao.core;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Query;

import java.util.List;

import app.betterplate.betterplate.data.core.Food;
import app.betterplate.betterplate.data.core.Menu;

@Dao
public interface MenuDao {

    @Query("SELECT * FROM menu WHERE restaurant_id = :restaurantId")
    List<Menu> getMenus(int restaurantId);

    @Query("SELECT * FROM food WHERE menu_id = :menuId")
    List<Food> getFoods(int menuId);

}
