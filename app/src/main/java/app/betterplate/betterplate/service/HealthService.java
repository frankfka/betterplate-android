package app.betterplate.betterplate.service;


import app.betterplate.betterplate.data.core.Nutrition;

public class HealthService {

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
//
//    public static double getHealthScoreNew(Nutrition nutrition) {
//        double sodium = nutrition.getSodium();
//        double cholesterol = nutrition.getCholesterol();
//        double fat = nutrition.getFat();
//        double satFat = nutrition.getSaturatedFat();
//        double transFat = nutrition.getTransFat();
//        double carbs = nutrition.getCarbohydrates();
//        double fiber = nutrition.getFiber();
//        double sugar = nutrition.getSugar();
//        double protein = nutrition.getProtein();
//        double calories = nutrition.getCalories();
//
//        double sugarToCarbs = sugar/carbs;
//        double fiberToCarbs = fiber/carbs;
//        double satToTotFat = satFat/fat;
//        double proteinRatio = protein*4/calories;
//        double carbRatio = carbs*4/calories;
//
//        NormalDistribution idealMacroRatio = new NormalDistribution(0.3, 0.05);
//        double sodiumContribution = sodium * -0.001;
//        Log.e("sodium", String.valueOf(sodiumContribution));
//        double sugarToCarbsContribution = sugarToCarbs * -1;
//        Log.e("sugar to carbs", String.valueOf(sugarToCarbsContribution));
//        double fiberToCarbsContribution = fiberToCarbs * 1;
//        Log.e("fiberToCarbsContribution", String.valueOf(fiberToCarbsContribution));
//        double cholesterolContribution = cholesterol * -0.01;
//        Log.e("cholesterolContribution", String.valueOf(cholesterolContribution));
//        double satFatToTotFatContribution = satToTotFat * -1;
//        Log.e("satFatToTotFatContribution", String.valueOf(satFatToTotFatContribution));
//        double transFatContribution = transFat * -10;
//        Log.e("transFatContribution", String.valueOf(transFatContribution));
//        double proteinMacroContribution = idealMacroRatio.density(proteinRatio);
//        Log.e("proteinMacroContribution", String.valueOf(proteinMacroContribution));
//        double carbMacroContribution = idealMacroRatio.density(carbRatio);
//        Log.e("carbMacroContribution", String.valueOf(carbMacroContribution));
//
//        return sodiumContribution + sugarToCarbsContribution + fiberToCarbsContribution + cholesterolContribution + satFatToTotFatContribution + transFatContribution + proteinMacroContribution + carbMacroContribution;
//    }

}
