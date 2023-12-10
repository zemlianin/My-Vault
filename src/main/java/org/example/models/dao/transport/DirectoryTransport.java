package org.example.models.dao.transport;

import org.example.models.dao.request.DirectoryRequest;
import org.example.models.entities.User;

import java.util.UUID;

public class DirectoryTransport {
    UUID id;
    String name;
    UUID parentId;
    User user;
    Boolean isRoot;

    public DirectoryTransport() {
    }


    public DirectoryTransport(UUID id, String name, UUID parentId, User user, Boolean isRoot) {
        this.id = id;
        this.isRoot = isRoot;
        this.name = name;
        this.parentId = parentId;
        this.user = user;
    }

    public Boolean getRoot() {
        return isRoot;
    }

    public void setRoot(Boolean root) {
        isRoot = root;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
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

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
