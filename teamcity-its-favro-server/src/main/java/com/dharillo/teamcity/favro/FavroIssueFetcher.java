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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FavroIssueFetcher extends AbstractIssueFetcher {

    private static final Logger LOG = Loggers.ISSUE_TRACKERS;
    private Pattern myPattern;

    /**
     * @param cacheUtil Request cache
     * @deprecated
     */
    public FavroIssueFetcher(@NotNull EhCacheUtil cacheUtil) {
        super(cacheUtil);
    }

    public void setPattern(final Pattern pattern) {
        myPattern = pattern;
    }

    @NotNull
    @Override
    public IssueData getIssue(@NotNull String host, @NotNull String id, @Nullable final Credentials credentials) throws Exception {
        final String issueId = getIssueId(id);
        final String url = getUrl(host, issueId);
        return getFromCacheOrFetch(url, () -> {
            if (credentials == null) {
                throw new InvalidCredentialsException();
            }
            try {
                FavroIssue issue = new IssueService(credentials).getIssue(url);
                return createIssue(issue);
            } catch (IllegalArgumentException e) {
                LOG.warn(e);
                throw e;
            }
        });
    }

    private String getIssueId(@NotNull final String idString) {
        final Matcher matcher = myPattern.matcher(idString);
        if (matcher.find() && matcher.groupCount() >= 1) {
            return matcher.group(1);
        } else {
            return idString;
        }
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
