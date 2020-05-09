package com.dharillo.teamcity.favro.auth;

import org.apache.commons.httpclient.Credentials;
import org.jetbrains.annotations.NotNull;

/**
 * Stores the token credentials to access to Favro
 *
 * @author David Harillo (dhs.seck@gmail.com)
 */
public class TokenCredentials implements Credentials {
    @NotNull
    private final String token;

    public TokenCredentials(@NotNull final String token) {
        this.token = token;
    }

    @NotNull
    public String getToken() {
        return this.token;
    }
}
