package com.vmo.core.modules.models.integration.auth.responses.user.details;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class PartnerUserResponse {
    @JsonProperty("_id")
    private String id;
    @JsonProperty("smart_tailing")
    private Boolean smartTailing;
    @JsonProperty("mailing_address")
    private Boolean mailingAddress;
    private List<String> staffs;
    private String name;
    private String address;
    private String city;
    private String state;
    @JsonProperty("zip_code")
    private String zipCode;
    private String lat;
    private String lng;
    private String phone;
    private String website;
    @JsonProperty("tell_about")
    private String tellAbout;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Boolean getSmartTailing() {
        return smartTailing;
    }

    public void setSmartTailing(Boolean smartTailing) {
        this.smartTailing = smartTailing;
    }

    public Boolean getMailingAddress() {
        return mailingAddress;
    }

    public void setMailingAddress(Boolean mailingAddress) {
        this.mailingAddress = mailingAddress;
    }

    public List<String> getStaffs() {
        return staffs;
    }

    public void setStaffs(List<String> staffs) {
        this.staffs = staffs;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

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

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLng() {
        return lng;
    }

    public void setLng(String lng) {
        this.lng = lng;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getTellAbout() {
        return tellAbout;
    }

    public void setTellAbout(String tellAbout) {
        this.tellAbout = tellAbout;
    }
}
