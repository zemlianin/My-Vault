package org.example.models.dao.request;

import java.util.UUID;

public class DirectoryRequest {
    String name;
    UUID parentId;

    public DirectoryRequest() {
    }

    public DirectoryRequest(String name, UUID parentId) {
        this.name = name;
        this.parentId = parentId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public UUID getParentId() {
        return parentId;
    }

    public void setParentId(UUID parentId) {
        this.parentId = parentId;
    }
}
