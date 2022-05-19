package com.vmo.core.common.utils;

import com.vmo.core.common.CommonConstants;
import com.vmo.core.modules.models.integration.auth.responses.gmap.GoogleServiceResponse;
import com.vmo.core.modules.models.integration.auth.responses.gmap.details.AddressComponent;

public class GoogleServiceUtils {
    public static AddressComponent getCountryAddress(GoogleServiceResponse response) {
        if (response != null && response.getResults().size() > 0 &&
                response.getResults().get(0).getAddressComponents().size() > 0
        ) {
            for (AddressComponent address : response.getResults().get(0).getAddressComponents()) {
                boolean country = false, political = false;
                for (String type : address.getTypes()) {
                    if (type.equals(CommonConstants.GOOGLE_ADDRESS_POLITICAL)) {
                        political = true;
                    } else if (type.equals(CommonConstants.GOOGLE_ADDRESS_COUNTRY)) {
                        country = true;
                    }
                }
                if (country && political) {
                    return address;
                }
            }
        }
        return null;
    }

    /**
     * @param response
     * @return the state address if in US, most likely province in other country
     */
    public static AddressComponent getAddressLevel1(GoogleServiceResponse response) {
        if (response != null && response.getResults().size() > 0 &&
                response.getResults().get(0).getAddressComponents().size() > 0
        ) {
            for (AddressComponent address : response.getResults().get(0).getAddressComponents()) {
                boolean level = false, political = false;
                for (String type : address.getTypes()) {
                    if (type.equals(CommonConstants.GOOGLE_ADDRESS_POLITICAL)) {
                        political = true;
                    } else if (type.equals(CommonConstants.GOOGLE_ADDRESS_AREA_1)) {
                        level = true;
                    }
                }
                if (level && political) {
                    return address;
                }
            }
        }
        return null;
    }

    /**
     * @param response
     * @return county (not country) if in US, most likely district in other country
     */
    public static AddressComponent getAddressLevel2(GoogleServiceResponse response) {
        if (response != null && response.getResults().size() > 0 &&
                response.getResults().get(0).getAddressComponents().size() > 0
        ) {
            for (AddressComponent address : response.getResults().get(0).getAddressComponents()) {
                boolean level = false, political = false;
                for (String type : address.getTypes()) {
                    if (type.equals(CommonConstants.GOOGLE_ADDRESS_POLITICAL)) {
                        political = true;
                    } else if (type.equals(CommonConstants.GOOGLE_ADDRESS_AREA_2)) {
                        level = true;
                    }
                }
                if (level && political) {
                    return address;
                }
            }
        }
        return null;
    }

    /**
     * @param response
     * @return city in US
     */
    public static AddressComponent getCity(GoogleServiceResponse response) {
        if (response != null && response.getResults().size() > 0 &&
                response.getResults().get(0).getAddressComponents().size() > 0
        ) {
            for (AddressComponent address : response.getResults().get(0).getAddressComponents()) {
                boolean locality = false, political = false;
                for (String type : address.getTypes()) {
                    if (type.equals(CommonConstants.GOOGLE_ADDRESS_POLITICAL)) {
                        political = true;
                    } else if (type.equals(CommonConstants.GOOGLE_ADDRESS_LOCALITY)) {
                        locality = true;
                    }
                }
                if (locality && political) {
                    return address;
                }
            }

            for (AddressComponent address : response.getResults().get(0).getAddressComponents()) {
                boolean locality = false, political = false;
                for (String type : address.getTypes()) {
                    if (type.equals(CommonConstants.GOOGLE_ADDRESS_POLITICAL)) {
                        political = true;
                    } else if (type.equals(CommonConstants.GOOGLE_ADDRESS_SUBLOCALITY)) {
                        locality = true;
                    }
                }
                if (locality && political) {
                    return address;
                }
            }
        }
        return null;
    }

    public static String getZipCode(GoogleServiceResponse response) {
        if (response != null && response.getResults().size() > 0 &&
                response.getResults().get(0).getAddressComponents().size() > 0
        ) {
            for (AddressComponent address : response.getResults().get(0).getAddressComponents()) {
                for (String type : address.getTypes()) {
                    if (type.equals(CommonConstants.GOOGLE_ADDRESS_POSTAL_CODE)) {
                        return address.getLongName();
                    }
                }
            }
        }
        return null;
    }
}
