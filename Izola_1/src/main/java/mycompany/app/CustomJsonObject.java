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

        public Meal(String mealName,
                    String[] mealIngredients,
                    String[] mealMeasurements,
                    String mealDescription,
                    String mealInstructions,
                    String mealCategory,
                    String mealArea) {
            super();
            this.mealName = mealName;
            this.mealIngredients = mealIngredients;
            this.mealMeasurements = mealMeasurements;
            this.mealDescription = mealDescription;
            this.mealInstructions = mealInstructions;
            this.mealCategory = mealCategory;
            this.mealArea = mealArea;
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
        return;
    }
}
