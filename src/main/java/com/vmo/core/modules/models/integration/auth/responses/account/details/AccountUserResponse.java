package com.vmo.core.modules.models.integration.auth.responses.account.details;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.vmo.core.modules.models.database.types.user.UserRoleType;
import com.vmo.core.modules.models.integration.auth.responses.user.details.UserStatus;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.collections4.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

public class AccountUserResponse {
    @JsonProperty("_id")
    @Getter @Setter
    private String id;
    @Getter @Setter
    private UserStatus status;
    @Getter
    private UserRoleType role;
    @Getter
    private List<UserRoleType> roles;
    @Getter @Setter
    private String email;
    @JsonProperty("display_name")
    @Getter @Setter
    private String displayName;

    @JsonProperty("role")
    public void setRole(String role) {
        this.role = UserRoleType.findByValue(role);
    }

    @JsonProperty("roles")
    public void setRoles(List<String> roles) {
        if (CollectionUtils.isNotEmpty(roles)) {
            this.roles = new ArrayList<>();
            for (String s : roles) {
                UserRoleType role = UserRoleType.findByValue(s);
                if (role != null) {
                    this.roles.add(role);
                }
            }
        }
    }
}
