package app.betterplate.betterplate.service;

import java.util.Locale;

import app.betterplate.betterplate.data.core.Food;
import app.betterplate.betterplate.data.core.FoodComponent;
import app.betterplate.betterplate.data.core.Nutrition;
import app.betterplate.betterplate.data.core.Restaurant;

public class StringFormatterService {

    public static String getFoodComponentDescription(FoodComponent foodComponent) {
        // Formatted string with calories & the most significant macro
        Nutrition nutrition = foodComponent.getNutrition();
        double calories = nutrition.getCalories();
        double carbs = nutrition.getCarbohydrates();
        double protein = nutrition.getProtein();
        double fat = nutrition.getFat();

        if (fat > protein && fat > carbs) {
            return String.format(Locale.CANADA, "%.0f Cals | %.0fg Fat", calories, fat);
        } else if (protein > fat && protein > carbs) {
            return String.format(Locale.CANADA, "%.0f Cals | %.0fg Protein", calories, protein);
        } else {
            return String.format(Locale.CANADA, "%.0f Cals | %.0fg Carbs", calories, carbs);
        }
    }

//    public static String getRestaurantDescription(Restaurant restaurant) {
//        double priceRating = restaurant.getPriceRating();
//        double minCalories = restaurant.getMinCalories();
//        double maxCalories = restaurant.getMaxCalories();
//        String formattedString = "";
//        for (int i = 0; i < priceRating; i++) {
//            formattedString = formattedString.concat("$");
//        }
//        return formattedString.concat(String.format(Locale.CANADA, " | %.0f-%.0f Cals", minCalories, maxCalories));
//    }

    public static String getFoodMenuDescription(Food food) {
        Nutrition nutritionalInfo = food.getNutritionalInfo();
        return String.format(Locale.CANADA, "%.0f Cal | C: %.0fg  F: %.0fg  P: %.0fg",
                nutritionalInfo.getCalories(), nutritionalInfo.getCarbohydrates(), nutritionalInfo.getFat(), nutritionalInfo.getProtein());
    }

    public static String getFoodSubtitleDescription(Food food) {
        Nutrition nutritionalInfo = food.getNutritionalInfo();
        return String.format(Locale.CANADA, "%.0f Cal | %.0fg Carbs | %.0fg Fats | %.0fg Protein",
                nutritionalInfo.getCalories(), nutritionalInfo.getCarbohydrates(), nutritionalInfo.getFat(), nutritionalInfo.getProtein());
    }

    public static String getMealSubtitleDescription(Nutrition nutritionalInfo) {
        return String.format(Locale.CANADA, "%.0f Cal | %.0fg Carbs | %.0fg Fats | %.0fg Protein",
                nutritionalInfo.getCalories(), nutritionalInfo.getCarbohydrates(), nutritionalInfo.getFat(), nutritionalInfo.getProtein());
    }

    public static String getSimpleFoodListDescription(Food food) {
        return String.valueOf(food.getNutritionalInfo().getCalories()).concat(" Cal");
    }

    public static String getFoodServingSize(Food food) {
        String statedServing = food.getServingSize();
        if(statedServing == null || statedServing.trim().isEmpty()) {
            return "";
        } else {
            return "Serving Size: " + statedServing.trim().toLowerCase();
        }
    }

}
