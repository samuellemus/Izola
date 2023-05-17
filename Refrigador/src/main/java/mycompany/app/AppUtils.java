package mycompany.app;


import java.io.IOException;
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
//import com.google.gson.Gson;


public class AppUtils extends App{

    static String ENDPOINT_MEAL = "https://www.themealdb.com/api/json/v1/1";
    private String searchType;
    String searchItem;
    String json;

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


    public String searchMealDB(String q) {
        System.out.printf("Searching for %s\n", q);
        System.out.println("This should take no time at all...");
        try {
            String url = String.format("%s/%s?%s=%s",
                                       AppUtils.ENDPOINT_MEAL,
                                       "search.php",
                                       "s",
                                       URLEncoder.encode(q, StandardCharsets.UTF_8));
            this.json = AppUtils.fetchString(url);
            System.out.println(json);
            System.out.println("[ url ] " + url + "\n\n");
            return json;
        } catch (IllegalArgumentException | IOException | InterruptedException e) {
            System.out.println("Something went wrong with the mealDB api. " +
                               "Please try again or try a different search.");
            return null;
        }
    }

    /**
     * trim allows the user to interact with the program to allow to custom modification
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
            return mealString;
        }
        else if (value.toLowerCase().equals("options")) System.out.println("You chose options."
                                                                           + "\n this path is not coded yet");
        return null;
    }

    public AppUtils() {
        System.out.println("Called AppUtils");
        this.searchItem = AppUtils.userInterface();
        this.json = searchMealDB(this.searchItem);
    }
}
