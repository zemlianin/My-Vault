package org.example.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import org.example.models.GrantedAuthorityDeserializer;
import org.example.models.dao.request.DirectoryRequest;
import org.example.models.dao.request.SignInRequest;
import org.example.models.dao.request.SignUpRequest;
import org.example.models.dao.response.DirectoryResponse;
import org.example.models.dao.response.JwtAuthenticationResponse;
import org.example.models.dao.transport.DirectoryTransport;
import org.example.repositories.UserRepository;
import org.example.services.dataAccess.DirectoryDataAccess;
import org.example.services.dataAccess.SecretDataAccess;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class DirectoryControllerIntegrationTest {
    @LocalServerPort
    int randomServerPort;

    @Autowired
    private ObjectMapper objectMapper;
    private String baseUrl;

    private final TestRestTemplate restTemplate;

    private final SecretDataAccess secretDataAccess;

    private final UserRepository userRepository;

    private final DirectoryDataAccess directoryDataAccess;

    private String authToken;

    @Autowired
    public DirectoryControllerIntegrationTest(TestRestTemplate testRestTemplate,
                                              SecretDataAccess secretDataAccess,
                                              DirectoryDataAccess directoryDataAccess,
                                              UserRepository userRepository) {
        this.restTemplate = testRestTemplate;
        this.secretDataAccess = secretDataAccess;
        this.userRepository = userRepository;
        this.directoryDataAccess = directoryDataAccess;
    }

    @BeforeEach
    void setUp() {
        baseUrl = "http://localhost:" + randomServerPort + "/api/v1/resource/directory";

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
        directoryDataAccess.deleteAllForAdmin();
    }

    @Test
    void testCreateNewDirectoryInRoot() {
        var request = new DirectoryRequest("a", null);

        var headers = new HttpHeaders();
        headers.setBearerAuth(authToken);
        var requestEntity = new HttpEntity<>(request, headers);

        var responseEntity = restTemplate.exchange(
                baseUrl + "/create",
                HttpMethod.POST,
                requestEntity,
                DirectoryResponse.class
        );

        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());
        assertNotNull(responseEntity.getBody().getId());
        assertEquals("a", responseEntity.getBody().getName());
    }

    @Test
    void testCreateNewDirectoryInNotRoot() {
        var user = userRepository.findByEmail("test@test.ru");

        var request1 = new DirectoryRequest("a", null);

        var directoryTransport = new DirectoryTransport(user.get(), request1);
        var parentDirectory = directoryDataAccess.addDirectory(directoryTransport);

        var request = new DirectoryRequest("a", parentDirectory.getParentId());

        var headers = new HttpHeaders();
        headers.setBearerAuth(authToken);
        var requestEntity = new HttpEntity<>(request, headers);

        var responseEntity = restTemplate.exchange(
                baseUrl + "/create",
                HttpMethod.POST,
                requestEntity,
                DirectoryResponse.class
        );

        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());
        assertNotNull(responseEntity.getBody().getId());
        assertEquals(parentDirectory.getParentId(), responseEntity.getBody().getParentId());
    }


    @Test
    void testCreateNewDirectoryInNotExistDirectory() {
        var request = new DirectoryRequest("a", UUID.randomUUID());

        var headers = new HttpHeaders();
        headers.setBearerAuth(authToken);
        var requestEntity = new HttpEntity<>(request, headers);

        var responseEntity = restTemplate.exchange(
                baseUrl + "/create",
                HttpMethod.POST,
                requestEntity,
                DirectoryResponse.class
        );

        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
    }

    @Test
    void testGetDirectoriesFromRoot() {
        var user = userRepository.findByEmail("test@test.ru");
        var request1 = new DirectoryRequest("a", null);

        var directoryTransport = new DirectoryTransport(user.get(), request1);
        directoryDataAccess.addDirectory(directoryTransport);
        directoryDataAccess.addDirectory(directoryTransport);


        var headers = new HttpHeaders();
        headers.setBearerAuth(authToken);
        var requestEntity = new HttpEntity<>(headers);

        var responseEntity = restTemplate.exchange(
                baseUrl + "/get_all",
                HttpMethod.GET,
                requestEntity,
                List.class
        );

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());
        assertEquals(responseEntity.getBody().size(), 2);
    }

    @Test
    void testGetDirectoriesFromEmptyRoot() {
        var headers = new HttpHeaders();
        headers.setBearerAuth(authToken);
        var requestEntity = new HttpEntity<>(headers);

        var responseEntity = restTemplate.exchange(
                baseUrl + "/get_all",
                HttpMethod.GET,
                requestEntity,
                List.class
        );

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());
        assertEquals(responseEntity.getBody().size(), 0);
    }
}
