package demo;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;

public final class OAuthDemo {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    private OAuthDemo() {
        // Utility class
    }

    public static void main(String[] args) throws Exception {
        AppConfig config = AppConfig.fromEnv();
        HttpClient client = HttpClient.newHttpClient();

        TokenResponse tokenResponse = getAccessToken(client, config);

        System.out.println("Access token acquired successfully.");
        System.out.println("Token type: " + tokenResponse.tokenType());
        System.out.println("Expires in: " + tokenResponse.expiresIn() + " seconds");

        if (config.callProtectedApi()) {
            callProtectedApi(client, config, tokenResponse.accessToken());
        }
    }

    private static TokenResponse getAccessToken(HttpClient client, AppConfig config)
            throws IOException, InterruptedException {

        StringBuilder form = new StringBuilder()
                .append("grant_type=client_credentials")
                .append("&client_id=").append(encode(config.clientId()))
                .append("&client_secret=").append(encode(config.clientSecret()));

        if (!config.scope().isBlank()) {
            form.append("&scope=").append(encode(config.scope()));
        }

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(config.tokenUrl()))
                .header("Content-Type", "application/x-www-form-urlencoded")
                .header("Accept", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(form.toString()))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() != 200) {
            throw new IllegalStateException(
                    "Token request failed with status " + response.statusCode()
                            + ". Check TOKEN_URL, CLIENT_ID, CLIENT_SECRET, and client configuration."
            );
        }

        JsonNode json = OBJECT_MAPPER.readTree(response.body());

        String accessToken = requiredJsonText(json, "access_token");
        String tokenType = json.path("token_type").asText("Bearer");
        int expiresIn = json.path("expires_in").asInt(-1);

        return new TokenResponse(accessToken, tokenType, expiresIn);
    }

    private static void callProtectedApi(HttpClient client, AppConfig config, String accessToken)
            throws IOException, InterruptedException {

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(config.apiUrl()))
                .header("Authorization", "Bearer " + accessToken)
                .header("Accept", "application/json")
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        System.out.println("Protected API response code: " + response.statusCode());
        System.out.println("Protected API response body:");
        System.out.println(response.body());
    }

    private static String requiredJsonText(JsonNode json, String fieldName) {
        String value = json.path(fieldName).asText();
        if (value == null || value.isBlank()) {
            throw new IllegalStateException("Missing expected field in token response: " + fieldName);
        }
        return value;
    }

    private static String encode(String value) {
        return URLEncoder.encode(value, StandardCharsets.UTF_8);
    }

    private record TokenResponse(String accessToken, String tokenType, int expiresIn) {
    }

    private record AppConfig(
            String tokenUrl,
            String clientId,
            String clientSecret,
            String scope,
            String apiUrl,
            boolean callProtectedApi
    ) {
        static AppConfig fromEnv() {
            return new AppConfig(
                    requireEnv("TOKEN_URL"),
                    requireEnv("CLIENT_ID"),
                    requireEnv("CLIENT_SECRET"),
                    getenvOrDefault("SCOPE", ""),
                    getenvOrDefault("API_URL", "https://your-api.example.com/protected"),
                    Boolean.parseBoolean(getenvOrDefault("CALL_PROTECTED_API", "false"))
            );
        }

        private static String requireEnv(String name) {
            String value = System.getenv(name);
            if (value == null || value.isBlank()) {
                throw new IllegalStateException("Missing required environment variable: " + name);
            }
            return value;
        }

        private static String getenvOrDefault(String name, String defaultValue) {
            String value = System.getenv(name);
            return (value == null || value.isBlank()) ? defaultValue : value;
        }
    }
}
