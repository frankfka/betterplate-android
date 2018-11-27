package app.betterplate.betterplate.data.core;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Builder
@Entity (tableName = "menu",
         foreignKeys = {
            @ForeignKey(entity = Restaurant.class,
                parentColumns = "restaurant_id",
                childColumns = "restaurant_id")
         },
         indices = @Index(name = "index_menu", value = "menu_id", unique = true))
public class Menu {

    @PrimaryKey
    @ColumnInfo(name = "menu_id")
    private int id;
    @ColumnInfo(name = "menu_name")
    private String name;
    @ColumnInfo(name = "restaurant_id")
    private int parentRestaurant;

    public Menu(int id, String name, int parentRestaurant) {
        this.id = id;
        this.name = name;
        this.parentRestaurant = parentRestaurant;
    }


    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        } else if (!(o instanceof Menu)) {
            return false;
        } else {
            Menu other = (Menu) o;
            return this.id == other.getId();
        }
    }

}
