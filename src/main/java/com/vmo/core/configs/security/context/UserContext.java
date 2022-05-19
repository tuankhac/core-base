package com.vmo.core.configs.security.context;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.vmo.core.modules.models.database.types.user.UserRoleType;
import lombok.Data;
import org.apache.commons.collections4.CollectionUtils;

import java.util.List;

@Data
public class UserContext {

    private boolean isAnonymous;

    private String token;

    private String id;

    private String email;

    private List<UserRoleType> userRoles;

    private String partnerId;

    private String wholesalerId;
    //    @Getter @Setter
//    private String onlineStoreId;

    private String storefrontId;

    public UserContext() {
        isAnonymous = true;
    }

    public UserContext(String token) {
        isAnonymous = false;
        this.token = token;
    }

    @JsonIgnore
    public UserRoleType getPrimaryRole() {
        if (CollectionUtils.isNotEmpty(userRoles)) {
            return userRoles.get(0);
        }
        return null;
    }
}
