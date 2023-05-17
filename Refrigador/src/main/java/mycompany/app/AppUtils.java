package mycompany.app;


import java.io.IOException;
import java.io.File;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse.BodyHandlers;
import java.net.http.HttpResponse;
import java.net.http.HttpClient;
import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Optional;
import java.util.Scanner;
//import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;


public class AppUtils extends App{

    static String ENDPOINT_MEAL = "https://www.themealdb.com/api/json/v1/1";
    private String searchType;
    String searchItem;
    String json;
    private static final Gson gson = new Gson();

    public static final HttpClient HTTP_CLIENT = HttpClient.newBuilder()
        .version(HttpClient.Version.HTTP_2)
        .followRedirects(HttpClient.Redirect.NORMAL)
        .build();

    public static String fetchString(String uri) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create(uri))
            .build();
        HttpResponse<String> response = HTTP_CLIENT.send(request, BodyHandlers.ofString());
        final int statusCode = response.statusCode();
        if (statusCode != 200) {
            throw new IOException("reponse status code not 200:" + statusCode);
        } // if
        return response.body().trim();
    } // fetchString


    public static Optional<ResponseObject.MealDBResult> searchMealDB(String q, String option_type) {
        try {
            String url = String.format("%s/%s.php?%s=%s", ENDPOINT_MEAL,
                                       option_type,"s",
                                       URLEncoder.encode(q, StandardCharsets.UTF_8));
            String json = AppUtils.fetchString(url);
            ResponseObject.MealDBResult result_meal =
                gson.fromJson(
                              json,
                              ResponseObject.MealDBResult.class);
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
    public static void userInterface() {
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
            AppUtils
                .searchMealDB(mealString, "search")
                .ifPresent(result -> processMealDBResult(result));
        }
        else if (value.toLowerCase().equals("options")) System.out.println("You chose options."
                                                                           + "\n this path is not coded yet");
    }

    ArrayList<ResponseObject.MealDBMeal> meals;

    private static void processMealDBResult(ResponseObject.MealDBResult result) {
        if (result.meals != null) {
            this.meals.addAll(Arrays.asList(result.meals));
            for (ResponseObject.MealDBMeal meal : result.meals) {

            }
            return;
        }
        System.out.println("Hm. I don't have anything for that. \n -> Perhaps try again?");
    }

    public AppUtils() {
        System.out.println("Called AppUtils");
        this.userInterface();
    }
}
