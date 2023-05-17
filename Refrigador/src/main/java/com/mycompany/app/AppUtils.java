package mycompany.app;


import java.io.IOException;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse.BodyHandlers;
import java.net.http.HttpResponse;
import java.net.http.HttpClient;
import java.net.URI;
import java.net.URLEncoder;
import java.util.Optional;
import com.google.gson.Gson;


public class AppUtils {

    private static String ENDPOINT_MEAL = "https://www.themealdb.com/api/json/v1/1";



    public static final HttpClient HTTP_CLIENT = HttpClient.newBuilder()
        .version(HttpClient.Version.HTTP_2)
        .followRedirects(HttpClient.Redirect.NORMAL)
        .build();

    public Gson gson = new Gson();

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


    public static Optional<ResponseObject.MealDBResult> searchMealDB(String q) {
        System.out.printf("Searching for %s\n", q);
        System.out.println("This should take no time at all...");
        try {
            String url = String.format("%s/%s.php?%s=%s",
                                       ENDPOINT_MEAL,
                                       "search",
                                       "s",
                                       URLEncoder.encode(q, StandardCharsets.UTF_8));
            String json = ApiAppFunct.fetchString(url);
            System.out.println(json);
            ResponseObject.MealDBResult result_meal =
                GSON.fromJson(
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



    public AppUtils() {
        System.out.println("Called AppUtils");
    }
}
