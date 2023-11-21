package org.example.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.client.methods.HttpPatch;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.example.models.dao.*;
import org.example.models.entities.Secret;
import org.example.models.entities.User;
import org.example.services.SecretDataAccess;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ActiveProfiles;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class SecretControllerIntegrationTest {
    @LocalServerPort
    int randomServerPort;
    private String baseUrl;

    private final TestRestTemplate restTemplate;

    private final SecretDataAccess secretDataAccess;

    private String token;

    @Autowired
    public SecretControllerIntegrationTest(TestRestTemplate testRestTemplate, SecretDataAccess secretDataAccess) {
        this.restTemplate = testRestTemplate;
        this.secretDataAccess = secretDataAccess;
    }
    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    private String authToken;

    @BeforeEach
    void setUp() {
        // Perform signup and signin to get the authentication token
        baseUrl = "http://localhost:" + randomServerPort + "/api/v1/resource/secret";
        SignUpRequest signUpRequest = new SignUpRequest("testuser", "password");
        restTemplate.postForEntity("/api/v1/auth/signup", signUpRequest, Void.class);

        SignInRequest signInRequest = new SignInRequest("testuser", "password");
        ResponseEntity<Void> signInResponse = restTemplate.postForEntity("/api/v1/auth/signin", signInRequest, Void.class);

        assertEquals(HttpStatus.OK, signInResponse.getStatusCode());
        assertNotNull(signInResponse.getHeaders().get("Authorization"));
        authToken = signInResponse.getHeaders().get("Authorization").get(0);
    }
    @BeforeAll
    public void signUp() throws URISyntaxException {

        var uri = new URI( "/api/v1/auth/signup");

        var request = new SignUpRequest("test", "test","test@test.ru", "test");
        var response = restTemplate.postForEntity(uri, request, JwtAuthenticationResponse.class);
        token = response.getBody().getToken();
    }

    @AfterEach
    public void resetDb() {
        secretDataAccess.deleteAllForAdmin();
    }

    @Test
    public void testPing() throws URISyntaxException {
        var uri = new URI(baseUrl + "/ping?key=1");
        restTemplate
        var result = restTemplate.getForEntity(uri, Integer.class);
        assertEquals(2, result.getBody());
    }

    @Test
    public void testCreate() throws URISyntaxException {
        var uri = new URI(baseUrl + "/create");
        var secretRequest = new SecretRequest("a", "a", "a", "a");

        var response = restTemplate.postForEntity(uri, secretRequest, SecretResponse.class);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals("a", response.getBody().getSecret().getName());
        assertEquals("a", response.getBody().getSecret().getUrl());
        assertEquals("a", response.getBody().getSecret().getLogin());
        assertEquals("a", response.getBody().getSecret().getPassword());
    }

    @Test
    public void testCreateWithoutArguments() throws URISyntaxException {
        var uri = new URI(baseUrl + "/create");
        var secretRequest = new SecretRequest(null, "a", null, "a");

        var response = restTemplate.postForEntity(uri, secretRequest, SecretResponse.class);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    public void testGetAll() throws URISyntaxException {
        var uri = new URI(baseUrl + "/get_all");
        secretDataAccess.addSecret(new Secret("a", "a", "a", "a"));
        secretDataAccess.addSecret(new Secret("b", "b", "b", "b"));

        var response = restTemplate.getForEntity(uri, ListSecretsResponse.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(2, response.getBody().getSecrets().size());
    }

    @Test
    public void testGetAllPaginate() throws URISyntaxException {
        var uri = new URI(baseUrl + "/get_all_paginate?page=0&size=1");
        secretDataAccess.addSecret(new Secret("a", "a", "a", "a"));
        secretDataAccess.addSecret(new Secret("b", "b", "b", "b"));

        var response = restTemplate.getForEntity(uri, ListSecretsResponse.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().getSecrets().size());
    }

    @Test
    public void testGet() throws URISyntaxException {
        var secret = new Secret("a", "a", "a", "a");
        secret.setId(secretDataAccess.addSecret(secret).getId());

        var id = secret.getId();
        var uri = new URI(baseUrl + "/get?id=" + id);
        var response = restTemplate.getForEntity(uri, SecretResponse.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(secret.getName(), response.getBody().getSecret().getName());
    }

    @Test
    public void testGetNotFound() throws URISyntaxException {
        var id = UUID.randomUUID();
        var uri = new URI(baseUrl + "/get?id=" + id);
        var response = restTemplate.getForEntity(uri, SecretResponse.class);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    /*
     Простите конечно за следующий тест, он работает, но происходит все буквально руками.
     Я считаю что эта джава имеет серьезные проблемы с порядком в версиях, а если серьезно то у джавы в них бардак
     я не понимаю почему если я подрубаю новую библиотеку с тестами JUnit, то библиотека тащит за собой старую версию спринга
     настолько старую что она не поддерживает запрос PATCH, И ЕСЛИ ты захочешь просто написать новую версию спринга руками в мавене
     то выясняется что нужно переписывать кучу кода, потому что в старых версиях спринга нашли уязвимость и они решили
     поменять как можно больше названий аннатоций, этот тест просто забастовка против БАРДАКА в версиях спринга,
     если он и будет продолжаться я продолжу использовать Apache HttpClient,
     Да, я буду писать больше кода, но сохраню свои нервные клетки.

     НО ПАСАРАН!
     */
    @Test
    public void testChange() throws IOException {
        var secret = new Secret("a", "a", "a", "a");
        secret.setId(secretDataAccess.addSecret(secret).getId());

        var id = secret.getId();
        var uri = baseUrl + "/change?id=" + id;
        var secretRequest = new SecretRequest("newName", "newUrl", "newLogin", "newPassword");

        var httpClient = HttpClients.createDefault();
        var httpPatch = new HttpPatch(uri);
        var entity = new StringEntity(new ObjectMapper().writeValueAsString(secretRequest));
        entity.setContentType("application/json");
        httpPatch.setEntity(entity);

        var response = httpClient.execute(httpPatch);

        int statusCode = response.getStatusLine().getStatusCode();
        assertEquals(        HttpStatus.OK.value(), statusCode);

        var responseEntity = response.getEntity();
        var responseBody = EntityUtils.toString(responseEntity);

        var secretResponse = new ObjectMapper().readValue(responseBody, SecretResponse.class);
        assertEquals("newName", secretResponse.getSecret().getName());

        httpClient.close();
    }
}
