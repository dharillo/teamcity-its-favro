package com.dharillo.teamcity.favro;

import com.dharillo.teamcity.favro.service.FavroIssue;
import com.dharillo.teamcity.favro.service.IssueService;
import jetbrains.buildServer.issueTracker.AbstractIssueFetcher;
import jetbrains.buildServer.issueTracker.IssueData;
import jetbrains.buildServer.util.cache.EhCacheUtil;
import jetbrains.buildServer.util.ssl.SSLTrustStoreProvider;
import org.apache.commons.httpclient.Credentials;
import org.apache.http.auth.InvalidCredentialsException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class FavroIssueFetcher extends AbstractIssueFetcher {

    private SSLTrustStoreProvider trustStore;
    /**
     * @param cacheUtil
     * @deprecated
     */
    public FavroIssueFetcher(@NotNull EhCacheUtil cacheUtil, @NotNull final SSLTrustStoreProvider trustStore) {
        super(cacheUtil);
        this.trustStore = trustStore;
    }

    @NotNull
    @Override
    public IssueData getIssue(@NotNull String host, @NotNull String id, @Nullable final Credentials credentials) throws Exception {
        final String url = getUrl(host, id);
        return getFromCacheOrFetch(url, () -> {
            if (credentials == null) {
                throw new InvalidCredentialsException();
            }
            return createIssue(new IssueService(credentials).getIssue(url));
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
