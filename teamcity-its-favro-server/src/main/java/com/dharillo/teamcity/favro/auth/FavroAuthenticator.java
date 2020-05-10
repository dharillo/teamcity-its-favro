package com.dharillo.teamcity.favro.auth;

import jetbrains.buildServer.issueTracker.IssueFetcherAuthenticator;
import jetbrains.buildServer.util.HTTPRequestBuilder;
import org.apache.commons.httpclient.Credentials;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.UsernamePasswordCredentials;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

import static com.dharillo.teamcity.favro.FavroConstants.*;

public class FavroAuthenticator implements IssueFetcherAuthenticator {
    private Credentials credentials;

    public FavroAuthenticator(@NotNull final Map<String, String> properties) {
        final String authType = properties.get(PARAM_AUTH_TYPE);
        final String organization = properties.get(PARAM_ORGANIZATION);
        final String username = properties.get(PARAM_USERNAME);
        if (AUTH_LOGIN_PASSWORD.equals(authType)) {
            final String password = properties.get(PARAM_PASSWORD);
            this.credentials = new FavroUsernamePasswordCredentials(username, password, organization);
        } else if (AUTH_ACCESS_TOKEN.equals(authType)) {
            final String token = properties.get(PARAM_ACCESS_TOKEN);
            this.credentials = new FavroUsernamePasswordCredentials(username, token, organization);
        }
    }

    @Override
    public boolean isBasicAuth() {
        return false;
    }

    /**
     * @param httpMethod Method to check
     * @deprecated
     */
    @Override
    public void applyAuthScheme(@NotNull HttpMethod httpMethod) {

    }

    @Override
    public void applyAuthScheme(@NotNull HTTPRequestBuilder httpRequestBuilder) {

    }

    @Nullable
    @Override
    public Credentials getCredentials() {
        return this.credentials;
    }
}
