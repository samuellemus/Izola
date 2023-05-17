package mycompany.app;

import com.google.gson.Gson;
import java.io.IOException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.net.URI;
import java.util.Optional;
import java.util.Scanner;

/**
 * Hello world!
 *
 */
public class App {
    private static final String ENDPOINT_MEAL = "https://www.themealdb.com/api/json/v1/1";
    private static final String CONFIG_PATH = "resources/config.properties";
    /** HTTP client */
    public static final HttpClient HTTP_CLIENT = HttpClient.newBuilder()
        .version(HttpClient.Version.HTTP_2) // Uses HTTP protocol version 2 where possible
        .followRedirects(HttpClient.Redirect.NORMAL) // always redirects, except from HTTP
        .build(); // Build and returns a HttpClient object.

    /**
     * fetchString Returns the response body string data from a URI.
     * @param uri location of desired content
     * @return response body string
     * @throws IOException if an I/O error occurs when sending or receiving
     * @throws InterruptedException if the HTTP client's {@code send} method is interrupted.
     **/
    public static String fetchString(String url) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create(url))
            .build();
        HttpResponse<String> response = HTTP_CLIENT.send(request, BodyHandlers.ofString());
        final int statusCode = response.statusCode();
        if (statusCode != 200) throw new IOException("response status code not 200: %d" + statusCode);
        System.out.println("\n" + response.statusCode());
        return response.body().trim();
    }

    /**
     * userInterface allows the user to interact with the program to allow to custom modification
     * of inquiries formatted and show in the CLI
     **/
    public static String userInterface() {
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
            String url = String.format("%s/%s?=%s=%s",
                                       ENDPOINT_MEAL,
                                       "search.php",
                                       "s",
                                       mealString);
            try {
                System.out.printf(url);
                //json = fetchString(url);
                scanner.close();
                return fetchString(url);
            } catch (Exception e) {
                scanner.close();
                System.out.println(e.toString());
                return null;
            }
        }
        else if (value.toLowerCase().equals("options")) System.out.println("You chose options."
                                                                           + "\n this path is not coded yet");
        return null;
    }

    public Optional.<App.MealDBResult> gsonParser(String json) {
        Gson gson = new Gson();
        try {

        }
        App.MealDBResult = gson.fromJson(json, App.MealDBResult.class);
    }

    public static class MealDBMeal {
        String idMeal;
        String strMeal;
        String strCategory;
        String strArea;
        String strInstructions;
        String strMealThumb;
        String strTags;
        String strYoutube;
        String strIngredient1, strIngredient2, strIngredient3, strIngredient4;
        String strIngredient5, strIngredient6, strIngredient7, strIngredient8;
        String strIngredient9, strIngredient10, strIngredient11, strIngredient12;
        String strIngredient13, strIngredient14, strIngredient15;
        String strSource;
        String strImageSource;
        String strMeasure1, strMeasure2, strMeasure3, strMeasure4, strMeasure5;
        String strMeasure6, strMeasure7, strMeasure8, strMeasure9, strMeasure10;
        String strMeasure11, strMeasure12, strMeasure13, strMeasure14, strMeasure15;
        String idIngredient;
        String strIngredient;
        String strDescription;
    }

    public static class MealDBResult {
        MealDBMeal[] meals;
    }


    public static void main( String[] args ) {
        System.out.println( "Hello World!" );
        System.out.println(App.userInterface());
    }
}
