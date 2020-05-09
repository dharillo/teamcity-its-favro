package com.dharillo.teamcity.favro;

import jetbrains.buildServer.issueTracker.AbstractIssueProviderFactory;
import jetbrains.buildServer.issueTracker.IssueFetcher;
import jetbrains.buildServer.issueTracker.IssueProvider;
import jetbrains.buildServer.issueTracker.IssueProviderType;
import org.jetbrains.annotations.NotNull;

public class FavroIssueProviderFactory extends AbstractIssueProviderFactory {
    public FavroIssueProviderFactory(@NotNull IssueProviderType type, @NotNull IssueFetcher fetcher) {
        super(type, fetcher);
    }

    @NotNull
    @Override
    public IssueProvider createProvider() {
        return new FavroIssueProvider(myType, myFetcher);
    }
}
