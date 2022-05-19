package com.vmo.core.modules.models.integration.auth.responses.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.vmo.core.modules.models.integration.auth.responses.user.details.StorefrontDetailResponse;
import com.vmo.core.modules.models.integration.auth.responses.user.details.PartnerUserResponse;
import com.vmo.core.modules.models.integration.auth.responses.user.details.UserAccountResponse;
import com.vmo.core.modules.models.integration.auth.responses.user.details.UserStatus;
import lombok.Getter;
import lombok.Setter;
import org.springframework.lang.Nullable;

public class UserDetailFullResponse {
    private String provider;
    @JsonProperty("os_type")
    private String osType;
    @JsonProperty("_id")
    private String id;
    @Getter @Setter
    private UserStatus status;
    private String email;
    private String role;
    @JsonProperty("display_name")
    private String displayName;
    @JsonProperty("user_name")
    @Getter @Setter
    private String username;
    //TODO: check response changes by auth service
    @Nullable
    @JsonProperty("account")
    private UserAccountResponse account;
    @Nullable
    private PartnerUserResponse partner;
    @Nullable
    private StorefrontDetailResponse storefront;
    private String gravatar;
    @JsonProperty(value = "first_name")
    private String firstName;
    @JsonProperty(value = "last_name")
    private String lastName;

    public String getProvider() {
        return provider;
    }

    public void setProvider(String provider) {
        this.provider = provider;
    }

    public String getOsType() {
        return osType;
    }

    public void setOsType(String osType) {
        this.osType = osType;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    @Nullable
    public UserAccountResponse getAccount() {
        return account;
    }

    public void setAccount(@Nullable UserAccountResponse account) {
        this.account = account;
    }

    @Nullable
    public PartnerUserResponse getPartner() {
        return partner;
    }

    public void setPartner(@Nullable PartnerUserResponse partner) {
        this.partner = partner;
    }

    @Nullable
    public StorefrontDetailResponse getStorefront() {
        return storefront;
    }

    public void setStorefront(@Nullable StorefrontDetailResponse storefront) {
        this.storefront = storefront;
    }

    public String getGravatar() {
        return gravatar;
    }

    public void setGravatar(String gravatar) {
        this.gravatar = gravatar;
    }

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

}
