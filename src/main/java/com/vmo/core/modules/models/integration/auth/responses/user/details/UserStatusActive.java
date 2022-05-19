package com.vmo.core.modules.models.integration.auth.responses.user.details;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class UserStatusActive {
    @JsonProperty("is_active")
    private Boolean isActive;
}
