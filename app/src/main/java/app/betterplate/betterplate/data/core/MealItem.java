package app.betterplate.betterplate.data.core;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.PrimaryKey;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity(tableName = "meal_item", foreignKeys = {
        @ForeignKey(entity = Food.class,
                parentColumns = "food_id",
                childColumns = "food_id"),})
public class MealItem {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "meal_item_id")
    private int id;
    @ColumnInfo(name = "food_id")
    private int foodId;
}
