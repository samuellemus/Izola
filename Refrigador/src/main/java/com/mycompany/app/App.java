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
            String url = String.format("%s/%s?=%s=%s",
                                       ENDPOINT_MEAL,
                                       "search.php",
                                       "s",
                                       mealString);
            try {
                System.out.println(url);
                //json = fetchString(url);
                scanner.close();
            } catch (Exception e) {
                scanner.close();
                System.out.println(e.toString());
            }
        }
        else if (value.toLowerCase().equals("options")) System.out.println("You chose options."
                                                                           + "\n this path is not coded yet");
    }


    public static void main( String[] args ) {
        System.out.println( "Hello World!" );
    }
}
