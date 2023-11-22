package org.example.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import org.apache.http.client.methods.HttpPatch;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.example.models.GrantedAuthorityDeserializer;
import org.example.models.dao.*;
import org.example.models.entities.Secret;
import org.example.repositories.UserRepository;
import org.example.services.SecretDataAccess;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.test.context.ActiveProfiles;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class SecretControllerIntegrationTest {
    @LocalServerPort
    int randomServerPort;

    @Autowired
    private ObjectMapper objectMapper;
    private String baseUrl;

    private final TestRestTemplate restTemplate;

    private final SecretDataAccess secretDataAccess;

    private final UserRepository userRepository;

    @Autowired
    public SecretControllerIntegrationTest(TestRestTemplate testRestTemplate,
                                           SecretDataAccess secretDataAccess,
                                           UserRepository userRepository) {
        this.restTemplate = testRestTemplate;
        this.secretDataAccess = secretDataAccess;
        this.userRepository = userRepository;
    }

    private String authToken;

    @BeforeEach
    void setUp() {
        baseUrl = "http://localhost:" + randomServerPort + "/api/v1/resource/secret";

        SimpleModule module = new SimpleModule();
        module.addDeserializer(GrantedAuthority.class, new GrantedAuthorityDeserializer());
        objectMapper.registerModule(module);

        var signUpRequest = new SignUpRequest("a", "a", "test@test.ru", "password");
        restTemplate.postForEntity("/api/v1/auth/signup", signUpRequest, Void.class);

        var signInRequest = new SignInRequest("test@test.ru", "password");
        var signInResponse = restTemplate.postForEntity("/api/v1/auth/signin", signInRequest, JwtAuthenticationResponse.class);


        assertEquals(HttpStatus.OK, signInResponse.getStatusCode());
        assertNotNull(signInResponse.getBody().getToken());
        authToken = signInResponse.getBody().getToken();
    }


    @AfterEach
    public void resetDb() {
        secretDataAccess.deleteAllForAdmin();
    }

    @Test
    void testCreateNewSecret() {
        var request = new SecretRequest("a", "a", "a", "a");

        var headers = new HttpHeaders();
        headers.setBearerAuth(authToken);
        var requestEntity = new HttpEntity<>(request, headers);

        var responseEntity = restTemplate.exchange(
                baseUrl + "/create",
                HttpMethod.POST,
                requestEntity,
                SecretResponse.class
        );

        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());
        assertNotNull(responseEntity.getBody().getId());
        assertEquals("a", responseEntity.getBody().getName());
    }

    @Test
    public void testPing() throws URISyntaxException {
        var uri = new URI(baseUrl + "/ping?key=1");

        var headers = new HttpHeaders();
        headers.setBearerAuth(authToken);
        var requestEntity = new HttpEntity<>(headers);

        var result = restTemplate.exchange(
                uri,
                HttpMethod.GET,
                requestEntity,
                Integer.class
        );

        assertEquals(2, result.getBody());
    }


    @Test
    public void testCreateWithoutArguments() throws URISyntaxException {
        var uri = new URI(baseUrl + "/create");
        var secretRequest = new SecretRequest(null, "a", null, "a");

        var headers = new HttpHeaders();
        headers.setBearerAuth(authToken);
        var requestEntity = new HttpEntity<>(secretRequest, headers);

        var response = restTemplate.exchange(
                uri,
                HttpMethod.POST,
                requestEntity,
                SecretResponse.class
        );

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    public void testGetAll() throws URISyntaxException {
        var uri = new URI(baseUrl + "/get_all");
        var user = userRepository.findByEmail("test@test.ru");
        secretDataAccess.addSecret(user.get(), new Secret("a", "a", "a", "a"));
        secretDataAccess.addSecret(user.get(), new Secret("b", "b", "b", "b"));

        var headers = new HttpHeaders();
        headers.setBearerAuth(authToken);
        var requestEntity = new HttpEntity<>(headers);

        var response = restTemplate.exchange(
                uri,
                HttpMethod.GET,
                requestEntity,
                ListSecretsResponse.class
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(2, response.getBody().getSecrets().size());
    }

    @Test
    public void testGetAllStrangeVectors() throws URISyntaxException {
        var signUpRequest = new SignUpRequest("a", "a", "stranger@test.ru", "password");
        restTemplate.postForEntity("/api/v1/auth/signup", signUpRequest, Void.class);

        var signInRequest = new SignInRequest("stranger@test.ru", "password");
        var signInResponse = restTemplate.postForEntity("/api/v1/auth/signin", signInRequest, JwtAuthenticationResponse.class);

        var strangeToken = signInResponse.getBody().getToken();


        var uri = new URI(baseUrl + "/get_all");
        var user = userRepository.findByEmail("test@test.ru");
        secretDataAccess.addSecret(user.get(), new Secret("a", "a", "a", "a"));
        secretDataAccess.addSecret(user.get(), new Secret("b", "b", "b", "b"));

        var headers = new HttpHeaders();
        headers.setBearerAuth(strangeToken);
        var requestEntity = new HttpEntity<>(headers);

        var response = restTemplate.exchange(
                uri,
                HttpMethod.GET,
                requestEntity,
                ListSecretsResponse.class
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(0, response.getBody().getSecrets().size());
    }

    @Test
    public void testGetAllPaginate() throws URISyntaxException {
        var uri = new URI(baseUrl + "/get_all_paginate?page=0&size=1");
        var user = userRepository.findByEmail("test@test.ru");

        secretDataAccess.addSecret(user.get(), new Secret("a", "a", "a", "a"));
        secretDataAccess.addSecret(user.get(), new Secret("b", "b", "b", "b"));

        var headers = new HttpHeaders();
        headers.setBearerAuth(authToken);
        var requestEntity = new HttpEntity<>(headers);

        var response = restTemplate.exchange(
                uri,
                HttpMethod.GET,
                requestEntity,
                ListSecretsResponse.class
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().getSecrets().size());
    }

    @Test
    public void testGet() throws URISyntaxException {
        var user = userRepository.findByEmail("test@test.ru");

        var secret = new Secret("a", "a", "a", "a");
        secret.setId(secretDataAccess.addSecret(user.get(), secret).getId());

        var id = secret.getId();
        var uri = new URI(baseUrl + "/get?id=" + id);

        var headers = new HttpHeaders();
        headers.setBearerAuth(authToken);
        var requestEntity = new HttpEntity<>(headers);

        var response = restTemplate.exchange(
                uri,
                HttpMethod.GET,
                requestEntity,
                SecretResponse.class
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(secret.getName(), response.getBody().getName());
    }

    @Test
    public void testGetNotFound() throws URISyntaxException {
        var id = UUID.randomUUID();
        var uri = new URI(baseUrl + "/get?id=" + id);

        var headers = new HttpHeaders();
        headers.setBearerAuth(authToken);
        var requestEntity = new HttpEntity<>(headers);

        var response = restTemplate.exchange(
                uri,
                HttpMethod.GET,
                requestEntity,
                SecretResponse.class
        );

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
        var user = userRepository.findByEmail("test@test.ru");

        var secret = new Secret("a", "a", "a", "a");
        secret.setId(secretDataAccess.addSecret(user.get(), secret).getId());

        var id = secret.getId();
        var uri = baseUrl + "/change?id=" + id;
        var secretRequest = new SecretRequest("newName", "newUrl", "newLogin", "newPassword");

        var httpClient = HttpClients.createDefault();
        var httpPatch = new HttpPatch(uri);
        var entity = new StringEntity(new ObjectMapper().writeValueAsString(secretRequest));
        entity.setContentType("application/json");
        httpPatch.setEntity(entity);

        httpPatch.setHeader("Authorization", "Bearer " + authToken);

        var response = httpClient.execute(httpPatch);

        int statusCode = response.getStatusLine().getStatusCode();
        assertEquals(HttpStatus.OK.value(), statusCode);

        var responseEntity = response.getEntity();
        var responseBody = EntityUtils.toString(responseEntity);

        var secretResponse = new ObjectMapper().readValue(responseBody, SecretResponse.class);
        assertEquals("newName", secretResponse.getName());

        httpClient.close();
    }
}
