package com.vmo.core.modules.models.integration.auth.responses.user.details;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class UserStatus {
    private UserStatusActive active;
    @JsonProperty("is_lock")
    private Boolean isLock;
}
