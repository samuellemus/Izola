package mycompany.app;


import java.io.IOException;
import java.io.File;
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
import java.util.Arrays;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;
import com.google.gson.Gson;

public class AppUtils extends App{
    ArrayList<String> rawIngredients = new ArrayList<>();
    ArrayList<String> ingredients = new ArrayList<>();
    ArrayList<ArrayList<String>> mealIngredients = new ArrayList<>();
    ArrayList<ResponseObject.MealDBMeal> meals;
    static String ENDPOINT_MEAL = "https://www.themealdb.com/api/json/v1/1";
    private String searchType;
    String searchItem;
    String json;
    private static final Gson gson = new Gson();
    String[] ingredientArray;
    HashSet<String> knownIngredients = new HashSet<>();
    List<CustomJsonObject.Meal> listOfMeals = new ArrayList<>();

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

    /**
     * trim allows the user to interact with the program to allow to custom modification
     * of inquiries formatted and show in the CLI
     **/
    public void userInterface() {
        // Create a Scanner object
        Scanner scanner = new Scanner(System.in);
        /**  Google {@code Gson} object for parsing JSON-formatted strings. */
        String json;
        System.out.println("[options] [search]");
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
    }

    private void processMealDBResult(ResponseObject.MealDBResult result) {
        if (result.meals != null) {
            meals = new ArrayList<>();
            for (ResponseObject.MealDBMeal meal : result.meals) meals.add(meal);
            System.out.printf("There are %s meals available.\n", meals.size());
            meals.stream().forEach(meal -> processMeal(meal));
            System.out.println("There is a possible "
                           + knownIngredients.size()
                           + " unique ingredients to choose from.");
            convertAndSave();
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
        rawIngredients.stream().forEach(ing -> {
                if (ing != null && ing.length() > 0) {
                    ingredients.add(ing);
                    knownIngredients.add(ing);
                }
            });
        ingredientArray = new String[ingredients.size()];
        for (int i = 0; i < ingredients.size() ; i++) ingredientArray[i] = ingredients.get(i);
        String mealString = meal.strMeal;
        addToCollection(mealString, this.ingredientArray);
        this.rawIngredients.clear();
        this.ingredients.clear();
    }

    private void addToCollection(String strMeal, String[] ingredients) {
        String mealName = strMeal.replace(" ", "_");
        mealName = mealName.replace("&", "and");
        mealName = mealName.replace("'", "");
        System.out.println(mealName);
        CustomJsonObject.Meal customMeal =
            new CustomJsonObject.Meal(mealName, ingredients);
        this.listOfMeals.add(customMeal);
    }

    private void convertAndSave() {
        Gson gson = new Gson();
        this.listOfMeals.stream().forEach(meal -> makeFile(meal.getMealName(), gson.toJson(meal)));
    }

    private void makeFile(String mealName, String content) {
        String path = String.format("resources/archive/meals/%s.json", mealName);
        try {
            File file = new File(path);
            if (file.createNewFile()) {
                System.out.println("File created: " + file.getName());
                writeToFile(path, content);
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
            myWriter.write(content);
            myWriter.close();
            System.out.println(String.format("[ %s ] : populated.", path));
        } catch (IOException e) {
            System.out.println("An error occured.");
            e.printStackTrace();
        }
    }

    public AppUtils() {
        System.out.println("Called AppUtils");
        this.userInterface();
    }
}
