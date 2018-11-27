package app.betterplate.betterplate.data.preferences;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.PrimaryKey;

import app.betterplate.betterplate.data.core.Food;
import app.betterplate.betterplate.data.core.FoodComponent;
import app.betterplate.betterplate.data.core.Restaurant;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity(tableName = "pref_favorite_restaurants", foreignKeys = {
        @ForeignKey(entity = Restaurant.class,
                parentColumns = "restaurant_id",
                childColumns = "restaurant_id"),
})
public class FavoriteRestaurant {
    @PrimaryKey
    @ColumnInfo(name = "restaurant_id")
    private int restaurantId;

    public FavoriteRestaurant(int restaurantId) {
        this.restaurantId = restaurantId;
    }

}
