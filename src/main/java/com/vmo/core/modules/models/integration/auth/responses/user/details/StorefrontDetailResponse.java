package com.vmo.core.modules.models.integration.auth.responses.user.details;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class StorefrontDetailResponse {
    @JsonProperty("_id")
    private String id;
    private String email;
    private String name;
    private String phone;
    @JsonProperty("shipping_address")
    private UserShippingAddress shippingAddress;
    private String logo;
}
