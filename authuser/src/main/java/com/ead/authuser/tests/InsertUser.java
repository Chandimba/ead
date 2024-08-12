package com.ead.authuser.tests;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

public class InsertUser {

    public static void main(String[] args) {
        String jsonFilePath = "C:\\Users\\2022\\Downloads\\users.json"; // Coloque o caminho correto para o ficheiro users.json
        String apiUrl = "http://localhost:8080/ead-authuser/auth/signup";
        String username = "admin";
        String password = "123456";

        try {
            // Ler o ficheiro JSON e converter para uma lista de Mapas
            ObjectMapper objectMapper = new ObjectMapper();
            Reader reader = new FileReader(jsonFilePath);
            List<Map<String, Object>> users = objectMapper.readValue(reader, new TypeReference<List<Map<String, Object>>>(){});

            // Chamar o serviço para cada utilizador
            for (Map<String, Object> user : users) {
                try {
                    sendPostRequest(apiUrl, user, username, password);
                } catch (Exception e) {}
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void sendPostRequest(String apiUrl, Map<String, Object> userData, String username, String password) throws IOException {
        // Criar a conexão HTTP
        URL url = new URL(apiUrl);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        // Configurar o método POST e a autenticação básica
        connection.setRequestMethod("POST");
        String auth = username + ":" + password;
        String encodedAuth = Base64.getEncoder().encodeToString(auth.getBytes(StandardCharsets.UTF_8));
        //connection.setRequestProperty("Authorization", "Basic " + encodedAuth);
        connection.setRequestProperty("Content-Type", "application/json; utf-8");
        connection.setRequestProperty("Accept", "application/json");
        connection.setDoOutput(true);

        // Converter o mapa de dados do utilizador para JSON e enviar no corpo da requisição
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonInputString = objectMapper.writeValueAsString(userData);

        try (var outputStream = connection.getOutputStream()) {
            byte[] input = jsonInputString.getBytes(StandardCharsets.UTF_8);
            outputStream.write(input, 0, input.length);
        }

        // Ler a resposta do servidor
        int responseCode = connection.getResponseCode();
        System.out.println("Response Code: " + responseCode);

        // Fechar a conexão
        connection.disconnect();
    }
}
