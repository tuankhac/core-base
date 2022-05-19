package com.vmo.core.modules.models.integration.auth.responses.user.details;

import com.fasterxml.jackson.annotation.JsonProperty;

public class UserAccountResponse {
    @JsonProperty("first_name")
    private String firstName;
    @JsonProperty("last_name")
    private String lastName;
    private String address;
    private String state;
    private String city;
    @JsonProperty("zip_code")
    private String zipCode;
    private String country;
    @JsonProperty("phone")
    private String phoneNumber;
    private String avatar;
    @JsonProperty("paypal_email")
    private String paypalEmail;
    @JsonProperty(value = "shipping_address")
    private AddressAccount shippingAddress;
    @JsonProperty(value = "binding_address")
    private AddressAccount bindingAddress;

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getPaypalEmail() {
        return paypalEmail;
    }

    public void setPaypalEmail(String paypalEmail) {
        this.paypalEmail = paypalEmail;
    }

    public AddressAccount getShippingAddress() {
        return shippingAddress;
    }

    public void setShippingAddress(AddressAccount shippingAddress) {
        this.shippingAddress = shippingAddress;
    }

    public AddressAccount getBindingAddress() {
        return bindingAddress;
    }

    public void setBindingAddress(AddressAccount bindingAddress) {
        this.bindingAddress = bindingAddress;
    }
}
