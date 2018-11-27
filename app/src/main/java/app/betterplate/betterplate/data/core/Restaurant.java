package app.betterplate.betterplate.data.core;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;

import java.util.Comparator;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@Entity (tableName = "restaurant", indices = @Index(value = "restaurant_id", name = "index_restaurant", unique = true))
public class Restaurant {
    @ColumnInfo(name = "restaurant_id")
    @PrimaryKey
    private int id;
    @ColumnInfo(name = "name")
    private String name;
    @ColumnInfo(name = "brand_color")
    private String brandColor;
    @ColumnInfo(name = "image_key")
    private String imageKey;
    @ColumnInfo(name = "health_score")
    private int numHealthStars;
    @ColumnInfo(name = "price_score")
    private int priceRating;
    @ColumnInfo(name = "min_calories")
    private double minCalories;
    @ColumnInfo(name = "max_calories")
    private double maxCalories;
    @ColumnInfo(name = "is_featured")
    private int isFeatured;

    public Restaurant(int id, String name, String brandColor, String imageKey, int numHealthStars,
                      int priceRating, double minCalories, double maxCalories, int isFeatured) {
        this.id = id;
        this.name = name;
        this.brandColor = brandColor;
        this.imageKey = imageKey;
        this.numHealthStars = numHealthStars;
        this.priceRating = priceRating;
        this.minCalories = minCalories;
        this.maxCalories = maxCalories;
        this.isFeatured = isFeatured;
    }

    public static Comparator<Restaurant> SORT_BY_ALPHABETICAL_ORDER = new Comparator<Restaurant>() {
        @Override
        public int compare(Restaurant one, Restaurant other) {
            return one.getName().compareToIgnoreCase(other.getName());
        }
    };

    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        } else if (!(o instanceof Restaurant)) {
            return false;
        } else {
            Restaurant other = (Restaurant) o;
            return this.id == other.getId();
        }
    }


}
