package com.dharillo.teamcity.favro;

import com.intellij.openapi.diagnostic.Logger;
import com.dharillo.teamcity.favro.service.FavroIssue;
import com.dharillo.teamcity.favro.service.IssueService;
import jetbrains.buildServer.issueTracker.AbstractIssueFetcher;
import jetbrains.buildServer.issueTracker.IssueData;
import jetbrains.buildServer.util.cache.EhCacheUtil;
import jetbrains.buildServer.log.Loggers;
import org.apache.commons.httpclient.Credentials;
import org.apache.http.auth.InvalidCredentialsException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.net.URISyntaxException;

public class FavroIssueFetcher extends AbstractIssueFetcher {

    private static final Logger LOG = Loggers.ISSUE_TRACKERS;
    /**
     * @param cacheUtil Request cache
     * @deprecated
     */
    public FavroIssueFetcher(@NotNull EhCacheUtil cacheUtil) {
        super(cacheUtil);
    }

    @NotNull
    @Override
    public IssueData getIssue(@NotNull String host, @NotNull String id, @Nullable final Credentials credentials) throws Exception {
        final String url = getUrl(host, id);
        return getFromCacheOrFetch(url, () -> {
            if (credentials == null) {
                throw new InvalidCredentialsException();
            }
            try {
                FavroIssue issue = new IssueService(credentials).getIssue(url);
                return createIssue(issue);
            } catch (IllegalArgumentException | URISyntaxException e) {
                LOG.warn(e);
                throw e;
            }
        });
    }

    private IssueData createIssue(FavroIssue issue) {
        return new IssueData(
                Integer.toString(issue.getSequentialId()),
                issue.getName(),
                issue.getState(),
                issue.getUrl(),
                issue.isArchived());
    }

    @NotNull
    @Override
    public String getUrl(@NotNull String host, @NotNull String id) {
        return host + "?card=" + id;
    }
}
