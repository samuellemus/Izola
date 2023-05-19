package mycompany.app;

import java.io.IOException;
import java.io.File;
import java.io.FilenameFilter;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.io.Reader;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse.BodyHandlers;
import java.net.http.HttpResponse;
import java.net.http.HttpClient;
import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;
import java.util.stream.Collectors;
import com.google.gson.Gson;

public class AppUtils extends App {

    static String ENDPOINT_MEAL = "https://www.themealdb.com/api/json/v1/1";
    private String searchType;
    String searchItem;
    String json;
    private static final Gson gson = new Gson();
    String[] ingredientArray;
    String[] measurementArray;
    HashSet<String> knownIngredients = new HashSet<>();
    List<ResponseObject.MealDBMeal> meals = new ArrayList<>();
    List<CustomJsonObject.Meal> listOfMeals = new ArrayList<>();
    List<CustomJsonObject.Meal> processedMeals = new ArrayList<>();
    List<String> rawIngredients = new ArrayList<>();
    List<String> ingredients = new ArrayList<>();
    List<String> fileContents = new ArrayList<>();
    List<String> rawMeasurements = new ArrayList<>();
    List<String> measurements = new ArrayList<>();
    String pantryFilePath = "src/main/resources/archive/pantry/stock.json";
    String mealJsonUri = "src/main/resources/archive/meals/";

    public HttpClient HTTP_CLIENT = HttpClient.newBuilder()
        .version(HttpClient.Version.HTTP_2)
        .followRedirects(HttpClient.Redirect.NORMAL)
        .build();

    public String fetchString(String uri) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder().uri(URI.create(uri)).build();
        HttpResponse<String> response = HTTP_CLIENT.send(request, BodyHandlers.ofString());
        final int statusCode = response.statusCode();
        if (statusCode != 200) throw new IOException("reponse status code not 200:" + statusCode);
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
            for (ResponseObject.MealDBMeal meal : result.meals) meals.add(meal);
            System.out.printf("There are %s meals available.\n", meals.size());
            meals.stream().forEach(meal -> processMeal(meal));
            System.out.println("There is a possible "
                           + knownIngredients.size()
                           + " unique ingredients to choose from.");
            this.convertAndSave();
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
        rawIngredients.stream().forEach(ing -> {
                if (ing != null && ing.length() > 0) {
                    ingredients.add(ing);
                    knownIngredients.add(ing);
                }
            });
        ingredientArray = new String[ingredients.size()];
        for (int i = 0; i < ingredients.size() ; i++) ingredientArray[i] = ingredients.get(i);
        String mealName = meal.strMeal;
        String mealNameString = mealName.replace(" ", "_");
        mealNameString = mealNameString.replace("&", "and");
        mealNameString = mealNameString.replace("'", "");
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
        rawMeasurements.stream().forEach(mm -> {
                if (mm != null && mm.length() > 0) {
                    measurements.add(mm);
                }
            });
        measurementArray = new String[measurements.size()];
        for (int i = 0; i < measurements.size() ; i++) measurementArray[i] = measurements.get(i);
        String mealDescription = meal.strDescription;
        String mealInstructions = meal.strInstructions;
        String mealCategory = meal.strCategory;
        String mealArea = meal.strArea;
        addToCollection(mealNameString,
                        this.ingredientArray,
                        this.measurementArray,
                        mealDescription,
                        mealInstructions,
                        mealCategory,
                        mealArea);
        this.rawIngredients.clear();
        this.ingredients.clear();
        this.rawMeasurements.clear();
        this.measurements.clear();
    }

    private void addToCollection(String mealName,
                                 String[] ingredients,
                                 String[] measurements,
                                 String mealDescription,
                                 String mealInstructions,
                                 String mealCategory,
                                 String mealArea) {
        CustomJsonObject.Meal customMeal =
            new CustomJsonObject.Meal(mealName,
                                      ingredients,
                                      measurements,
                                      mealDescription,
                                      mealInstructions,
                                      mealCategory,
                                      mealArea);
        this.listOfMeals.add(customMeal);
    }

    private void convertAndSave() {
        listOfMeals.stream().forEach(meal -> makeFile(mealJsonUri, meal.getMealName(), this.gson.toJson(meal)));
    }

    private void makeFile(String path, String mealName, String content) {
        String filePath = String.format("%s%s.json", path, mealName);
        try {
            File file = new File(filePath);
            if (file.createNewFile()) {
                System.out.println("File created: " + file.getName());
                writeToFile(filePath, content);
            } else {
                System.out.println("File already exists.");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void writeToFile(String path, String content) {
        try {
            FileWriter myWriter = new FileWriter(path);
            if (!content.startsWith("{")) {
                myWriter.write("{" + "\"ingredients\":" + content + "}");
            } else {
                myWriter.write(content);
            }
            myWriter.close();
            System.out.println(String.format("[ %s ] : populated.", path));
        } catch (IOException e) {
            System.out.println("An error occured.");
            e.printStackTrace();
        }
    }

    File directoryPath;
    FilenameFilter jsonFilefilter;
    File fileList[];
    private void findMealFiles() {
        this.directoryPath = new File("src/main/resources/archive/meals");
        this.jsonFilefilter = new FilenameFilter() {
                public boolean accept(File dir, String name) {
                    String lowercaseName = name.toLowerCase();
                    if (lowercaseName.endsWith(".json")) {
                        return true;
                    } else {
                        return false;
                    }
                }};
        this.fileList = directoryPath.listFiles(jsonFilefilter);
        for(File file : fileList) {
            processedMeals.add(gson.fromJson(getFileContent(file), CustomJsonObject.Meal.class));
        }
    }

    private String[] compareIngredients() {
        // make pantry ingredients object
        // compare ingredients in pantry to ingredients in meals
        System.out.println("Not implemented");
        return null;
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
        String content = "";
        try {
            lines = Files.readAllLines(file.toPath(), StandardCharsets.UTF_8);
            for (String line : lines) {
                content += line;
            }
            return content;
        } catch (IOException e) {
            return null;
        }
    }

    /**
     * userInterface allows the user to interact with the program to allow to custom modification
     * of inquiries formatted and shown in the CLI
     **/
    public void userInterface() {
        // Create a Scanner object
        Scanner scanner = new Scanner(System.in);
        String json;
        System.out.println("Welcome! How can I help you?");
        System.out.println("[options] [search] [pantry]");
        String value = scanner.nextLine();
        if (value.toLowerCase().equals("search")) {
            System.out.println("You chose search.\n Which meal are you looking for?");
            String mealString = scanner.nextLine();
            System.out.printf("Searching for %s\n", mealString);
            searchMealDB(mealString, "search")
                .ifPresent(result -> processMealDBResult(result));
        }
        else if (value.toLowerCase().equals("options")) System.out.println("You chose options."
                                                                           + "\n this path is not coded yet");
        else if (value.toLowerCase().equals("pantry")) {
            System.out.println("Welcome to your pantry!");
            System.out.println("Would you like to [ ] ?"
                               + "\n[ 0 ] update pantry "
                               + "\n[ 1 ] show pantry contents "
                               + "\n[ 2 ] see what you can make "
                               + "\n[ 3 ] return"
                               + "\n:");
            printPantryContents();
        }
    }

    public void emptyPantry() {
        updatePantry("{}");
    }

    public void updatePantry(String content) {
        if (getFileContent(new File(pantryFilePath)) != null) {
            System.out.println(getFileContent(new File(pantryFilePath)));
        } else {
            writeToFile(pantryFilePath, content);
        }
    }

    public CustomJsonObject.Ingredient findPantryFile() {
        File file = new File(pantryFilePath);
        CustomJsonObject.Ingredient pantry =
            gson.fromJson(getFileContent(file), CustomJsonObject.Ingredient.class);
        String[] ingArr = new String[]{"apple", "banana", "milk"};
        updatePantry(gson.toJson(ingArr));
        return pantry;
    }

    public void printPantryContents() {
       File pantryFile = new File(pantryFilePath);
       CustomJsonObject.Ingredient pantry = findPantryFile();
       for (String ingredient : pantry.getIngredients()) {
           System.out.println(ingredient);
       }
    }

    public AppUtils() {
        super();
        this.userInterface();
    }

}
