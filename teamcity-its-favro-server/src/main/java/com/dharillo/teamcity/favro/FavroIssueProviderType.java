package com.dharillo.teamcity.favro;

import jetbrains.buildServer.issueTracker.IssueProviderType;
import jetbrains.buildServer.web.openapi.PluginDescriptor;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

import static com.dharillo.teamcity.favro.FavroConstants.*;

public class FavroIssueProviderType extends IssueProviderType {

    public static final String DEFAULT_ISSUE_PATTERN = "#(\\w+-\\d+)";
    @NotNull
    private final String configurationUrl;
    @NotNull
    private final String issuePopupUrl;

    public FavroIssueProviderType(@NotNull final PluginDescriptor pluginDescriptor) {
        configurationUrl = pluginDescriptor.getPluginResourcesPath("admin/editIssueProvider.jsp");
        issuePopupUrl = pluginDescriptor.getPluginResourcesPath("popup.jsp");
    }

    @NotNull
    @Override
    public String getType() {
        return "FavroIssues";
    }

    @NotNull
    @Override
    public String getDisplayName() {
        return "Favro";
    }

    @NotNull
    @Override
    public String getEditParametersUrl() {
        return this.configurationUrl;
    }

    @NotNull
    @Override
    public String getIssueDetailsUrl() {
        return this.issuePopupUrl;
    }

    @NotNull
    @Override
    public Map<String, String> getDefaultProperties() {
        return new HashMap<String, String>() {{
            put(PARAM_AUTH_TYPE, AUTH_LOGIN_PASSWORD);
            put(PARAM_PATTERN, DEFAULT_ISSUE_PATTERN);
        }};
    }
}
