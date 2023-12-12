package org.example.models.dao.transport;

import org.example.models.dao.request.DirectoryRequest;
import org.example.models.entities.User;
import org.example.models.entities.directory.Directory;

import java.util.Optional;
import java.util.UUID;

public class DirectoryTransport {
    UUID id;
    String name;
    Optional<UUID> parentId;
    User user;

    public DirectoryTransport() {
    }

    public DirectoryTransport(Directory directory) {
        id = directory.getId();
        name = directory.getName();
        parentId = Optional.of(directory.getParentId());
        user = directory.getUser();
    }


    public DirectoryTransport(User user, DirectoryRequest directoryRequest) {
        this.name = directoryRequest.getName();
        this.parentId = Optional.ofNullable(directoryRequest.getParentId());
        this.user = user;
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

    public Optional<UUID> getParentId() {
        return parentId;
    }

    public void setParentId(Optional<UUID> parentId) {
        this.parentId = parentId;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
