package com.dharillo.teamcity.favro.auth;

import org.apache.commons.httpclient.UsernamePasswordCredentials;
import org.jetbrains.annotations.NotNull;

public class FavroUsernamePasswordCredentials extends UsernamePasswordCredentials {
    @NotNull
    private final String organization;
    public FavroUsernamePasswordCredentials(@NotNull final String username, @NotNull final String password, @NotNull final String organization) {
        super(username, password);
        this.organization = organization;
    }

    @NotNull
    public String getOrganization() {
        return organization;
    }
}
