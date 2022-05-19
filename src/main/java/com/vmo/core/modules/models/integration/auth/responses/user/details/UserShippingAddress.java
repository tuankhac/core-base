package com.vmo.core.modules.models.integration.auth.responses.user.details;

import com.fasterxml.jackson.annotation.JsonProperty;

public class UserShippingAddress {
    private String address;
    private String city;
    private String state;
    @JsonProperty("zip_code")
    private String zipCode;

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }
}
