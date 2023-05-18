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
        private String[] mealMeasurements;
        private String mealDescription;
        private String mealInstructions;
        private String mealCategory;
        private String mealArea;

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
        public String[] getMealMeasurements() {
            return mealMeasurements;
        }
        public void setMealMeasurements(String[] mealMeasurements) {
            this.mealMeasurements = mealMeasurements;
        }
        public String getMealDescription() {
            return mealDescription;
        }
        public void setMealDescription(String mealDescription) {
            this.mealDescription = mealDescription;
        }
        public String getMealInstructions() {
            return mealInstructions;
        }
        public void setMealInstructions(String mealInstructions) {
            this.mealInstructions = mealInstructions;
        }
        public String getMealCategory() {
            return mealCategory;
        }
        public void setMealCategory(String mealCategory) {
            this.mealCategory = mealCategory;
        }
        public String getMealArea() {
            return mealArea;
        }
        public void setMealArea(String mealArea) {
            this.mealArea = mealArea;
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
