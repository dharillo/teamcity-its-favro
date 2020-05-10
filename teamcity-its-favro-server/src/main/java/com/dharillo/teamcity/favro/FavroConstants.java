package com.dharillo.teamcity.favro;

import java.util.regex.Pattern;

public interface FavroConstants {
    String PARAM_NAME = "name";
    String PARAM_ORGANIZATION = "organization";
    String PARAM_AUTH_TYPE = "authType";
    String PARAM_USERNAME = "username";
    String PARAM_PASSWORD = "secure:password";
    String PARAM_ACCESS_TOKEN = "secure:accessToken";
    String PARAM_PATTERN = "pattern";

    String AUTH_LOGIN_PASSWORD = "loginPassword";
    String AUTH_ACCESS_TOKEN = "accessToken";

    Pattern PATTERN_DEFAULT = Pattern.compile("https://favro.com/organization/[^/]+/([^?]+)?\\?card=[^-]+-(?<sequentialId>\\d+)");
}
