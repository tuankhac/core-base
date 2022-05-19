package com.vmo.core.modules.models.integration.auth.responses.account;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class CustomerDetailResponse {
    @JsonProperty("_id")
    private String id;
    @JsonProperty("first_name")
    private String firstName;
    @JsonProperty("last_name")
    private String lastName;
    private String phone;
    private String address;
    private String city;
    private String state;
    @JsonProperty("zip_code")
    private String zipCode;
    private String country;
    private String email;
    @JsonProperty("token_email")
    private String tokenEmail;
    private String avatar;
    @JsonProperty("paypal_email")
    private String paypalEmail;
}
