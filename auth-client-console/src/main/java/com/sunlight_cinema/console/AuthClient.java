package com.sunlight_cinema.console;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.io.entity.StringEntity;
import org.apache.hc.core5.http.ContentType;

import java.util.Scanner;

public class AuthClient {
    private static final String URL = "http://localhost:8080/api/auth/register";
    private static final ObjectMapper mapper = new ObjectMapper();

    public static void main(String[] args) throws Exception {
        try (CloseableHttpClient client = HttpClients.createDefault();
             Scanner sc = new Scanner(System.in, "UTF-8")) {

            System.out.print("Username: ");
            String username = sc.nextLine();
            System.out.print("Password: ");
            String password = sc.nextLine();
            System.out.print("Email: ");
            String email = sc.nextLine();

            String json = String.format("""
                {
                  "username": "%s",
                  "password": "%s",
                  "email": "%s"
                }
                """, username, password, email);

            HttpPost post = new HttpPost(URL);
            post.setHeader("Content-Type", "application/json");
            post.setEntity(new StringEntity(json, ContentType.APPLICATION_JSON));

            client.execute(post, response -> {
                String body = org.apache.hc.core5.http.io.entity.EntityUtils.toString(response.getEntity());
                JsonNode node = mapper.readTree(body);
                System.out.println("\nОтвет сервера:");
                System.out.println(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(node));
                return null;
            });
        }
    }
}