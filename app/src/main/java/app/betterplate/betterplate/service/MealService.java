package app.betterplate.betterplate.service;

import android.content.Context;
import android.os.AsyncTask;

import java.util.List;
import java.util.concurrent.ExecutionException;

import app.betterplate.betterplate.dao.core.MealDao;
import app.betterplate.betterplate.data.core.Food;
import app.betterplate.betterplate.data.database.MainDatabase;
import app.betterplate.betterplate.data.core.MealItem;
import app.betterplate.betterplate.data.core.Nutrition;


public class MealService {

    MainDatabase mainDatabase;

    public MealService(Context context) {
        this.mainDatabase = MainDatabase.getInstance(context);
    }

    public Nutrition getMealNutrition(List<Food> foodsInMeal) {
        Nutrition nutrition = new Nutrition(0, 0, 0, 0, 0, 0, 0, 0, 0,0, 0, 0, 0, 0);
        if(foodsInMeal == null || foodsInMeal.isEmpty()) {
            return nutrition;
        } else {
            for(Food food : foodsInMeal) {
                Nutrition nutritionForItem = food.getNutritionalInfo();
                nutrition.setCalories(nutrition.getCalories() + nutritionForItem.getCalories());
                nutrition.setCarbohydrates(nutrition.getCarbohydrates() + nutritionForItem.getCarbohydrates());
                nutrition.setFat(nutrition.getFat() + nutritionForItem.getFat());
                nutrition.setProtein(nutrition.getProtein() + nutritionForItem.getProtein());
                nutrition.setCalcium(nutrition.getCalcium() + nutritionForItem.getCalcium());
                nutrition.setCholesterol(nutrition.getCholesterol() + nutritionForItem.getCholesterol());
                nutrition.setFiber(nutrition.getFiber() + nutritionForItem.getFiber());
                nutrition.setIron(nutrition.getIron() + nutritionForItem.getIron());
                nutrition.setSaturatedFat(nutrition.getSaturatedFat() + nutritionForItem.getSaturatedFat());
                nutrition.setSodium(nutrition.getSodium() + nutritionForItem.getSodium());
                nutrition.setSugar(nutrition.getSugar() + nutritionForItem.getSugar());
                nutrition.setTransFat(nutrition.getTransFat() + nutritionForItem.getTransFat());
                nutrition.setVitaminA(nutrition.getVitaminA() + nutritionForItem.getVitaminA());
                nutrition.setVitaminC(nutrition.getVitaminC() + nutritionForItem.getVitaminC());
            }
            return nutrition;
        }
    }

    public List<Food> getCurrentMeal() throws ExecutionException, InterruptedException {
        class getCurrentMealTask extends AsyncTask<Void,Void,List<Food>>
        {
            @Override
            protected List<Food> doInBackground(Void... url) {
                return mainDatabase.mealDao().getCurrentMealFoods();
            }
        }
        return new getCurrentMealTask().execute().get();
    }

    public Void insertFoodToMeal(final Food food) throws ExecutionException, InterruptedException {
        class insertFoodTask extends AsyncTask<Food,Void,Void>
        {
            @Override
            protected Void doInBackground(Food... foods) {
                MealItem mealItem = new MealItem();
                mealItem.setFoodId(foods[0].getId());
                mainDatabase.mealDao().addToMeal(mealItem);
                return null;
            }
        }
        return new insertFoodTask().execute(food).get();
    }

    public Void removeFoodFromMeal(final Food food) throws ExecutionException, InterruptedException {
        class removeFoodTask extends AsyncTask<Food,Void,Void>
        {
            @Override
            protected Void doInBackground(Food... foods) {
                MealDao mealDao = mainDatabase.mealDao();
                List<MealItem> mealItems = mealDao.getMealItemsFromFoodId(foods[0].getId());
                mainDatabase.mealDao().deleteFromMeal(mealItems.get(0));
                return null;
            }
        }
        return new removeFoodTask().execute(food).get();
    }

    public Void removeAllFoodsFromMeal() throws ExecutionException, InterruptedException {
        class removeAllFoodsFromMealTask extends AsyncTask<Void,Void,Void>
        {
            @Override
            protected Void doInBackground(Void... voids) {
                mainDatabase.mealDao().deleteAllFoodsFromMeal();
                return null;
            }
        }
        return new removeAllFoodsFromMealTask().execute().get();
    }

}
