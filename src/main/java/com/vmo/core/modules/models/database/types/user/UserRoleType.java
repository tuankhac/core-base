package com.vmo.core.modules.models.database.types.user;

import lombok.Getter;

public enum UserRoleType {
    ROOT("root"),
    ADMIN("admin"),
    EMPLOYEE("employee"), //v1 role
    MANAGER("manager"), //v1 role
    PERSONAL("personal"),//// buyer & seller
    WHOLESALER("wholesaler"),
    MARKETING_REP("marketing_rep"), //v1 role
//    WAREHOUSE_EMPLOYEE("warehouse_employee"),
    ONLINE_SALES_REP("online_sales_rep"), //v1 role
    ROAD_SALES_REP("road_sales_rep"), //v1 role
    CUSTOMER_SERVICE_REP("customer_service_rep"), //v1 role
    PRODUCT_MANAGER("product_manager"), //v1 role
    EXECUTIVE_STAFF("executive_staff"), //v1 role
    WAREHOUSE_ADMIN("warehouse_admin"), //v1 role
    WAREHOUSE_STAFF("warehouse_staff"), //v1 role
    WAREHOUSE_MANAGER("warehouse_manager"), //v1 role
//    PARTNER_ADMIN("partner_admin"),
//    PARTNER_MANAGER("partner_manager"),
//    PARTNER_EMPLOYEE("partner_employee"),
//    ONLINE_STORE("online_store"),

    //both partner or online store
    SHOP_ADMIN("user_administrator"),
    SHOP_MANAGER("user_manager"),
    SHOP_EMPLOYEE("user_employee"),
    //end

    CUSTOMER("customer");

    @Getter
    private final String value;

    UserRoleType(String value) {
        this.value = value;
    }

    public static UserRoleType findByValue(String value) {
        if (value == null) return null;

        for (UserRoleType role : UserRoleType.values()) {
            if (role.getValue().equals(value)) {
                return role;
            }
        }

        return null;
    }
}
