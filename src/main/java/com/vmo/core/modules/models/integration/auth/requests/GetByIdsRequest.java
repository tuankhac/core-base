package com.vmo.core.modules.models.integration.auth.requests;

import lombok.Data;

import java.util.List;

@Data
public class GetByIdsRequest {
    private List<String> ids;

    public GetByIdsRequest() {
    }

    public GetByIdsRequest(List<String> ids) {
        this.ids = ids;
    }
}
