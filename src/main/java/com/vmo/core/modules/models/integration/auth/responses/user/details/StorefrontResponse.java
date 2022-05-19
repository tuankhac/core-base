package com.vmo.core.modules.models.integration.auth.responses.user.details;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class StorefrontResponse {
    @JsonProperty("_id")
    private String id;
    private String name;
    private String logo;
}
