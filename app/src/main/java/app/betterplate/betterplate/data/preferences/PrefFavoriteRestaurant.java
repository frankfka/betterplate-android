package app.betterplate.betterplate.data.preferences;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity(tableName = "favorite_restaurants")
public class PrefFavoriteRestaurant {

    @PrimaryKey
    @ColumnInfo(name = "restaurant_id")
    private int restaurantId;

    public PrefFavoriteRestaurant(int restaurantId) {
        this.restaurantId = restaurantId;
    }

}
