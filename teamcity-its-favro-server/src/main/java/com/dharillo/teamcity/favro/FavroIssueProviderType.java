package com.dharillo.teamcity.favro;

import jetbrains.buildServer.issueTracker.IssueProviderType;
import jetbrains.buildServer.web.openapi.PluginDescriptor;
import org.jetbrains.annotations.NotNull;

public class FavroIssueProviderType extends IssueProviderType {

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
}
