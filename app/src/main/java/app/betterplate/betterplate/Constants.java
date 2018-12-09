package app.betterplate.betterplate;

import java.util.Comparator;

import app.betterplate.betterplate.data.core.Food;

public class Constants {
    public static String RESTAURANT_HEADER_IMAGE_FOLDER = "restaurant_header/";

    /**
     * Comparators
     */
    public static Comparator<Food> CALORIE_COMPARATOR = new Comparator<Food>() {
        @Override
        public int compare(Food o1, Food o2) {
            double calories1 = o1.getNutritionalInfo().getCalories();
            double calories2 = o2.getNutritionalInfo().getCalories();
            if(calories1 > calories2) {
                return 1;
            } else if (calories2 > calories1) {
                return -1;
            } else {
                return 0;
            }
        }
    };

    public static Comparator<Food> PROTEIN_COMPARATOR = new Comparator<Food>() {
        @Override
        public int compare(Food o1, Food o2) {
            double protein1 = o1.getNutritionalInfo().getProtein();
            double protein2 = o2.getNutritionalInfo().getProtein();
            if(protein1 > protein2) {
                return 1;
            } else if (protein2 > protein1) {
                return -1;
            } else {
                return 0;
            }
        }
    };

    public static Comparator<Food> FAT_COMPARATOR = new Comparator<Food>() {
        @Override
        public int compare(Food o1, Food o2) {
            double fat1 = o1.getNutritionalInfo().getFat();
            double fat2 = o2.getNutritionalInfo().getFat();
            if(fat1 > fat2) {
                return 1;
            } else if (fat2 > fat1) {
                return -1;
            } else {
                return 0;
            }
        }
    };

    public static Comparator<Food> CARBS_COMPARATOR = new Comparator<Food>() {
        @Override
        public int compare(Food o1, Food o2) {
            double carbs1 = o1.getNutritionalInfo().getCarbohydrates();
            double carbs2 = o2.getNutritionalInfo().getCarbohydrates();
            if(carbs1 > carbs2) {
                return 1;
            } else if (carbs2 > carbs1) {
                return -1;
            } else {
                return 0;
            }
        }
    };

}
