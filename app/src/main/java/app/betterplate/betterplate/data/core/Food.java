package app.betterplate.betterplate.data.core;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Embedded;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;

import java.io.Serializable;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity(tableName = "food", foreignKeys = {
        @ForeignKey(entity = Menu.class,
                parentColumns = "menu_id",
                childColumns = "menu_id", onDelete = ForeignKey.CASCADE),},
        indices = @Index(name = "index_food", value = "food_id", unique = true))
public class Food implements Serializable {
    @PrimaryKey
    @ColumnInfo(name = "food_id")
    private int id;
    @ColumnInfo(name = "menu_id")
    private int parentMenuId;
    @ColumnInfo(name = "food_name")
    private String name;
    @ColumnInfo(name = "serving_size")
    private String servingSize;
    @Embedded
    private Nutrition nutritionalInfo;
    @ColumnInfo(name = "is_vegetarian")
    private int isVegetarian;
    @ColumnInfo(name = "is_vegan")
    private int isVegan;
    @ColumnInfo(name = "is_gf")
    private int isGF;
    @ColumnInfo(name = "is_featured")
    private int isFeatured;
    @Ignore
    private Set<FoodComponent> removedFoodComponents;
    @Ignore
    private Set<FoodComponent> addedFoodComponents;

    public Food(int id, String name, String servingSize, Nutrition nutritionalInfo, int isVegetarian, int isVegan, int isGF, int isFeatured) {
        this.removedFoodComponents = new HashSet<>();
        this.id = id;
        this.name = name;
        this.servingSize = servingSize;
        this.nutritionalInfo = nutritionalInfo;
        this.isVegetarian = isVegetarian;
        this.isVegan = isVegan;
        this.isGF = isGF;
        this.isFeatured = isFeatured;
    }

    public void removeFoodComponent(FoodComponent foodComponent) {

        // Add to set of removed items, change nutrition & change display strings
        if (!removedFoodComponents.contains(foodComponent)) {
            removedFoodComponents.add(foodComponent);
            Nutrition foodComponentNutrition = foodComponent.getNutrition();
            nutritionalInfo.setCalories(minusOrReturnZero(nutritionalInfo.getCalories(),foodComponentNutrition.getCalories()));
            nutritionalInfo.setCarbohydrates(minusOrReturnZero(nutritionalInfo.getCarbohydrates(), foodComponentNutrition.getCarbohydrates()));
            nutritionalInfo.setCholesterol(minusOrReturnZero(nutritionalInfo.getCholesterol(), foodComponentNutrition.getCholesterol()));
            nutritionalInfo.setFat(minusOrReturnZero(nutritionalInfo.getFat(), foodComponentNutrition.getFat()));
            nutritionalInfo.setFiber(minusOrReturnZero(nutritionalInfo.getFiber(), foodComponentNutrition.getFiber()));
            nutritionalInfo.setProtein(minusOrReturnZero(nutritionalInfo.getProtein(), foodComponentNutrition.getProtein()));
            nutritionalInfo.setSaturatedFat(minusOrReturnZero(nutritionalInfo.getSaturatedFat(), foodComponentNutrition.getSaturatedFat()));
            nutritionalInfo.setSodium(minusOrReturnZero(nutritionalInfo.getSodium(), foodComponentNutrition.getSodium()));
            nutritionalInfo.setSugar(minusOrReturnZero(nutritionalInfo.getSugar(), foodComponentNutrition.getSugar()));
            nutritionalInfo.setTransFat(minusOrReturnZero(nutritionalInfo.getTransFat(), foodComponentNutrition.getTransFat()));
        }
    }

    private double minusOrReturnZero(double minusFrom, double whatToMinus) {
        double result = minusFrom - whatToMinus;
        if (result < 0) {
            return 0;
        }
        return result;
    }

    public void addFoodComponent(FoodComponent foodComponent) {

        // Add to set of added items OR remove from set of removed items, change nutrition & change display strings
        if (removedFoodComponents.contains(foodComponent) || !addedFoodComponents.contains(foodComponent)) {
            if (removedFoodComponents.contains(foodComponent)) {
                removedFoodComponents.remove(foodComponent);
            } else {
                addedFoodComponents.add(foodComponent);
            }
            Nutrition foodComponentNutrition = foodComponent.getNutrition();
            nutritionalInfo.setCalories(nutritionalInfo.getCalories() + foodComponentNutrition.getCalories());
            nutritionalInfo.setCarbohydrates(nutritionalInfo.getCarbohydrates() + foodComponentNutrition.getCarbohydrates());
            nutritionalInfo.setCholesterol(nutritionalInfo.getCholesterol() + foodComponentNutrition.getCholesterol());
            nutritionalInfo.setFat(nutritionalInfo.getFat() + foodComponentNutrition.getFat());
            nutritionalInfo.setFiber(nutritionalInfo.getFiber() + foodComponentNutrition.getFiber());
            nutritionalInfo.setProtein(nutritionalInfo.getProtein() + foodComponentNutrition.getProtein());
            nutritionalInfo.setSaturatedFat(nutritionalInfo.getSaturatedFat() + foodComponentNutrition.getSaturatedFat());
            nutritionalInfo.setSodium(nutritionalInfo.getSodium() + foodComponentNutrition.getSodium());
            nutritionalInfo.setSugar(nutritionalInfo.getSugar() + foodComponentNutrition.getSugar());
            nutritionalInfo.setTransFat(nutritionalInfo.getTransFat() + foodComponentNutrition.getTransFat());
        }
    }

    public static Comparator<Food> SORT_BY_INC_CALS = new Comparator<Food>() {
        @Override
        public int compare(Food one, Food other) {
            return Nutrition.SORT_BY_INC_CALS.compare(one.getNutritionalInfo(), other.getNutritionalInfo());
        }
    };

    public static Comparator<Food> SORT_BY_DEC_PROTEIN = new Comparator<Food>() {
        @Override
        public int compare(Food one, Food other) {
            return Nutrition.SORT_BY_DEC_PROTEIN.compare(one.getNutritionalInfo(), other.getNutritionalInfo());
        }
    };

    public static Comparator<Food> SORT_BY_INC_CARBS = new Comparator<Food>() {
        @Override
        public int compare(Food one, Food other) {
            return Nutrition.SORT_BY_INC_CARBS.compare(one.getNutritionalInfo(), other.getNutritionalInfo());
        }
    };

    public static Comparator<Food> SORT_BY_INC_FAT = new Comparator<Food>() {
        @Override
        public int compare(Food one, Food other) {
            return Nutrition.SORT_BY_INC_FAT.compare(one.getNutritionalInfo(), other.getNutritionalInfo());
        }
    };

    public static Comparator<Food> DEFAULT_SORT = new Comparator<Food>() {
        @Override
        public int compare(Food one, Food other) {
            return one.getName().compareTo(other.getName());
        }
    };

    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        } else if (!(o instanceof Food)) {
            return false;
        } else {
            Food other = (Food) o;
            return this.id == other.getId();
        }
    }

}
