package com.vmo.core.common.utils;

import com.vmo.core.configs.security.context.UserContext;
import com.vmo.core.modules.models.database.types.user.UserRoleType;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SecurityUtils {
    private static final List<UserRoleType> adminRoles;
    private static final List<UserRoleType> shopRoles;
    private static final List<UserRoleType> p2pRoles;

    static {
        adminRoles = new ArrayList<>();
        adminRoles.add(UserRoleType.ROOT);
        adminRoles.add(UserRoleType.ADMIN);
        adminRoles.add(UserRoleType.MANAGER);
        adminRoles.add(UserRoleType.EMPLOYEE);
        adminRoles.add(UserRoleType.MARKETING_REP);
//        adminRoles.add(UserRoleType.WAREHOUSE_EMPLOYEE);
        adminRoles.add(UserRoleType.ONLINE_SALES_REP);
        adminRoles.add(UserRoleType.ROAD_SALES_REP);
        adminRoles.add(UserRoleType.CUSTOMER_SERVICE_REP);
        adminRoles.add(UserRoleType.PRODUCT_MANAGER);
        adminRoles.add(UserRoleType.EXECUTIVE_STAFF);
        adminRoles.add(UserRoleType.WAREHOUSE_ADMIN);
        adminRoles.add(UserRoleType.WAREHOUSE_STAFF);
        adminRoles.add(UserRoleType.WAREHOUSE_MANAGER);
    }

    static {
        shopRoles = new ArrayList<>();
        shopRoles.add(UserRoleType.SHOP_ADMIN);
        shopRoles.add(UserRoleType.SHOP_MANAGER);
        shopRoles.add(UserRoleType.SHOP_EMPLOYEE);
    }

    static {
        p2pRoles = new ArrayList<>();
        p2pRoles.add(UserRoleType.PERSONAL);
        //online store or partner
        p2pRoles.add(UserRoleType.SHOP_ADMIN);
        p2pRoles.add(UserRoleType.SHOP_MANAGER);
        p2pRoles.add(UserRoleType.SHOP_EMPLOYEE);
    }

    public static String getUserToken() {
        return ((UserContext) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getToken();
    }

    public static boolean isLogined() {
        return SecurityContextHolder.getContext() != null &&
                SecurityContextHolder.getContext().getAuthentication() != null &&
                SecurityContextHolder.getContext().getAuthentication().getPrincipal() != null &&
                SecurityContextHolder.getContext().getAuthentication().getPrincipal() instanceof UserContext &&
                ((UserContext) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId() != null;
    }

    public static String getUserLogin() {
        if (isLogined()) {
            return ((UserContext) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId();
        }
        return null;
    }

    public static String getPartnerId() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal == null) return null;

        return ((UserContext) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getPartnerId();
    }

    public static String getStorefrontId() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal == null || !(principal instanceof UserContext)) return null;

        return ((UserContext) principal).getStorefrontId();
    }

    public static boolean isPartner() {
        return checkHaveAnyRole(shopRoles)
                && StringUtils.isNotBlank(getPartnerId());
    }

    public static boolean isOnlineStore() {
        return checkHaveAnyRole(shopRoles)
                && StringUtils.isNotBlank(getStorefrontId());
    }

    public static UserRoleType getUserPrimaryRole() {
        if (SecurityContextHolder.getContext() != null
                && SecurityContextHolder.getContext().getAuthentication() != null
                && SecurityContextHolder.getContext().getAuthentication().getPrincipal() != null
                && SecurityContextHolder.getContext().getAuthentication().getPrincipal() instanceof UserContext
        ) {
            List<UserRoleType> roles =
                    ((UserContext) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUserRoles();
            if (CollectionUtils.isNotEmpty(roles)) {
                return roles.get(0);
            }
        }
        return null;
    }

    public static List<UserRoleType> getUserRoles() {
        if (SecurityContextHolder.getContext() != null
                && SecurityContextHolder.getContext().getAuthentication() != null
                && SecurityContextHolder.getContext().getAuthentication().getPrincipal() != null
                && SecurityContextHolder.getContext().getAuthentication().getPrincipal() instanceof UserContext
        ) {
            return ((UserContext) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUserRoles();
        }
        return null;
    }


    public static boolean checkHaveAnyRole(List<UserRoleType> checkRoles) {
        return checkHaveAnyRole(getUserRoles(), checkRoles);
    }

    public static boolean checkHaveAnyRole(List<UserRoleType> userRoles, List<UserRoleType> checkRoles) {
        if (userRoles == null || userRoles.size() < 1) {
            return false;
        }
        if (checkRoles == null || checkRoles.size() < 1) {
            return true;
        }
        for (UserRoleType role : checkRoles) {
            if (checkHaveRole(userRoles, role)) {
                return true;
            }
        }
        return false;
    }

    public static boolean checkHaveRole(UserRoleType checkRole) {
        return checkHaveRole(getUserRoles(), checkRole);
    }

    public static boolean checkHaveRole(List<UserRoleType> userRoles, UserRoleType checkRole) {
        if (userRoles == null || userRoles.size() < 1) {
            return false;
        }
        if (checkRole == null) {
            return true;
        }
        return userRoles.contains(checkRole);
    }

    public static boolean checkRoleAccessFromAdmin(List<UserRoleType> roles) {
        return roles != null && roles.size() > 0 && (roles.stream().anyMatch(r -> adminRoles.contains(r)));
    }

    public static boolean hasP2pPermission() {
        return checkHaveAnyRole(p2pRoles);
    }

    //buyer BBB market
    public static boolean hasBuyerPermission() { //currently same as p2p
        return checkHaveAnyRole(Collections.singletonList(UserRoleType.PERSONAL))
                || (isPartner() && !isOnlineStore());
    }
}
