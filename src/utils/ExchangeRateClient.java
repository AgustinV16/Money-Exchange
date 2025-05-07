package utils;

import base.Currency;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import io.github.cdimascio.dotenv.Dotenv;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;

public class ExchangeRateClient {
    private static final String API_BASE_URL = "https://v6.exchangerate-api.com/v6/";
    private static final Dotenv dotenv = Dotenv.load(); // Load .env file
    private static final String API_KEY = dotenv.get("API_KEY");

    public List<Currency> requestConversion(String baseName, String baseCode, String targetName, String targetCode, String amount) {
        String url = String.format("%s%s/pair/%s/%s/%s", API_BASE_URL, API_KEY, baseCode, targetCode, amount);
        JsonObject response = sendRequest(url);

        if ("error".equalsIgnoreCase(response.get("result").getAsString())) {
            throw new RuntimeException("API request failed. Please check your API key.");
        }

        String resultValue = response.get("conversion_result").getAsString();

        List<Currency> result = new ArrayList<>();
        result.add(new Currency(baseName, baseCode, amount));
        result.add(new Currency(targetName, targetCode, resultValue));

        return result;
    }

    private JsonObject sendRequest(String url) {
        try {
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            return new Gson().fromJson(response.body(), JsonObject.class);

        } catch (IOException | InterruptedException e) {
            throw new RuntimeException("An unexpected error occurred. Please check your internet connection.");
        }
    }
}
