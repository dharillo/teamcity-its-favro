package com.dharillo.teamcity.favro.service;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class MultiEntryResponse<TEntity> {
    @NotNull
    private List<TEntity> entities;
    @NotNull
    private String requestId;

    public MultiEntryResponse(@NotNull List<TEntity> entities, @NotNull String requestId) {
        this.entities = entities;
        this.requestId = requestId;
    }

    @NotNull
    public List<TEntity> getEntities() {
        return entities;
    }

    @NotNull
    public String getRequestId() {
        return requestId;
    }
}
