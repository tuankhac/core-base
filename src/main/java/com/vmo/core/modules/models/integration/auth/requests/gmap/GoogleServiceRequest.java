package com.vmo.core.modules.models.integration.auth.requests.gmap;

public class GoogleServiceRequest {
    private String category;
    private String data;

    public GoogleServiceRequest() {}

    public GoogleServiceRequest(String category, String data) {
        this.category = category;
        this.data = data;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
