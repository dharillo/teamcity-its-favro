package com.dharillo.teamcity.favro.service;

import com.dharillo.teamcity.favro.auth.FavroUsernamePasswordCredentials;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.httpclient.Credentials;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.HttpClientBuilder;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.util.regex.Matcher;

import static com.dharillo.teamcity.favro.FavroConstants.PATTERN_DEFAULT;

public class IssueService {
    @NotNull
    private final Credentials credentials;
    private final HttpClient client;
    private static final String API_CARD_ENDPOINT = "https://favro.com/api/v1/cards/";

    public IssueService(@NotNull final Credentials credentials) {
        this(credentials, createClient());
    }

    public IssueService(@NotNull final Credentials credentials, @NotNull HttpClient client) {
        this.credentials = credentials;
        this.client = client;
    }

    public FavroIssue getIssue(@NotNull String url) throws IllegalArgumentException {
        if (url.isEmpty()) {
            throw new IllegalArgumentException("Invalid url");
        }
        final int sequentialId = getSequentialId(url);
        return getFavroIssue(sequentialId).setUrl(url);
    }

    @Nullable
    private FavroIssue getFavroIssue(int sequentialId) {
        try {
            HttpGet request = getRequest(sequentialId);
            HttpResponse response = client.execute(request);
            if (response.getStatusLine().getStatusCode() != HttpStatus.SC_OK) {
                return null;
            }
            return readResponse(response);
        } catch (URISyntaxException | IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @NotNull
    private static HttpClient createClient() {
        return HttpClientBuilder.create().build();
    }

    @NotNull
    private FavroIssue readResponse(HttpResponse response) throws IOException {
        String body = new BasicResponseHandler().handleResponse(response);
        return FavroIssue.parse(body);
    }

    @NotNull
    private HttpGet getRequest(int sequentialId) throws URISyntaxException {
        URIBuilder builder = new URIBuilder(API_CARD_ENDPOINT);
        builder.addParameter("unique", "true");
        builder.addParameter("cardSequentialId", Integer.toString(sequentialId));
        HttpGet request = new HttpGet(builder.build());
        addAuth(request);
        return request;
    }

    private void addAuth(HttpGet request) {
        if (credentials instanceof FavroUsernamePasswordCredentials) {
            final FavroUsernamePasswordCredentials favroCredentials = (FavroUsernamePasswordCredentials)this.credentials;
            String auth = favroCredentials.getUserName() + ":" + favroCredentials.getPassword();
            byte[] encodedAuth = Base64.encodeBase64(auth.getBytes(StandardCharsets.ISO_8859_1));
            String authHeader = "Basic " + new String(encodedAuth);
            request.setHeader(HttpHeaders.AUTHORIZATION, authHeader);
            request.addHeader("organizationId", favroCredentials.getOrganization());
        }
    }

    public static int getSequentialId(@NotNull String url) throws IllegalArgumentException {
        final Matcher match = PATTERN_DEFAULT.matcher(url);
        if (!match.matches()) {
            throw new IllegalArgumentException("The url given doesn't match the pattern");
        }
        return Integer.parseInt(match.group("sequentialId"));
    }
}
