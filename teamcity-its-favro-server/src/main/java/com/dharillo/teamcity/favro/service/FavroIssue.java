package com.dharillo.teamcity.favro.service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Type;
import java.util.Date;

public class FavroIssue {

    @NotNull
    private final String cardCommonId;
    @NotNull
    private final String name;
    @NotNull
    private final Date startDate;
    @NotNull
    private final Date dueDate;
    @NotNull
    private final String detailedDescription;
    @NotNull
    private String url;
    private final boolean archived;
    private final int sequentialId;

    public FavroIssue(@NotNull String url, @NotNull String cardCommonId, @NotNull String name, boolean archived, int sequentialId, @NotNull Date startDate, @NotNull Date dueDate, @NotNull String detailedDescription) {
        this.url = url;
        this.cardCommonId = cardCommonId;
        this.name = name;
        this.archived = archived;
        this.sequentialId = sequentialId;
        this.startDate = startDate;
        this.dueDate = dueDate;
        this.detailedDescription = detailedDescription;
    }

    @NotNull
    public String getUrl() {
        return url;
    }

    public FavroIssue setUrl(@NotNull String url) {
        this.url = url;
        return this;
    }

    @NotNull
    public String getCardCommonId() {
        return cardCommonId;
    }

    @NotNull
    public String getName() {
        return name;
    }

    public boolean isArchived() {
        return archived;
    }

    public int getSequentialId() {
        return sequentialId;
    }

    @NotNull
    public Date getStartDate() {
        return startDate;
    }

    @NotNull
    public Date getDueDate() {
        return dueDate;
    }

    @NotNull
    public String getDescription() {
        return detailedDescription;
    }


    @NotNull
    public static FavroIssue parse(@NotNull String serverResponse) {
        Gson serializer = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").create();
        Type responseType = new TypeToken<MultiEntryResponse<FavroIssue>>() {}.getType();
        MultiEntryResponse<FavroIssue> data = serializer.fromJson(serverResponse, responseType);
        if (data == null) {
            throw new JsonSyntaxException("Unable to deserialize");
        }
        return data.getEntities().get(0);
    }

    public String getState() {
        return this.archived ? "Archived" : "Open";
    }
}
