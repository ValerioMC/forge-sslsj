package com.forge.sslsj.controller;// package: com.example.demo;

import com.forge.sslsj.ForgeSslsjApplication;
import com.forge.sslsj.model.AuthenticationRequest;
import com.forge.sslsj.model.AuthenticationResponse;
import com.forge.sslsj.payload.PersonRequest;
import com.forge.sslsj.payload.PersonResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(classes = ForgeSslsjApplication.class)
@ActiveProfiles("test")
public class PersonIntegrationTest {

    @Autowired
    private RestTemplate restTemplate;

    @LocalServerPort
    private int port;

    @Test
    public void testCreateAndGetPerson() {
        String jwtToken = authenticateAndGetToken();

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(jwtToken);
        
//       
        // Create a new Person
        PersonRequest personRequest = new PersonRequest();
        personRequest.setFirstName("John");
        personRequest.setLastName("Doe");
        personRequest.setEmail("john.doe@example.com");
        personRequest.setPhone("123-456-7890");
        personRequest.setAddress("123 Main St");
        personRequest.setCity("Sample City");
        personRequest.setState("Sample State");
        personRequest.setCountry("Sample Country");
        personRequest.setPostalCode("12345");

        
        // Perform a POST request to create a new person
        ResponseEntity<PersonResponse> createResponse = restTemplate.postForEntity("/api/persons", new HttpEntity<>(personRequest, headers), PersonResponse.class);
        assertEquals(HttpStatus.OK, createResponse.getStatusCode());
        assertNotNull(createResponse.getBody());
        Long personId = createResponse.getBody().getId();

        // Perform a GET request to retrieve the created person
        ResponseEntity<PersonResponse> getResponse = restTemplate.getForEntity("/api/persons/" + personId, PersonResponse.class);
        assertEquals(HttpStatus.OK, getResponse.getStatusCode());
        assertNotNull(getResponse.getBody());

        // Validate the retrieved person details
        assertEquals(personRequest.getFirstName(), getResponse.getBody().getFirstName());
        assertEquals(personRequest.getLastName(), getResponse.getBody().getLastName());
        assertEquals(personRequest.getEmail(), getResponse.getBody().getEmail());
    }

    private String authenticateAndGetToken() {
        AuthenticationRequest loginRequest = new AuthenticationRequest("user", "password");

        ResponseEntity<AuthenticationResponse> responseEntity = restTemplate.postForEntity("http://localhost:"+ port + "/authenticate", loginRequest, AuthenticationResponse.class);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());
        return responseEntity.getBody().jwt();
    }
}