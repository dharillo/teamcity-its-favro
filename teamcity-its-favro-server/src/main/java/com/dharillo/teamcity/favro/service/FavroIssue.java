package com.dharillo.teamcity.favro.service;

import com.google.gson.Gson;
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
    private final String description;
    @NotNull
    private final String url;
    private final boolean archived;
    private final int sequentialId;

    public FavroIssue(@NotNull String url, @NotNull String cardCommonId, @NotNull String name, boolean archived, int sequentialId, @NotNull Date startDate, @NotNull Date dueDate, @NotNull String description) {
        this.url = url;
        this.cardCommonId = cardCommonId;
        this.name = name;
        this.archived = archived;
        this.sequentialId = sequentialId;
        this.startDate = startDate;
        this.dueDate = dueDate;
        this.description = description;
    }

    @NotNull
    public String getUrl() {
        return url;
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
        return description;
    }


    @NotNull
    public static FavroIssue parse(@NotNull String serverResponse) {
        Gson serializer = new Gson();
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
