package mycompany.app;


import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;

public class CustomJsonObject {

    public static class MealCollection {
        public CustomJsonObject.MealList[] meals;
    }

    public static class MealList {
        public List<CustomJsonObject.Meal> mealList;
    }

    public static class Meal {
        private String mealName;
        private String[] mealIngredients;

        public Meal(String mealName, String[] mealIngredients) {
            super();
            this.mealName = mealName;
            this.mealIngredients = mealIngredients;
        }

        public String getMealName() {
            return mealName;
        }

        public void setMealName(String mealName) {
            this.mealName = mealName;
        }

        public String[] getMealIngredients() {
            return mealIngredients;
        }

        public void setMealIngredients(String[] mealIngredients) {
            this.mealIngredients = mealIngredients;
        }
    }


    public static void main (String [] args) throws FileNotFoundException, IOException {

        List < CustomJsonObject.Meal > meals = new ArrayList <> ();

        String[] spaghettiIngredients = {"pasta", "tomato sauce", "salt"};
        CustomJsonObject.Meal meal1 = new CustomJsonObject.Meal("Spaghetti", spaghettiIngredients);
        String[] gcIngredients = {"sliced bread", "colby-jack cheese", "cheddar", "butter"};
        CustomJsonObject.Meal meal2 = new CustomJsonObject.Meal("Grilled Cheese", gcIngredients);

        meals.add(meal1);
        meals.add(meal2);

        Gson gson = new Gson();
        String json = gson.toJson(meals);
        json = json.replace("{", "{\n");
        json = json.replace("}", "\n}");
        System.out.println(json);
    }
}
