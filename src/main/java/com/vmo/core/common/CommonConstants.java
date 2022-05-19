package com.vmo.core.common;

import java.util.Arrays;
import java.util.List;

public interface CommonConstants {
    String HEADER_AUTH_NAME = "Authorization";
    String HEADER_AUTH_PREFIX = "Bearer ";
    String HEADER_SECRET_KEY = "x-bbb-client-secret";
    String ROLE_PREFIX = "ROLE_";
    String HEADER_PROXY_FORWARD_IP = "x-forwarded-for";

    String CONFIG_PREFIX = "base";

    String FORMAT_DATE = "yyyy-MM-dd";
    String FORMAT_DATE_TIME = "yyyy-MM-dd'T'HH:mm:ss.SSS";
    String FORMAT_DATE_TIME_EBAY = "yyyy-MM-dd'T'HH:mm:ss.SSSZ";
    String FORMAT_MONTH = "yyyy-MM";
    String FORMAT_TIME = "HH:mm:ss";
    String FORMAT_DAY_US = "MM-dd-yyyy";
    String FORMAT_TIME_MINUTE = "HH:mm";
    String MD5_SALT = "MD5_SALT_HASH_KEY";


//    String OPEN_TERMS_NAME = "Open Terms";

    //time
    long SECOND = 1000L;
    long MINUTE = SECOND * 60;
    long HOUR = MINUTE * 60;
    long DAY = HOUR * 24;
    long WEEK = DAY * 7;


    //database definition
    String NUMERIC = "NUMERIC(36,2)";
    String NUMERIC_3 = "NUMERIC(36,3)";
    String NUMERIC_38_2 = "NUMERIC(38,2)";
    String NUMERIC_10_4 = "NUMERIC(10,4)";
    String NUMERIC_10_2 = "NUMERIC(10,2)";


    //api request
    List<String> cacheableBodyContentTypes = Arrays.asList(
            "json",
            "xml",
            "text",
            "xhtml"
    );


    //component
    String INV_COMP_TYPE_BASE = "base";

    //eternal service
    String GOOGLE_SERVICE_CATEGORY_INFO = "get_info";
    String GOOGLE_ADDRESS_POLITICAL = "political";
    String GOOGLE_ADDRESS_COUNTRY = "country";
    String GOOGLE_ADDRESS_AREA_1 = "administrative_area_level_1";
    String GOOGLE_ADDRESS_AREA_2 = "administrative_area_level_2";
    String GOOGLE_ADDRESS_LOCALITY = "locality";
    String GOOGLE_ADDRESS_SUBLOCALITY = "sublocality";
    String GOOGLE_ADDRESS_POSTAL_CODE = "postal_code";


    //mail template
    String TEMPLATE_LISTING_SHIPPED = "order-shipped";
    String TEMPLATE_LISTING_SOLD_PICKUP = "pickup-auto-send";
    String TEMPLATE_LISTING_SOLD_PICKUP_BBB = "pickup-auto-send-bbb";
    String TEMPLATE_PRICING_ENGINE_PREDICT_CHANGE = "prediction-price-change";
    String TEMPLATE_TRADE_IN_LEAD_GEN_CREATED = "sales-qualified";


    String PLACEHOLDER_INVENTORY_TITLE = "<bike_name>";
    String PLACEHOLDER_DATE = "<date>";
//    String SUBJECT_LISTING_SOLD_PICKUP = "The item " + PLACEHOLDER_INVENTORY_TITLE + " at bicyclebluebook.com is ready for pickup";
    String SUBJECT_LISTING_SOLD_PICKUP = "Congratulations on your purchase: " + PLACEHOLDER_INVENTORY_TITLE;
    String SUBJECT_PRICING_ENGINE_PREDICT_CHANGE = "Prediction for Tommorrow’s " + PLACEHOLDER_DATE + " Price change";

    //Inventory const config name
    String INVENTORY_NAME_OFFSET = "inventory_name_offset";

    int PAGE_SIZE_DEFAULT = 20;
}
