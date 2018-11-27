package app.betterplate.betterplate.data.core;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Embedded;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity(tableName = "food_component", indices = @Index(name = "index_food_component", value = "food_component_id", unique = true))
public class FoodComponent {
    @PrimaryKey
    @ColumnInfo(name = "food_component_id")
    private int id;
    @ColumnInfo(name = "food_component_name")
    private String name;
    @Embedded
    private Nutrition nutrition;

    public FoodComponent(int id, String name, Nutrition nutrition) {
        this.id = id;
        this.name = name;
        this.nutrition = nutrition;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof FoodComponent)) {
            return false;
        }
        FoodComponent foodComponent = (FoodComponent) o;
        return foodComponent.getId() == id;
    }

    @Override
    public int hashCode() {
        return id;
    }

}
