package app.betterplate.betterplate.service;

import android.util.Log;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import app.betterplate.betterplate.data.core.Food;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FoodFinderService {

    private static int GREATER_THAN = 0;
    private static int LESS_THAN = 1;

    // Food preferences
    private double minCalories;
    private double maxCalories;
    private double minCarbs;
    private double maxCarbs;
    private double minProtein;
    private double maxProtein;
    private double minFat;
    private double maxFat;
    private double maxSodium;
    private double maxSugar;

    // Dietary restrictions
    private boolean isGlutenFree;
    private boolean isVegan;
    private boolean isVegetarian;

    public List<Food> findFoods(List<Food> inputFoods) {
        List<Food> listToReturn = new ArrayList<>(inputFoods);

        /**
         * Special dietary restrictions
         */
        if(isGlutenFree) {
            for (Iterator<Food> foodIterator = listToReturn.iterator(); foodIterator.hasNext();) {
                Food food = foodIterator.next();
                if (food.getIsGF() == 0) {
                    foodIterator.remove();
                }
            }
        }
        if(isVegan) {
            for (Iterator<Food> foodIterator = listToReturn.iterator(); foodIterator.hasNext();) {
                Food food = foodIterator.next();
                if (food.getIsVegan() == 0) {
                    foodIterator.remove();
                }
            }
        }
        if(isVegetarian) {
            for (Iterator<Food> foodIterator = listToReturn.iterator(); foodIterator.hasNext();) {
                Food food = foodIterator.next();
                if (food.getIsVegetarian() == 0) {
                    foodIterator.remove();
                }
            }
        }

        /**
         * Calories
         */
        if (minCalories != 0) {
            for (Iterator<Food> foodIterator = listToReturn.iterator(); foodIterator.hasNext();) {
                Food food = foodIterator.next();
                if (food.getNutritionalInfo().getCalories() < minCalories) {
                    foodIterator.remove();
                }
            }
        }
        if (maxCalories != 0) {
            for (Iterator<Food> foodIterator = listToReturn.iterator(); foodIterator.hasNext();) {
                Food food = foodIterator.next();
                if (food.getNutritionalInfo().getCalories() > maxCalories) {
                    foodIterator.remove();
                }
            }
        }
        if (listToReturn.isEmpty()) {
            return listToReturn;
        }

        /**
         * Carbs
         */
        if (minCarbs != 0) {
            for (Iterator<Food> foodIterator = listToReturn.iterator(); foodIterator.hasNext();) {
                Food food = foodIterator.next();
                if (food.getNutritionalInfo().getCarbohydrates() < minCarbs) {
                    foodIterator.remove();
                }
            }
        }
        if (maxCarbs != 0) {
            for (Iterator<Food> foodIterator = listToReturn.iterator(); foodIterator.hasNext();) {
                Food food = foodIterator.next();
                if (food.getNutritionalInfo().getCarbohydrates() > maxCarbs) {
                    foodIterator.remove();
                }
            }
        }
        if (listToReturn.isEmpty()) {
            return listToReturn;
        }

        /**
         * Protein
         */
        if (minProtein != 0) {
            for (Iterator<Food> foodIterator = listToReturn.iterator(); foodIterator.hasNext();) {
                Food food = foodIterator.next();
                if (food.getNutritionalInfo().getProtein() < minProtein) {
                    foodIterator.remove();
                }
            }
        }
        if (maxProtein != 0) {
            for (Iterator<Food> foodIterator = listToReturn.iterator(); foodIterator.hasNext();) {
                Food food = foodIterator.next();
                if (food.getNutritionalInfo().getProtein() > maxProtein) {
                    foodIterator.remove();
                }
            }
        }
        if (listToReturn.isEmpty()) {
            return listToReturn;
        }

        /**
         * Fat
         */
        if (minFat != 0) {
            for (Iterator<Food> foodIterator = listToReturn.iterator(); foodIterator.hasNext();) {
                Food food = foodIterator.next();
                if (food.getNutritionalInfo().getFat() < minFat) {
                    foodIterator.remove();
                }
            }
        }
        if (maxFat != 0) {
            for (Iterator<Food> foodIterator = listToReturn.iterator(); foodIterator.hasNext();) {
                Food food = foodIterator.next();
                if (food.getNutritionalInfo().getFat() > maxFat) {
                    foodIterator.remove();
                }
            }
        }
        if (listToReturn.isEmpty()) {
            return listToReturn;
        }

        return listToReturn;
    }
}
