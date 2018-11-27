package app.betterplate.betterplate.data.core;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;

import lombok.Getter;
import lombok.Setter;

@Entity(tableName = "food_component_join",
        foreignKeys = {
                @ForeignKey(entity = FoodComponent.class,
                        parentColumns = "food_component_id",
                        childColumns = "food_component_id"),
                @ForeignKey(entity = Food.class,
                        parentColumns = "food_id",
                        childColumns = "food_id")
        },
        indices = @Index(name = "index_food_component_join", value = {"food_component_id", "food_id"}, unique = true))
@Setter
@Getter
public class FoodComponentJoin {
    @PrimaryKey
    @ColumnInfo(name = "food_component_join_id")
    private int id;
    @ColumnInfo(name = "food_component_id")
    private int foodComponentId;
    @ColumnInfo(name = "food_id")
    private int foodId;
    @ColumnInfo(name = "include_by_default")
    private int includeByDefault;
}
