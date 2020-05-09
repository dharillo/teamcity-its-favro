package com.dharillo.teamcity.favro;

import jetbrains.buildServer.issueTracker.AbstractIssueProvider;
import jetbrains.buildServer.issueTracker.IssueFetcher;
import org.jetbrains.annotations.NotNull;

public class FavroIssueProvider extends AbstractIssueProvider {
    public FavroIssueProvider( @NotNull IssueFetcher fetcher) {
        super("Favro", fetcher);
    }

    protected boolean useIdPrefix() {
        return true;
    }
}
