package app.betterplate.betterplate.service;


import android.util.SparseArray;
import android.util.SparseIntArray;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import app.betterplate.betterplate.data.core.Food;
import app.betterplate.betterplate.data.core.Nutrition;

public class FoodService {

    // This is used for retrieving macros
    public static Integer CARBS = 0;
    public static Integer PROTEIN = 1;
    public static Integer FAT = 2;


    public static SparseArray<Double> getMacrosInPercent(List<Food> foods) {

        SparseArray<Double> macros = new SparseArray<>();
        // Initialize so we never get a null pointer
        macros.append(CARBS, 0.0);
        macros.append(FAT, 0.0);
        macros.append(PROTEIN, 0.0);

        double totalCals = 0;
        double totalCarbs = 0;
        double totalProtein = 0;
        double totalFat = 0;

        for (Food food: foods) {
            Nutrition foodNutrition = food.getNutritionalInfo();
            double calsFromCarbs = foodNutrition.getCarbohydrates() * 4;
            double calsFromProtein = foodNutrition.getProtein() * 4;
            double calsFromFat = foodNutrition.getFat() * 9;

            totalCals += calsFromCarbs + calsFromFat + calsFromProtein;
            totalCarbs += calsFromCarbs;
            totalFat += calsFromFat;
            totalProtein += calsFromProtein;
        }

        // Set new values
        macros.setValueAt(CARBS, totalCarbs/totalCals);
        macros.setValueAt(FAT, totalFat/totalCals);
        macros.setValueAt(PROTEIN, totalProtein/totalCals);
        return macros;
    }

    public static double getHealthScorePublished(Nutrition nutrition) {
        return 0.710 - 0.0538*nutrition.getFat() - 0.423*nutrition.getSaturatedFat()
        - 0.00398*nutrition.getCholesterol() - 0.00254*nutrition.getSodium() - 0.0300*nutrition.getCarbohydrates()
        + 0.561*nutrition.getFiber() - 0.0245*nutrition.getSugar() + 0.123*nutrition.getProtein()
        + 0.0234*nutrition.getVitaminA() + 0.00399*nutrition.getVitaminC() + 0.0137*nutrition.getCalcium() - 0.0186*nutrition.getIron();
    }

    // Standardize this somehow?
    public static String getHealthWarnings(Nutrition nutrition) {
        String warningString = "";
        if (nutrition.getCalories() > 1100) {
            warningString = warningString.concat("High in Calories\n");
        }
        if (nutrition.getSodium() > 1200) {
            warningString = warningString.concat("High in Sodium\n");
        }
        if (nutrition.getSugar() > 20) {
            warningString = warningString.concat("High in Sugar\n");
        }
        if (nutrition.getTransFat() > 1) {
            warningString = warningString.concat("High in Trans Fats\n");
        }

        return warningString.trim();
    }


}
