package com.dharillo.teamcity.favro.service;

import org.jetbrains.annotations.NotNull;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;
import org.apache.commons.httpclient.Credentials;

public class IssueService {
    @NotNull
    private final Credentials credentials;

    public IssueService(@NotNull final Credentials credentials) {
        this.credentials = credentials;
    }

    public FavroIssue getIssue(@NotNull String url) {
        if (url.isEmpty()) {
            throw new IllegalArgumentException("Invalid url");
        }
        throw new NotImplementedException();
    }
}
