package com.spring.azure.springazurecloud.models.client;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.persistence.Embeddable;

@Embeddable
@Data
public class Address {
    private String city;
    private String country;
    private String state;


    private long postalCode;
}
