package com.mycompany.app;

import com.google.gson.Gson;
import java.io.IOException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.net.URI;
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

    /**  Google {@code Gson} object for parsing JSON-formatted strings. */
    Gson gson = new Gson();

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
            .GET()
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
    public String userInterface() {
        // Create a Scanner object
        Scanner scanner = new Scanner(System.in);
        String input = scanner.nextLine();
        System.out.println(input);
        scanner.close();
    }

    public static void main( String[] args ) {
        System.out.println( "Hello World!" );
        userInterface();
    }
}
