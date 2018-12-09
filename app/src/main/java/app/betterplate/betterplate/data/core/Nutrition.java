package app.betterplate.betterplate.data.core;

import android.arch.persistence.room.ColumnInfo;

import java.io.Serializable;
import java.util.Comparator;
import java.util.LinkedHashMap;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class Nutrition implements Serializable {
    private double calories;
    private double carbohydrates;
    private double protein;
    private double fat;
    @ColumnInfo(name = "saturated_fat")
    private double saturatedFat;
    @ColumnInfo(name = "trans_fat")
    private double transFat;
    private double cholesterol;
    private double sodium;
    private double fiber;
    private double sugar;
    @ColumnInfo(name = "vit_a")
    private double vitaminA;
    @ColumnInfo(name = "vit_c")
    private double vitaminC;
    private double calcium;
    private double iron;

    public Nutrition(double calories, double carbohydrates, double protein, double fat, double saturatedFat, double transFat, double cholesterol, double sodium, double fiber, double sugar, double vitaminA,
                     double vitaminC, double calcium, double iron) {
        this.calories = calories;
        this.carbohydrates = carbohydrates;
        this.protein = protein;
        this.fat = fat;
        this.saturatedFat = saturatedFat;
        this.transFat = transFat;
        this.cholesterol = cholesterol;
        this.sodium = sodium;
        this.fiber = fiber;
        this.sugar = sugar;
        this.vitaminA = vitaminA;
        this.vitaminC = vitaminC;
        this.calcium = calcium;
        this.iron = iron;
    }

    public LinkedHashMap<String,String> getAvailableNutritionDetails() {

        LinkedHashMap<String, String> availableNutritionDetailsMap = new LinkedHashMap<>();
        availableNutritionDetailsMap.put("Calories", String.valueOf(this.calories).concat(" Cal"));
        availableNutritionDetailsMap.put("Total Fat", String.valueOf(this.fat).concat(" g"));
        availableNutritionDetailsMap.put("Saturated Fat", String.valueOf(this.saturatedFat).concat(" g"));
        availableNutritionDetailsMap.put("Trans Fat", String.valueOf(this.transFat).concat(" g"));
        availableNutritionDetailsMap.put("Cholesterol", String.valueOf(this.cholesterol).concat(" mg"));
        availableNutritionDetailsMap.put("Sodium", String.valueOf(this.sodium).concat(" mg"));
        availableNutritionDetailsMap.put("Carbohydrates", String.valueOf(this.carbohydrates).concat(" g"));
        availableNutritionDetailsMap.put("Fiber", String.valueOf(this.fiber).concat(" g"));
        availableNutritionDetailsMap.put("Sugar", String.valueOf(this.sugar).concat(" g"));
        availableNutritionDetailsMap.put("Protein", String.valueOf(this.protein).concat(" g"));
        availableNutritionDetailsMap.put("Calcium", String.valueOf(this.calcium).concat(" %"));
        availableNutritionDetailsMap.put("Iron", String.valueOf(this.iron).concat(" %"));
        availableNutritionDetailsMap.put("Vitamin A", String.valueOf(this.vitaminA).concat( "%"));
        availableNutritionDetailsMap.put("Vitamin C", String.valueOf(this.vitaminC).concat( "%"));

        return availableNutritionDetailsMap;
    }

    //TODO compile this with stuff in Constants
    public static Comparator<Nutrition> SORT_BY_INC_CALS = new Comparator<Nutrition>() {
        @Override
        public int compare(Nutrition one, Nutrition other) {
            double oneCalories = one.getCalories();
            double otherCalories = other.getCalories();
            return Double.compare(oneCalories, otherCalories);
        }
    };

    public static Comparator<Nutrition> SORT_BY_DEC_PROTEIN = new Comparator<Nutrition>() {
        @Override
        public int compare(Nutrition one, Nutrition other) {
            double oneProtein = one.getProtein();
            double otherProtein = other.getProtein();
            return Double.compare(otherProtein, oneProtein);
        }
    };

    public static Comparator<Nutrition> SORT_BY_INC_CARBS = new Comparator<Nutrition>() {
        @Override
        public int compare(Nutrition one, Nutrition other) {
            double oneCarbs = one.getCarbohydrates();
            double otherCarbs = other.getCarbohydrates();
            return Double.compare(oneCarbs, otherCarbs);
        }
    };

    public static Comparator<Nutrition> SORT_BY_INC_FAT = new Comparator<Nutrition>() {
        @Override
        public int compare(Nutrition one, Nutrition other) {
            double oneFat = one.getFat();
            double otherFat = other.getFat();
            return Double.compare(oneFat, otherFat);
        }
    };

}
