package mycompany.app;

import java.io.IOException;
import java.io.File;
import java.io.FileWriter;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse.BodyHandlers;
import java.net.http.HttpResponse;
import java.net.http.HttpClient;
import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;
import com.google.gson.Gson;

public class AppUtils extends App {

    String value;
    List<String> rawIngredients = new ArrayList<>();
    List<String> rawMeasurements = new ArrayList<>();
    private static final Gson gson = new Gson();
    Scanner scanner = new Scanner(System.in);
    HashSet<String> knownIngredientsSet = new HashSet<>();
    List<String> pantryItemList = new ArrayList<>();
    HashSet<CustomJsonObject.Meal> knownMealSet = new HashSet<>();
    List<CustomJsonObject.Meal> knownMealList = new ArrayList<>();
    List<String> measurements = new ArrayList<>();
    List<String> ingredients = new ArrayList<>();
    String pantryFilePath = "src/main/resources/archive/pantry/stock.json";
    List<ResponseObject.MealDBMeal> meals = new ArrayList<>();
    String[] ingredientArray;
    String[] measurementArray;
    HashSet<String> matchingIngredients = new HashSet<>();
    String mealsFilePath = "src/main/resources/archive/meals/meals.json";
    String knownIngredientsFilePath = "src/main/resources/archive/meals/info/listOfKnownIngredients.json";
    HashSet<CustomJsonObject.Meal> possibleMealsSet = new HashSet<>();
    File pantryFile = new File(pantryFilePath);
    CustomJsonObject.Ingredient pantry =
            gson.fromJson(getFileContent(pantryFile), CustomJsonObject.Ingredient.class);
    File mealsFile = new File(mealsFilePath);
    File knownIngredientFile = new File(knownIngredientsFilePath);
    HashSet<String> similarIngredientsPantry = new HashSet<>();
    static String ENDPOINT_MEAL = "https://www.themealdb.com/api/json/v1/1";
    public HttpClient HTTP_CLIENT = HttpClient.newBuilder()
        .version(HttpClient.Version.HTTP_2)
        .followRedirects(HttpClient.Redirect.NORMAL)
        .build();

    public String fetchString(String uri) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder().uri(URI.create(uri)).build();
        HttpResponse<String> response = HTTP_CLIENT.send(request, BodyHandlers.ofString());
        final int statusCode = response.statusCode();
        if (statusCode != 200) throw new IOException("response status code not 200:" + statusCode);
        return response.body().trim();
    } // fetchString

    public Optional<ResponseObject.MealDBResult> searchMealDB(String q, String option_type) {
        try {
            String url = String.format("%s/%s.php?%s=%s", ENDPOINT_MEAL,
                                       option_type,"s",
                                       URLEncoder.encode(q, StandardCharsets.UTF_8));
            String json = fetchString(url);
            ResponseObject.MealDBResult result_meal =
                gson.fromJson(json, ResponseObject.MealDBResult.class);
            System.out.println("[ url ] " + url + "\n\n");
            return Optional.<ResponseObject.MealDBResult>ofNullable(result_meal);
        } catch (IllegalArgumentException | IOException | InterruptedException e) {
            System.out.println("Something went wrong with the mealDB api. " +
                               "Please try again or try a different search.");
            return Optional.<ResponseObject.MealDBResult>empty();
        }
    }

    public void processMealDBResult(ResponseObject.MealDBResult result) {
        if (result.meals != null) {
            meals.clear();
            meals.addAll(Arrays.asList(result.meals));
            System.out.printf("There are %s meals available.\n", meals.size());
            meals.forEach(this::processMeal);
            System.out.println("There is a possible "
                           + knownIngredientsSet.size()
                           + " unique ingredients to choose from.");
            writeToFile(mealsFilePath, gson.toJson(this.knownMealSet), "meals");
            writeToFile(knownIngredientsFilePath, gson.toJson(this.knownIngredientsSet), "ingredients");
        } else {
            System.out.println("Hm. I don't have anything for that. \n -> Perhaps try again?");
        }
    }

    private void processMeal(ResponseObject.MealDBMeal meal) {
        rawIngredients.add(meal.strIngredient1);
        rawIngredients.add(meal.strIngredient2);
        rawIngredients.add(meal.strIngredient3);
        rawIngredients.add(meal.strIngredient4);
        rawIngredients.add(meal.strIngredient5);
        rawIngredients.add(meal.strIngredient6);
        rawIngredients.add(meal.strIngredient7);
        rawIngredients.add(meal.strIngredient8);
        rawIngredients.add(meal.strIngredient9);
        rawIngredients.add(meal.strIngredient10);
        rawIngredients.add(meal.strIngredient11);
        rawIngredients.add(meal.strIngredient12);
        rawIngredients.add(meal.strIngredient13);
        rawIngredients.add(meal.strIngredient14);
        rawIngredients.add(meal.strIngredient15);
        rawIngredients.forEach(ing -> {
                if (ing != null && ing.length() > 0) {
                    ingredients.add(ing);
                    knownIngredientsSet.add(ing.toLowerCase());
                }
            });
        ingredientArray = new String[ingredients.size()];
        for (int i = 0; i < ingredients.size() ; i++) ingredientArray[i] = ingredients.get(i);
        rawMeasurements.add(meal.strMeasure1);
        rawMeasurements.add(meal.strMeasure2);
        rawMeasurements.add(meal.strMeasure3);
        rawMeasurements.add(meal.strMeasure4);
        rawMeasurements.add(meal.strMeasure5);
        rawMeasurements.add(meal.strMeasure6);
        rawMeasurements.add(meal.strMeasure7);
        rawMeasurements.add(meal.strMeasure8);
        rawMeasurements.add(meal.strMeasure9);
        rawMeasurements.add(meal.strMeasure10);
        rawMeasurements.add(meal.strMeasure11);
        rawMeasurements.add(meal.strMeasure12);
        rawMeasurements.add(meal.strMeasure13);
        rawMeasurements.add(meal.strMeasure14);
        rawMeasurements.add(meal.strMeasure15);
        rawMeasurements.forEach(mm -> {
                if (mm != null && mm.length() > 0) {
                    measurements.add(mm);
                }
            });
        measurementArray = new String[measurements.size()];
        for (int i = 0; i < measurements.size() ; i++) measurementArray[i] = measurements.get(i);
        CustomJsonObject.Meal customMeal =
            new CustomJsonObject.Meal(meal.strMeal,
                                      this.ingredientArray,
                                      this.measurementArray,
                                      meal.strDescription,
                                      meal.strInstructions,
                                      meal.strCategory,
                                      meal.strArea);
        this.knownMealSet.add(customMeal);
        this.rawIngredients.clear();
        this.ingredients.clear();
        this.rawMeasurements.clear();
        this.measurements.clear();
    }

    private void writeToFile(String path, String content, String type) {
        try {
            FileWriter myWriter = new FileWriter(path);
            if (!content.startsWith("{")) {
                myWriter.write(String.format("{\"%s\":%s}", type, content));
            } else {
                myWriter.write(content);
            }
            myWriter.close();
            System.out.printf("[ %s ] : populated.%n", path);
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }

    /**
     * getFileContent takes an input of type File.
     * Then, reads all the lines of the file and condenses the lines into a single
     * String variable.
     * @param file of type File
     * @return String
     **/
    private String getFileContent(File file) {
        List<String> lines;
        StringBuilder content = new StringBuilder();
        try {
            lines = Files.readAllLines(file.toPath(), StandardCharsets.UTF_8);
            for (String line : lines) {
                content.append(line);
            }
            return content.toString();
        } catch (IOException e) {
            return null;
        }
    }

    /**
     * userInterface permits the user to interact with the program to allow the custom modification
     * of inquiries formatted and shown in the CLI
     **/
    public void userInterface() {
        System.out.println("Welcome! How can I help you?");
        System.out.println("""
                [0] search
                [1] pantry
                [2] meals
                [3] exit""");
        this.value = scanner.nextLine();
        switch (value) {
            case "0" -> {
                System.out.println("You chose search.\n Which meal are you looking for?");
                String mealString = scanner.nextLine();
                System.out.printf("Searching for %s\n", mealString);
                fillKnownIngredientSet();
                fillKnownMealsSet();
                searchMealDB(mealString, "search")
                        .ifPresent(this::processMealDBResult);
                userInterface();
            }
            case "1" -> {
                handlePantry();
                userInterface();
            }
            case "2" -> {
                System.out.println("You chose to list known meals. ");
                handleMealDisplay();
            }
            case "3" -> {
                System.out.println("You chose to list known ingredients. ");
                fillKnownIngredientSet();
                knownIngredientsSet.forEach(System.out::println);
                userInterface();
            }
            default -> {
                return;
            }
        }
    }

    /**
     *  {@code handlePantry()} Takes in no input and returns nothing.
     *  Takes the next input from the user using a scanner object.
     *  Then, handles the response from the user; guided by text prompt.
     *  */
    public void handlePantry() {
        System.out.println("Welcome to your pantry.");
            System.out.println("""
                    Would you like to [ ] ?
                    [ 0 ] update pantry\s
                    [ 1 ] show pantry contents\s
                    [ 2 ] see what you can make\s
                    [ 3 ] return""");
        this.value = scanner.nextLine();
        switch (value) {
            case "0" -> {
                System.out.println("Choice: Update Pantry");
                handleUpdatePantry();
            }
            case "1" -> {
                System.out.println("Choice: Show Pantry Contents");
                this.printPantryContents();
            }
            case "2" -> {
                System.out.println("Choice: see what you can make");
                possibleMeals();
            }
            default -> {
                System.out.println("Choice: return");
                userInterface();
            }
        }
    }

    public void handleUpdatePantry() {
        System.out.println("""
                Would you like to [ ] ?
                [ 0 ] remove (an) item(s)
                [ 1 ] add (an) item(s)
                [ 2 ] go back""");
        this.value = scanner.nextLine();
        switch (value) {
            case "0" -> {
                System.out.println("You chose to remove an item.");
                handlePantryItemRemoval();
            }
            case "1" -> {
                System.out.println("""
                        You chose to add (an) item(s).\s
                        Type your item(s) (*type "exit" when finished adding items*)""");
                fillCurrentPantryList();
                addToPantry();
            }
            case "2" -> handlePantry();
        }
    }

    public CustomJsonObject.Ingredient findPantryFile() {
        File currentPantryFile = new File(pantryFilePath);
        CustomJsonObject.Ingredient currentPantry =
            gson.fromJson(this.getFileContent(currentPantryFile), CustomJsonObject.Ingredient.class);
        return currentPantry;
    }

    public void fillCurrentPantryList() {
        File currentPantryFile = new File(pantryFilePath);
        this.pantryItemList.clear();
        CustomJsonObject.Ingredient currentPantry =
                gson.fromJson(this.getFileContent(currentPantryFile), CustomJsonObject.Ingredient.class);
        this.pantryItemList.addAll(Arrays.asList(currentPantry.getIngredients()));
    }

    public void printPantryContents() {
        CustomJsonObject.Ingredient pantry = this.findPantryFile();
        Arrays.asList(pantry.getIngredients()).forEach(System.out::println);
    }

    public void addToPantry() {
        this.value = this.scanner.nextLine();
        if (this.value.equals("exit")) {
            System.out.println("""
                    Would you like to see your inventory?
                    [ 0 ] yes
                    [ 1 ] no""");
            this.value = scanner.nextLine();
            if (this.value.equals("0")) {
                this.pantryItemList.forEach(System.out::println);
                writeToFile(pantryFilePath, gson.toJson(this.pantryItemList), "ingredients");
            } else {
                writeToFile(pantryFilePath, gson.toJson(this.pantryItemList), "ingredients");
            }
        } else if (checkIngredient(value)) {
            this.pantryItemList.add(value);
            addToPantry();
        } else if (!checkIngredient(value)) {
            addToPantry();
        }
    }

    /**
     * {@code printKnownMeals} takes no input and returns nothing.
     * Creates an {@code CustomJsonObject.MealItems object} and populates it with a
     * gson-processed collection of objects.
     * Then, the {@code meal.getName()} method is called on each meal object to print
     * out a list of all the meal names in a local json file.
     *  */
    public void printKnownMeals() {
        CustomJsonObject.MealItems meals =
            gson.fromJson(getFileContent(mealsFile), CustomJsonObject.MealItems.class);
        Arrays.asList(meals.meals).forEach(meal -> {
                System.out.println(meal.getMealName());
            });
    }

    /**
     * {@code fillKnownMealsSet} takes no input and returns nothing.
     * Creates an {@code CustomJsonObject.MealItems object} and populates it with a
     * gson-processed collection of objects.
     * Then, the MealItems objects are sent to a {@code knownMealHashSet} Hash Set.
     *  */
    public void fillKnownMealsSet() {
        CustomJsonObject.MealItems meals =
            gson.fromJson(getFileContent(mealsFile), CustomJsonObject.MealItems.class);
        this.knownMealSet.addAll(Arrays.asList(meals.meals));
        this.knownMealList.addAll(Arrays.asList(meals.meals));
    }

    public void fillKnownIngredientSet() {
        CustomJsonObject.Ingredient knownIngs =
            gson.fromJson(getFileContent(knownIngredientFile), CustomJsonObject.Ingredient.class);
        this.knownIngredientsSet.addAll(Arrays.asList(knownIngs.getIngredients()));
    }

    public boolean checkIngredient(String ingredient) {
        if (!this.knownIngredientsSet.contains(ingredient)) {
            System.out.println("""
                    Hm. I don't know that one.\s
                    Would you [ ]?
                    [ 0 ] like a suggestion based on the ingredients I know
                    [ 1 ] omit this prompt
                    [ 2 ] add anyway
                    [ 3 ] try again
                    *Note: If I'm not able to recognize which meal you are providing,
                        there is a possibility that I will not be able to provide
                        suggestions for you regarding that ingredient.""");
            this.value = scanner.nextLine();
            System.out.println("*Note: If I don't know of a certain ingredient, "
                               + "try searching for a meal with that ingredient. ");
            switch (value) {
                case "0" -> {
                    this.fillKnownIngredientSet();
                    System.out.println("Known ingredient set size: " + this.knownIngredientsSet.size());
                    this.knownIngredientsSet.forEach(elem -> {
                        if (elem.contains(ingredient.substring(0, 2))) {
                            System.out.println(elem);
                        }
                    });
                    return false;
                }
                case "1" -> {
                    System.out.println("Omitted.");
                    return false;
                }
                case "2" -> {
                    System.out.println("Added.");
                    return true;
                }
                case "3" -> {
                    this.value = scanner.nextLine();
                    checkIngredient(value);
                }
            }
        }
        return true;
    }

    public void possibleMeals() {
        fillKnownMealsSet();
        fillKnownIngredientSet();
        knownMealSet.forEach(meal -> {
                knownIngredientsSet.forEach(ing -> {
                        if (Arrays.asList(meal.getMealIngredients()).contains(ing)) {
                            possibleMealsSet.add(meal);
                        }
                    });
            });
        this.possibleMealsSet.forEach(meal -> {
                System.out.println(meal.getMealName()
                                   + "\n" + numberOfSimilarIngredients(meal.getMealIngredients())
                                   + " matching ingredients."
                                   + "\n");
            });
    }

    public int numberOfSimilarIngredients(String[] mealIngredients) {
        this.fillCurrentPantryList();
        Arrays.asList(mealIngredients).forEach(mealIngredient -> {
                pantryItemList.forEach(item -> {
                        if (mealIngredient.contains(item)) {
                            this.matchingIngredients.add(mealIngredient);
                        }
                    });
            });
        int matched = this.matchingIngredients.size();
        System.out.println("You need "
                           + (mealIngredients.length - matched)
                           + " more ingredients to make this meal");
        this.matchingIngredients.clear();
        return matched;
    }

    public void handleMealDisplay() {
        System.out.println("""
                [ 0 ] list known meals
                [ 1 ] go back""");
        this.value = scanner.nextLine();
        if (value.equals("0")) {
            fillKnownMealsSet();
            printKnownMeals();
            System.out.println("""
                    [ 0 ] select meal
                    [ 1 ] go back""");
            this.value = scanner.nextLine();
            if (value.equals("0")) {
                for (int i = 0; i < this.knownMealList.size(); i++ ) {
                    System.out.println("[ " + i + " ] " + this.knownMealList.get(i).getMealName());
                }
                handleMealSelection();
            }
        } else if (value.equals("1")) {
            userInterface();
        }
    }

    public void handlePantryItemRemoval() {
        System.out.println("Which item(s) would you like to remove?\ntype: \"exit\" when finished");
        this.value = scanner.nextLine();
        fillCurrentPantryList();
        if (this.value.equals("exit")) {
            handleUpdatePantry();
        } else if (!this.pantryItemList.contains(value)) {
            System.out.println("""
                    Hm. 
                    You don't seem to have that item in your pantry.
                    I can check for a similar item if you'd like.
                    [0] check for similar item
                    [1] return""");
            this.value = scanner.nextLine();
            if (value.equals("0")) {
                System.out.println("Show user a similar item to input");
                printSimilarItemPantry(this.value);
            } else {
                handlePantryItemRemoval();
            }
        } else {
            this.pantryItemList.remove(value);
            writeToFile(pantryFilePath, gson.toJson(this.pantryItemList), "ingredients");
            fillCurrentPantryList();
            System.out.println("Removed: " + value);
            handlePantryItemRemoval();
        }
    }

    public void printSimilarItemPantry(String userValue) {

        fillCurrentPantryList();
        this.pantryItemList.forEach(item -> {
            System.out.println(item);

            if (item.contains(userValue)) {
                 System.out.println(item);
                this.similarIngredientsPantry.add(item);
            }
        });
        if (this.similarIngredientsPantry.size() == 0) {
            System.out.println("hm. I don't seem to have any similar items.");
            handlePantryItemRemoval();
        } else {
            System.out.println("Here's a list of similar items currently in your pantry: ");
            this.similarIngredientsPantry.forEach(System.out::println);
            this.similarIngredientsPantry.clear();
        }
    }

    public void handleMealSelection() {
        System.out.println("Provide the index number of the meal you're interested in.");
        System.out.println("Type \"exit\" to go back");
        this.value = scanner.nextLine();
        if (!value.toLowerCase().equals("exit")) {
            if (Integer.parseInt(value) > 0 && Integer.parseInt(value) < this.knownMealList.size()) {
                System.out.println(" -- Meal -- "
                        + "\n   " + this.knownMealList.get(Integer.parseInt(value)).getMealName()
                        + "\n\n -- Instructions -- "
                        + "\n   " + this.knownMealList.get(Integer.parseInt(value)).getMealInstructions()
                        + "\n\n -- Ingredients -- "
                        + "\n   "
                        + Arrays.asList(this.knownMealList.get(Integer.parseInt(value)).getMealIngredients()));
                handleMealSelection();
            }
        } else {
            handleMealDisplay();
        }
    }

    public AppUtils() {
        super();
        this.userInterface();
    }
}
