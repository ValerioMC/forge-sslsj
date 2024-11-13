package com.forge.sslsj.controller;// package: com.example.demo;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.forge.sslsj.ForgeSslsjApplication;
import com.forge.sslsj.model.AuthenticationRequest;
import com.forge.sslsj.model.AuthenticationResponse;
import com.forge.sslsj.payload.PersonRequest;
import com.forge.sslsj.payload.PersonResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = ForgeSslsjApplication.class)
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class PersonIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void testCreateAndGetPerson() throws Exception {
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

        MockHttpServletRequestBuilder createPersonRequest =
                MockMvcRequestBuilders.post("/api/persons").headers(headers)
                        .content(objectMapper.writeValueAsString(personRequest))
                        .contentType(MediaType.APPLICATION_JSON);

        String contentAsString = mockMvc.perform(createPersonRequest)
                .andExpect(status().isOk()).andReturn()
                .getResponse().getContentAsString();

        PersonResponse personResponse = objectMapper.readValue(contentAsString, PersonResponse.class);

        // Validate the retrieved person details
        assertEquals(personRequest.getFirstName(), personResponse.getFirstName());
        assertEquals(personRequest.getLastName(), personResponse.getLastName());
        assertEquals(personRequest.getEmail(), personResponse.getEmail());
    }

    private String authenticateAndGetToken() throws Exception {
        // Prepare the login request
        AuthenticationRequest loginRequest = new AuthenticationRequest("user", "password");

        // Convert request to JSON
        String loginRequestJson = objectMapper.writeValueAsString(loginRequest);

        MockHttpServletRequestBuilder request =
                MockMvcRequestBuilders.post("/authenticate")
                        .content(loginRequestJson).contentType(MediaType.APPLICATION_JSON);

        // Perform the POST request and capture the result
        String jwtToken = mockMvc.perform(request).andExpect(status().isOk()).andReturn().getResponse().getContentAsString();

        return objectMapper.readValue(jwtToken, AuthenticationResponse.class).jwt();
    }
}