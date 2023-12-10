package org.example.models.dao.response;

import org.example.models.entities.Directory;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class DirectoryResponse {
    UUID id;
    String name;
    UUID parentId;
    List<SecretResponse> secrets;

    UserResponse user;

    public DirectoryResponse() {
    }

    public DirectoryResponse(Directory directory) {
        id = directory.getId();
        name = directory.getName();
        parentId = directory.getParentId();
        secrets = new ArrayList<>();
        user = new UserResponse(directory.getUser());

        for (var secret : directory.getSecrets()) {
            secrets.add(new SecretResponse(secret));
        }
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

    public List<SecretResponse> getSecrets() {
        return secrets;
    }

    public void setSecrets(List<SecretResponse> secrets) {
        this.secrets = secrets;
    }
}
