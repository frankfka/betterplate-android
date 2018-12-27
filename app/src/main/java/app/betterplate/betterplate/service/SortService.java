package app.betterplate.betterplate.service;

import android.util.Log;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import app.betterplate.betterplate.data.core.Food;
import app.betterplate.betterplate.data.core.Restaurant;


public class SortService {

    public static int SORT_BY_INC_CALORIES = 1;
    public static int SORT_BY_DEC_PROTEIN = -2;
    public static int SORT_BY_INC_CARBS = 3;
    public static int SORT_BY_INC_FAT = 4;
    public static int SORT_BY_DEC_HEALTH = -5;

    public static List<Food> sortFoods(List<Food> foods, int sortByType) {
        List<Food> listToSort = new ArrayList<>(foods);
        if(sortByType == SORT_BY_INC_CALORIES) {
            Collections.sort(listToSort, Food.SORT_BY_INC_CALS);
        } else if (sortByType == SORT_BY_DEC_PROTEIN) {
            Collections.sort(listToSort, Food.SORT_BY_DEC_PROTEIN);
        } else if (sortByType == SORT_BY_INC_CARBS) {
            Collections.sort(listToSort, Food.SORT_BY_INC_CARBS);
        } else if (sortByType == SORT_BY_INC_FAT) {
            Collections.sort(listToSort, Food.SORT_BY_INC_FAT);
        } else if (sortByType == SORT_BY_DEC_HEALTH) {
            //TODO refactor this somehow?
            if(listToSort.get(0).getNutritionalInfo().getHealthScore() == 0) {
                for(Food food: listToSort) {
                    food.getNutritionalInfo().setHealthScore(HealthService.getHealthScorePublished(food.getNutritionalInfo()));
                }
            }
            Collections.sort(listToSort, Food.SORT_BY_DEC_HEALTH);
        } else {
            Collections.sort(listToSort, Food.DEFAULT_SORT);
        }
        return listToSort;
    }

    public static List<Restaurant> sortRestaurantByAlphabeticalOrder(List<Restaurant> restaurants) {
        if(restaurants == null || restaurants.isEmpty()) {
            return new ArrayList<>();
        }
        List<Restaurant> listToSort = new ArrayList<>(restaurants);
        Collections.sort(listToSort, Restaurant.SORT_BY_ALPHABETICAL_ORDER);
        return listToSort;
    }

}
