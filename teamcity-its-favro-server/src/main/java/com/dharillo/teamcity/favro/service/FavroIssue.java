package com.dharillo.teamcity.favro.service;

import org.jetbrains.annotations.NotNull;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.Date;

public class FavroIssue {

    @NotNull
    private final String commonId;
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

    public FavroIssue(@NotNull String url, @NotNull String commonId, @NotNull String name, boolean archived, int sequentialId, @NotNull Date startDate, @NotNull Date dueDate, @NotNull String description) {
        this.url = url;
        this.commonId = commonId;
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
    public String getCommonId() {
        return commonId;
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
        throw new NotImplementedException();
    }

    public String getState() {
        return this.archived ? "Archived" : "Open";
    }
}
