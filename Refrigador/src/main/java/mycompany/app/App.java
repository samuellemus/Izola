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
import java.util.Map;
//import com.google.gson.Gson;
//import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Hello world!
 *
 */
public class App {

    public void processMealDB(String json) {
        System.out.println(json);
    }

    public static void main( String[] args ) {
        System.out.println( "Hello World!" );
        String json = "{\"name\":\"John\",\"age\":31,\"city\":\"London\"}";

        System.out.println(json);
        //AppUtils utility = new AppUtils();
        //System.out.println(utility.searchItem);
        //System.out.println(utility.json);
        //App.processMealDB(utility.json);
    }
}
