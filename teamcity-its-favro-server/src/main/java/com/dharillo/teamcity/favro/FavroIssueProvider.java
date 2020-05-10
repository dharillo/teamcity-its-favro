package com.dharillo.teamcity.favro;

import com.dharillo.teamcity.favro.auth.FavroAuthenticator;
import jetbrains.buildServer.issueTracker.AbstractIssueProvider;
import jetbrains.buildServer.issueTracker.IssueFetcher;
import jetbrains.buildServer.issueTracker.IssueFetcherAuthenticator;
import jetbrains.buildServer.issueTracker.IssueProviderType;
import jetbrains.buildServer.serverSide.InvalidProperty;
import jetbrains.buildServer.serverSide.PropertiesProcessor;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.dharillo.teamcity.favro.FavroConstants.*;
import static com.intellij.openapi.util.text.StringUtil.isEmptyOrSpaces;

public class FavroIssueProvider extends AbstractIssueProvider {
    public FavroIssueProvider(@NotNull IssueProviderType type, @NotNull IssueFetcher fetcher) {
        super(type.getType(), fetcher);
    }

    @NotNull
    @Override
    protected IssueFetcherAuthenticator getAuthenticator() {
        return new FavroAuthenticator(myProperties);
    }

    @Override
    public void setProperties(@NotNull Map<String, String> properties) {
        patchWithPattern(properties);
        myHost = getHostProperty(properties);
        myFetchHost = myHost;
        properties.put("host", myHost);
        super.setProperties(properties);
    }

    private void patchWithPattern(Map<String, String> properties) {
        String patternTemplate = properties.get(PARAM_PATTERN);
        if (patternTemplate == null || isEmptyOrSpaces(patternTemplate)) {
            properties.put(PARAM_PATTERN, FavroIssueProviderType.DEFAULT_ISSUE_PATTERN);
        }
    }

    private String getHostProperty(Map<String, String> properties) {
        final String organization = properties.get(PARAM_ORGANIZATION);
        return String.format("https://favro.com/organization/%s/", organization);
    }

    @NotNull
    @Override
    protected Pattern compilePattern(@NotNull Map<String, String> properties) {
        final Pattern result = super.compilePattern(properties);
        ((FavroIssueFetcher) myFetcher).setPattern(result);
        return result;
    }

    @NotNull
    @Override
    protected String extractId(@NotNull final String match) {
        Matcher m = myPattern.matcher(match);
        if (m.find() && m.groupCount() >= 1) {
            return m.group(1);
        } else {
            return super.extractId(match);
        }
    }

    @Override
    @NotNull
    public PropertiesProcessor getPropertiesProcessor() {
        return FAVRO_PROP_PROCESSOR;
    }

    private static final PropertiesProcessor FAVRO_PROP_PROCESSOR = new PropertiesProcessor() {
        @Override
        public Collection<InvalidProperty> process(Map<String, String> map) {
            final List<InvalidProperty> result = new ArrayList<>();

            checkNotEmptyParam(result, map, PARAM_USERNAME, "Username must be specified");
            checkNotEmptyParam(result, map, PARAM_ORGANIZATION, "Organization identifier must be specified");

            // Check auth type
            if (!checkNotEmptyParam(result, map, PARAM_AUTH_TYPE, "Auth type must be selected")) {
                return result;
            }
            String authTypeParam = map.get(PARAM_AUTH_TYPE);

            if (AUTH_LOGIN_PASSWORD.equals((authTypeParam))) {
                checkNotEmptyParam(result, map, PARAM_PASSWORD, "Password must be specified");
            } else if (AUTH_ACCESS_TOKEN.equals(authTypeParam)) {
                checkNotEmptyParam(result, map, PARAM_ACCESS_TOKEN, "Access token must be specified");
            }
            return result;
        }

        private boolean checkNotEmptyParam(@NotNull final Collection<InvalidProperty> invalid,
                                           @NotNull final Map<String, String> map,
                                           @NotNull final String propertyName,
                                           @NotNull final String errorMessage) {
            if (isEmptyOrSpaces(map.get(propertyName))) {
                invalid.add(new InvalidProperty(propertyName, errorMessage));
                return false;
            }
            return true;
        }
    };
}
