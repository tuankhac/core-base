package com.vmo.core;

import com.vmo.core.common.CommonConstants;
import com.vmo.core.configs.env.Environments;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
@ConfigurationProperties(prefix = CommonConstants.CONFIG_PREFIX)
public class AppConfig {
    private Environments env;
    private Boolean enableServiceLog;
    private Boolean logApiException;
    private EndpointConfig endpoint;
    private EbayConfig ebay;
    private String bbbSellerId;  //display name: BicycleBlueBook
    private String bbbStorefrontId;
    @Getter @Setter
    private String wholesaleStorefrontId;
    @Getter @Setter
    private String kidsSuperstoreId;
    private String bbbPaypalEmail;
    private SlackWebhook slackWebhook;
    private IntegratedServicesConfig integratedServicesConfig;
    private List<String> tradeInAdminEmails;
    private String trekPartnerParentId;
    private List<String> dropOffAdminEmails;
    private List<String> trekAdminEmails;
    @Getter
    @Setter
    private List<String> pricingEngineEmails;
    @Getter
    @Setter
    private List<String> openTermsDropShipEmails;
    @Getter
    @Setter
    private List<String> multiListingSaleEmails;
    @Getter
    @Setter
    private List<String> supportEmails;
    @Getter
    @Setter
    private List<String> dailyReportEmails;
    @Getter
    @Setter
    private ConnectAppSalesforce connectAppSalesforce;
    @Getter @Setter
    private String secretKey;

    public Environments getEnv() {
        return env;
    }

    public void setEnv(Environments env) {
        this.env = env;
    }

    public Boolean getEnableServiceLog() {
        return enableServiceLog;
    }

    public void setEnableServiceLog(Boolean enableServiceLog) {
        this.enableServiceLog = enableServiceLog;
    }

    public Boolean getLogApiException() {
        return logApiException;
    }

    public void setLogApiException(Boolean logApiException) {
        this.logApiException = logApiException;
    }

    public EndpointConfig getEndpoint() {
        return endpoint;
    }

    public void setEndpoint(EndpointConfig endpoint) {
        this.endpoint = endpoint;
    }

    public EbayConfig getEbay() {
        return ebay;
    }

    public void setEbay(EbayConfig ebay) {
        this.ebay = ebay;
    }

    public String getBbbSellerId() {
        return bbbSellerId;
    }

    public void setBbbSellerId(String bbbSellerId) {
        this.bbbSellerId = bbbSellerId;
    }

    public String getBbbStorefrontId() {
        return bbbStorefrontId;
    }

    public void setBbbStorefrontId(String bbbStorefrontId) {
        this.bbbStorefrontId = bbbStorefrontId;
    }

    public String getBbbPaypalEmail() {
        return bbbPaypalEmail;
    }

    public void setBbbPaypalEmail(String bbbPaypalEmail) {
        this.bbbPaypalEmail = bbbPaypalEmail;
    }

    public SlackWebhook getSlackWebhook() {
        return slackWebhook;
    }

    public void setSlackWebhook(SlackWebhook slackWebhook) {
        this.slackWebhook = slackWebhook;
    }

    public IntegratedServicesConfig getIntegratedServicesConfig() {
        return integratedServicesConfig;
    }

    public void setIntegratedServicesConfig(IntegratedServicesConfig integratedServicesConfig) {
        this.integratedServicesConfig = integratedServicesConfig;
    }

    public List<String> getTradeInAdminEmails() {
        return tradeInAdminEmails;
    }

    public void setTradeInAdminEmails(List<String> tradeInAdminEmails) {
        this.tradeInAdminEmails = tradeInAdminEmails;
    }

    public String getTrekPartnerParentId() {
        return trekPartnerParentId;
    }

    public void setTrekPartnerParentId(String trekPartnerParentId) {
        this.trekPartnerParentId = trekPartnerParentId;
    }

    public List<String> getDropOffAdminEmails() {
        return dropOffAdminEmails;
    }

    public void setDropOffAdminEmails(List<String> dropOffAdminEmails) {
        this.dropOffAdminEmails = dropOffAdminEmails;
    }

    public List<String> getTrekAdminEmails() {
        return trekAdminEmails;
    }

    public void setTrekAdminEmails(List<String> trekAdminEmails) {
        this.trekAdminEmails = trekAdminEmails;
    }

    public static class IntegratedServicesConfig {
        private float recommendationBicycleMasterListingTimeout;
        private float saveSearchTimeout;

        public float getRecommendationBicycleMasterListingTimeout() {
            return recommendationBicycleMasterListingTimeout;
        }

        public void setRecommendationBicycleMasterListingTimeout(float recommendationBicycleMasterListingTimeout) {
            this.recommendationBicycleMasterListingTimeout = recommendationBicycleMasterListingTimeout;
        }

        public float getSaveSearchTimeout() {
            return saveSearchTimeout;
        }

        public void setSaveSearchTimeout(float saveSearchTimeout) {
            this.saveSearchTimeout = saveSearchTimeout;
        }
    }

    public static class EndpointConfig {
        private String baseApiUrl;
        private String baseCoreApiUrl;
        private String baseValueGuideApiUrl;
        private String coreApiPrefix;
        private String authApiPrefix;
        private String billingApiPrefix;
        private String notifyApiPrefix;
        private String subscriptionApiPrefix;
        private String shipmentApiPrefix;
        private String valueGuideApiPrefix;
        private String baseWebappUrl;
        private String salesforceApiUrl;
        private String supportApiPrefix;
        private String tradeCreditApiPrefix;
        private String elasticSearchApiUrl;
        private String bicycleElasticSearch;
        private String tokenAuthen;
        private String masterListingElasticSearch;
        private String baseUrlRecommendation;
        private String importBicycleClassify;
        private String buyLocalNowUrl;
        private String baseAttentiveUrl;

        public String getBaseApiUrl() {
            return baseApiUrl;
        }

        public void setBaseApiUrl(String baseApiUrl) {
            this.baseApiUrl = baseApiUrl;
        }

        public String getBaseCoreApiUrl() {
            return baseCoreApiUrl;
        }

        public void setBaseCoreApiUrl(String baseCoreApiUrl) {
            this.baseCoreApiUrl = baseCoreApiUrl;
        }

        public String getBaseValueGuideApiUrl() {
            return baseValueGuideApiUrl;
        }

        public void setBaseValueGuideApiUrl(String baseValueGuideApiUrl) {
            this.baseValueGuideApiUrl = baseValueGuideApiUrl;
        }

        public String getCoreApiPrefix() {
            return coreApiPrefix;
        }

        public void setCoreApiPrefix(String coreApiPrefix) {
            this.coreApiPrefix = coreApiPrefix;
        }

        public String getAuthApiPrefix() {
            return authApiPrefix;
        }

        public void setAuthApiPrefix(String authApiPrefix) {
            this.authApiPrefix = authApiPrefix;
        }

        public String getBillingApiPrefix() {
            return billingApiPrefix;
        }

        public void setBillingApiPrefix(String billingApiPrefix) {
            this.billingApiPrefix = billingApiPrefix;
        }

        public String getNotifyApiPrefix() {
            return notifyApiPrefix;
        }

        public void setNotifyApiPrefix(String notifyApiPrefix) {
            this.notifyApiPrefix = notifyApiPrefix;
        }

        public String getSubscriptionApiPrefix() {
            return subscriptionApiPrefix;
        }

        public void setSubscriptionApiPrefix(String subscriptionApiPrefix) {
            this.subscriptionApiPrefix = subscriptionApiPrefix;
        }

        public String getShipmentApiPrefix() {
            return shipmentApiPrefix;
        }

        public void setShipmentApiPrefix(String shipmentApiPrefix) {
            this.shipmentApiPrefix = shipmentApiPrefix;
        }

        public String getValueGuideApiPrefix() {
            return valueGuideApiPrefix;
        }

        public void setValueGuideApiPrefix(String valueGuideApiPrefix) {
            this.valueGuideApiPrefix = valueGuideApiPrefix;
        }

        public String getBaseWebappUrl() {
            return baseWebappUrl;
        }

        public void setBaseWebappUrl(String baseWebappUrl) {
            this.baseWebappUrl = baseWebappUrl;
        }

        public String getSalesforceApiUrl() {
            return salesforceApiUrl;
        }

        public void setSalesforceApiUrl(String salesforceApiUrl) {
            this.salesforceApiUrl = salesforceApiUrl;
        }

        public String getSupportApiPrefix() {
            return supportApiPrefix;
        }

        public void setSupportApiPrefix(String supportApiPrefix) {
            this.supportApiPrefix = supportApiPrefix;
        }

        public String getTradeCreditApiPrefix() {
            return tradeCreditApiPrefix;
        }

        public void setTradeCreditApiPrefix(String tradeCreditApiPrefix) {
            this.tradeCreditApiPrefix = tradeCreditApiPrefix;
        }

        public String getElasticSearchApiUrl() {
            return elasticSearchApiUrl;
        }

        public void setElasticSearchApiUrl(String elasticSearchApiUrl) {
            this.elasticSearchApiUrl = elasticSearchApiUrl;
        }

        public String getBicycleElasticSearch() {
            return bicycleElasticSearch;
        }

        public void setBicycleElasticSearch(String bicycleElasticSearch) {
            this.bicycleElasticSearch = bicycleElasticSearch;
        }

        public String getMasterListingElasticSearch() {
            return masterListingElasticSearch;
        }

        public void setMasterListingElasticSearch(String masterListingElasticSearch) {
            this.masterListingElasticSearch = masterListingElasticSearch;
        }

        public String getBaseUrlRecommendation() {
            return baseUrlRecommendation;
        }

        public void setBaseUrlRecommendation(String baseUrlRecommendation) {
            this.baseUrlRecommendation = baseUrlRecommendation;
        }

        public String getTokenAuthen() {
            return tokenAuthen;
        }

        public void setTokenAuthen(String tokenAuthen) {
            this.tokenAuthen = tokenAuthen;
        }

        public String getImportBicycleClassify() {
            return importBicycleClassify;
        }

        public void setImportBicycleClassify(String importBicycleClassify) {
            this.importBicycleClassify = importBicycleClassify;
        }

        public String getBuyLocalNowUrl() {
            return buyLocalNowUrl;
        }

        public void setBuyLocalNowUrl(String buyLocalNowUrl) {
            this.buyLocalNowUrl = buyLocalNowUrl;
        }

        public String getBaseAttentiveUrl() {
            return baseAttentiveUrl;
        }

        public void setBaseAttentiveUrl(String baseAttentiveUrl) {
            this.baseAttentiveUrl = baseAttentiveUrl;
        }
    }

    public static class EbayConfig {
        private String endpoint;
        private boolean isSandbox;
        private String linkViewItem;
        private String countdownApiUrl;
        private String countdownApiEbayDomain;

        public String getEndpoint() {
            return endpoint;
        }

        public void setEndpoint(String endpoint) {
            this.endpoint = endpoint;
        }

        public boolean isSandbox() {
            return isSandbox;
        }

        public void setSandbox(boolean sandbox) {
            isSandbox = sandbox;
        }

        public String getLinkViewItem() {
            return linkViewItem;
        }

        public void setLinkViewItem(String linkViewItem) {
            this.linkViewItem = linkViewItem;
        }

        public String getCountdownApiUrl() {
            return countdownApiUrl;
        }

        public void setCountdownApiUrl(String countdownApiUrl) {
            this.countdownApiUrl = countdownApiUrl;
        }

        public String getCountdownApiEbayDomain() {
            return countdownApiEbayDomain;
        }

        public void setCountdownApiEbayDomain(String countdownApiEbayDomain) {
            this.countdownApiEbayDomain = countdownApiEbayDomain;
        }
    }

    public static class SlackWebhook {
        private Boolean isEnable;
        private String webhookUrl;
        //instant alert if errors exceed this limit
        private Integer clientErrorAlert;
        private Integer serverErrorAlert;
        //if doesnt exceed, send notification for each cycle of:
        private Integer notificationCycleSeconds;
        private Integer delaySecondsErrorAlert;

        public Boolean getEnable() {
            return isEnable;
        }

        public void setEnable(Boolean enable) {
            isEnable = enable;
        }

        public String getWebhookUrl() {
            return webhookUrl;
        }

        public void setWebhookUrl(String webhookUrl) {
            this.webhookUrl = webhookUrl;
        }

        public Integer getClientErrorAlert() {
            return clientErrorAlert;
        }

        public void setClientErrorAlert(Integer clientErrorAlert) {
            this.clientErrorAlert = clientErrorAlert;
        }

        public Integer getServerErrorAlert() {
            return serverErrorAlert;
        }

        public void setServerErrorAlert(Integer serverErrorAlert) {
            this.serverErrorAlert = serverErrorAlert;
        }

        public Integer getNotificationCycleSeconds() {
            return notificationCycleSeconds;
        }

        public void setNotificationCycleSeconds(Integer notificationCycleSeconds) {
            this.notificationCycleSeconds = notificationCycleSeconds;
        }

        public Integer getDelaySecondsErrorAlert() {
            return delaySecondsErrorAlert;
        }

        public void setDelaySecondsErrorAlert(Integer delaySecondsErrorAlert) {
            this.delaySecondsErrorAlert = delaySecondsErrorAlert;
        }
    }

    public static class ConnectAppSalesforce {
        private String grantType;
        private String clientId;
        private String clientSecret;
        private String username;
        private String password;
        private String receiveNotificationUsername;
        private String receiveNotificationPassword;

        public String getGrantType() {
            return grantType;
        }

        public void setGrantType(String grantType) {
            this.grantType = grantType;
        }

        public String getClientId() {
            return clientId;
        }

        public void setClientId(String clientId) {
            this.clientId = clientId;
        }

        public String getClientSecret() {
            return clientSecret;
        }

        public void setClientSecret(String clientSecret) {
            this.clientSecret = clientSecret;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public String getReceiveNotificationUsername() {
            return receiveNotificationUsername;
        }

        public void setReceiveNotificationUsername(String receiveNotificationUsername) {
            this.receiveNotificationUsername = receiveNotificationUsername;
        }

        public String getReceiveNotificationPassword() {
            return receiveNotificationPassword;
        }

        public void setReceiveNotificationPassword(String receiveNotificationPassword) {
            this.receiveNotificationPassword = receiveNotificationPassword;
        }
    }
}
