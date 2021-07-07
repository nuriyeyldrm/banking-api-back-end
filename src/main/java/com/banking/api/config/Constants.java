package com.banking.api.config;

public final class Constants {

    public static final String API_SECRET_KEY = "bankingapikey";

    // token validity is 2 hours, after token will expire
    public static final long TOKEN_VALIDITY = 2 * 60 * 60 * 1000;

    public static final String LOGIN_REGEX = "^(?>[a-zA-Z0-9!$&*+=?^_`{|}~.-]+@[a-zA-Z0-9-]+(?:\\.[a-zA-Z0-9-]+)*)|(?>[_.@A-Za-z0-9-]+)$";

    public static final String SYSTEM_ACCOUNT = "system";
    public static final String DEFAULT_LANGUAGE = "en";
    public static final String ANONYMOUS_USER = "anonymoususer";

    private Constants(){

    }
}
