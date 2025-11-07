package com.sunlight_cinema.console;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.hc.client5.http.classic.methods.*;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.apache.hc.core5.http.io.entity.StringEntity;
import org.apache.hc.core5.http.ContentType;

import java.util.Scanner;

public class AuthClient {
    private static final String BASE_URL = "http://localhost:8080";
    private static final ObjectMapper mapper = new ObjectMapper();

    public static void main(String[] args) throws Exception {
        try (CloseableHttpClient client = HttpClients.createDefault();
             Scanner sc = new Scanner(System.in, "UTF-8")) {

            while (true) {
                System.out.println("\n=== CRUD для User ===");
                System.out.println("1. Создать (register)");
                System.out.println("2. Получить всех");
                System.out.println("3. Получить по ID");
                System.out.println("4. Обновить");
                System.out.println("5. Удалить");
                System.out.println("0. Выход");
                System.out.print("Выбор: ");

                String choice = sc.nextLine();
                if (choice.equals("0")) break;

                switch (choice) {
                    case "1" -> register(client, sc);
                    case "2" -> getAll(client);
                    case "3" -> getById(client, sc);
                    case "4" -> update(client, sc);
                    case "5" -> delete(client, sc);
                    default -> System.out.println("Неверный выбор");
                }
            }
        }
    }

    private static void register(CloseableHttpClient client, Scanner sc) throws Exception {
        System.out.print("Username: "); String username = sc.nextLine();
        System.out.print("Password: "); String password = sc.nextLine();
        System.out.print("Email: "); String email = sc.nextLine();

        String json = String.format("{\"username\":\"%s\",\"password\":\"%s\",\"email\":\"%s\"}", username, password, email);
        HttpPost post = new HttpPost(BASE_URL + "/api/auth/register");
        post.setEntity(new StringEntity(json, ContentType.APPLICATION_JSON));
        executeAndPrint(client, post);
    }

    private static void getAll(CloseableHttpClient client) throws Exception {
        HttpGet get = new HttpGet(BASE_URL + "/api/users");
        executeAndPrint(client, get);
    }

    private static void getById(CloseableHttpClient client, Scanner sc) throws Exception {
        System.out.print("ID: "); String id = sc.nextLine();
        HttpGet get = new HttpGet(BASE_URL + "/api/users/" + id);
        executeAndPrint(client, get);
    }

    private static void update(CloseableHttpClient client, Scanner sc) throws Exception {
        System.out.print("ID: "); String id = sc.nextLine();
        System.out.print("Новый username: "); String username = sc.nextLine();
        System.out.print("Новый email: "); String email = sc.nextLine();

        String json = String.format("{\"username\":\"%s\",\"email\":\"%s\"}", username, email);
        HttpPut put = new HttpPut(BASE_URL + "/api/users/" + id);
        put.setEntity(new StringEntity(json, ContentType.APPLICATION_JSON));
        executeAndPrint(client, put);
    }

    private static void delete(CloseableHttpClient client, Scanner sc) throws Exception {
        System.out.print("ID: "); String id = sc.nextLine();
        HttpDelete delete = new HttpDelete(BASE_URL + "/api/users/" + id);
        executeAndPrint(client, delete);
    }

    private static void executeAndPrint(CloseableHttpClient client, HttpUriRequestBase request) throws Exception {
        client.execute(request, response -> {
            String body = EntityUtils.toString(response.getEntity());
            System.out.println("\nСтатус: " + response.getCode());
            System.out.println("Ответ:");
            System.out.println(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(mapper.readTree(body)));
            return null;
        });
    }
}