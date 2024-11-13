package com.forge.sslsj.payload;

import lombok.Data;

@Data
public class PersonResponse {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private String address;
    private String city;
    private String state;
    private String country;
    private String postalCode;
    // Add more fields if needed
}