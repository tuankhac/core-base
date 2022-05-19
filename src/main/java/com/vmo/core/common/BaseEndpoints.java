package com.vmo.core.common;

import com.vmo.core.configs.EndpointConfig;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class BaseEndpoints {
    @Autowired
    public BaseEndpoints(EndpointConfig endpointConfig) {
        BASE_SERVICE = endpointConfig.getBaseApiUrl();
        BASE_SERVICE_ML = endpointConfig.getBaseValueGuideApiUrl();

        CORE_API_PREFIX = endpointConfig.getCoreApiPrefix() != null ? endpointConfig.getCoreApiPrefix() : CORE_API_PREFIX;
        AUTHENTICATION_API_PREFIX = endpointConfig.getAuthApiPrefix() != null ? endpointConfig.getAuthApiPrefix() : AUTHENTICATION_API_PREFIX;
        BILLING_API_PREFIX = endpointConfig.getBillingApiPrefix() != null ? endpointConfig.getBillingApiPrefix() : BILLING_API_PREFIX;
        NOTIFY_API_PREFIX = endpointConfig.getNotifyApiPrefix() != null ? endpointConfig.getNotifyApiPrefix() : NOTIFY_API_PREFIX;
        SUBSCRIPTION_API_PREFIX = endpointConfig.getSubscriptionApiPrefix() != null ? endpointConfig.getSubscriptionApiPrefix() : SUBSCRIPTION_API_PREFIX;
        VALUE_GUIDE_API_PREFIX = endpointConfig.getValueGuideApiPrefix() != null ? endpointConfig.getValueGuideApiPrefix() : VALUE_GUIDE_API_PREFIX;
        SHIPMENT_API_PREFIX = endpointConfig.getShipmentApiPrefix() != null ? endpointConfig.getShipmentApiPrefix() : SHIPMENT_API_PREFIX;
        SUPPORT_API_PREFIX = endpointConfig.getSupportApiPrefix() != null ? endpointConfig.getSupportApiPrefix() : SUPPORT_API_PREFIX;
        TRADE_CREDIT_PREFIX = endpointConfig.getTradeCreditApiPrefix() != null ? endpointConfig.getTradeCreditApiPrefix() : TRADE_CREDIT_PREFIX;

        BASE_WEB_APP = endpointConfig.getBaseWebappUrl() != null ? endpointConfig.getBaseWebappUrl() : BASE_WEB_APP;
    }

    //region Other BBB micro services
    private static String BASE_SERVICE = "https://api-dev.bicyclebluebook.com";
    private static String BASE_SERVICE_ML = "https://api-staging.bicyclebluebook.com";
    private static String CORE_API_PREFIX = "";
    private static String AUTHENTICATION_API_PREFIX = "";
    private static String BILLING_API_PREFIX = "";
    private static String NOTIFY_API_PREFIX = "";
    private static String SUBSCRIPTION_API_PREFIX = "";
    private static String VALUE_GUIDE_API_PREFIX = "";
    private static String SHIPMENT_API_PREFIX = "";
    private static String SUPPORT_API_PREFIX = "";
    private static String TRADE_CREDIT_PREFIX = "";

    public static String getBaseUrlCore() {
        return BASE_SERVICE + CORE_API_PREFIX;
    }

    public static String getBaseUrlAuthentication() {
        return BASE_SERVICE + AUTHENTICATION_API_PREFIX;
    }

    public static String getBaseUrlBilling() {
        return BASE_SERVICE + BILLING_API_PREFIX;
    }

    public static String getBaseUrlNotify() {
        return BASE_SERVICE + NOTIFY_API_PREFIX;
    }

    public static String getBaseUrlSubscription() {
        return BASE_SERVICE + SUBSCRIPTION_API_PREFIX;
    }

    public static String getBaseUrlValueGuideV2() {
        return BASE_SERVICE_ML + VALUE_GUIDE_API_PREFIX;
//        return "http://54.146.28.203:8888";
    }

    public static String getBaseUrlShipment() {
        return BASE_SERVICE + SHIPMENT_API_PREFIX;
//        return "http://localhost:8080";
    }

    public static String getBaseUrlSupport() {
        return BASE_SERVICE + SUPPORT_API_PREFIX;
    }

    public static String getBaseUrlTradeCredit() {
        return BASE_SERVICE + TRADE_CREDIT_PREFIX;
    }


    public static String BASE_WEB_APP = "";


    public static final String V1 = "/v1";
    //endregion


    //region Common
    //for more than one service
    protected static final String API = "/api";
    public static final String ID = "id";
    protected static final String ID_PATH = "/{" + ID + "}";
    protected static final String PRIVATE = "/private";
    protected static final String PRIVATE_API = PRIVATE + API;

    protected static final String COMMON = "/common";
    protected static final String CONFIG = "/config";

    //base
    protected static final String LOG = "/log";
    protected static final String RELOAD = "/reload";


    //web
    protected static final String WEB_MARKET_PLACE = "/marketplace";
    protected static final String BUY_NOW = "/buy-now";
    protected static final String SCORECARD_HISTORY = "/trade-in-account/trade-in/history";
    protected static final String WEB_STOREFRONT = "/store-front";
    protected static final String SELLER = "/seller";
    protected static final String WEB_ONLINE_STORE = "/online-store";
    protected static final String HEALTH = "/health";
    protected static final String KUBE_START_UP = "/kube-startup";
    protected static final String KUBE_LIVENESS = "/kube-liveness";
    protected static final String KUBE_READINESS = "/kube-readiness";
    //endregion

    public static final String LOG_PUBLIC = API + LOG;
    public static final String URL_CHECK_STATUS_CONNECTION_START_UP = API + V1 + HEALTH + KUBE_START_UP;
    public static final String URL_CHECK_STATUS_CONNECTION_LIVENESS = API + V1 + HEALTH + KUBE_LIVENESS;
    public static final String URL_CHECK_STATUS_CONNECTION_READINESS = API + V1 + HEALTH + KUBE_READINESS;

    public static final String[] URL_CHECK_STATUS_CONNECTION = new String[]{
            URL_CHECK_STATUS_CONNECTION_START_UP,
            URL_CHECK_STATUS_CONNECTION_LIVENESS,
            URL_CHECK_STATUS_CONNECTION_READINESS
    };

    //region web
    public static String getWebListingDetail(long masterListingId) {
        return BASE_WEB_APP + WEB_MARKET_PLACE + BUY_NOW + "/" + masterListingId;
    }

    public static String getWebListingStore(String storefrontId, String userId) {
        return StringUtils.isBlank(storefrontId)
                ? BASE_WEB_APP + WEB_MARKET_PLACE + SELLER + "/" + userId
                : BASE_WEB_APP + WEB_MARKET_PLACE + WEB_ONLINE_STORE + "/" + storefrontId;
    }

    public static String getLinkScorecardHistory() {
        return BASE_WEB_APP + SCORECARD_HISTORY;
    }
    //endregion

    //region job
    public static final String SHUTDOWN = "/shutdown";
    public static final String JOB = "/job";
    public static final String RUNNING = "/running";
    public static final String INQUEUE = "/inqueue";
    public static final String PURGE = "/purge";
    public static final String CONFIGURATION = "/configuration";
    public static final String URL_SAFE_SHUTDOWN = PRIVATE_API + SHUTDOWN;
    public static final String URL_JOB_CONFIGS = API + JOB + CONFIGURATION;
    public static final String URL_JOB_RUNNING = API + JOB + RUNNING;
    public static final String URL_JOB_INQUEUE = API + JOB + INQUEUE;
    public static final String URL_JOB_CONFIG = API + JOB + CONFIGURATION + ID_PATH;
    public static final String URL_UPDATE_JOB_CONFIG = PRIVATE_API + JOB + CONFIGURATION + ID_PATH;
    public static final String URL_JOB_LOGS = API + JOB + LOG;
    public static final String URL_JOB_LOGS_PURGE = API + JOB + LOG + PURGE;
    //endregion
}
