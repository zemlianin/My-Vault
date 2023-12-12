package org.example.models.dao.transport;

import org.example.models.entities.Secret;
import org.example.models.entities.User;
import org.example.models.entities.directory.RootDirectory;

import java.util.List;
import java.util.UUID;

public class RootDirectoryTransport {

    UUID id;
    User user;
    List<Secret> secrets;

    public RootDirectoryTransport() {
    }

    public RootDirectoryTransport(RootDirectory rootDirectory) {
        this.id = rootDirectory.getId();
        this.user = rootDirectory.getUser();
        this.secrets = rootDirectory.getSecrets();
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<Secret> getSecrets() {
        return secrets;
    }

    public void setSecrets(List<Secret> secrets) {
        this.secrets = secrets;
    }
}
