package com.vmo.core.modules.models.integration.auth.responses.gmap;

import com.vmo.core.modules.models.integration.auth.responses.gmap.details.ResultsResponse;

import java.util.List;

public class GoogleServiceResponse {
    private List<ResultsResponse> results;
    private String status;

    public List<ResultsResponse> getResults() {
        return results;
    }

    public void setResults(List<ResultsResponse> results) {
        this.results = results;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
