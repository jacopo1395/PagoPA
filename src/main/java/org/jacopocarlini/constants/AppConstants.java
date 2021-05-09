package org.jacopocarlini.constants;

import lombok.experimental.UtilityClass;

@UtilityClass
public final class AppConstants {

    @UtilityClass
    public static final class Url {
        public static final String VERSION = "/v1";
        public static final String SUBMIT_MESSAGE_FOR_USER = VERSION + "/submitMessageForUser";
    }

    @UtilityClass
    public static final class IOUrl {
        private static final String BASE_URL = "https://api.io.italia.it/api";
        private static final String VERSION = "/v1";

        public static final String OCP_APIM_SUBSCRIPTION_KEY = "Ocp-Apim-Subscription-Key";
        public static final String SUBMIT_MESSAGE = BASE_URL + VERSION + "/messages";
        public static final String GET_PROFILE = BASE_URL + VERSION + "/profiles/{fiscalCode}";
    }
}
