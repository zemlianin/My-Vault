package org.example.models.entities.directory;

import org.example.models.entities.Secret;
import org.example.models.entities.User;

import java.util.List;
import java.util.UUID;

public interface DirectoryInterface {
    User getUser();

    void setUser(User user);

    UUID getId();

    void setId(UUID id);

    List<Secret> getSecrets();

    void setSecrets(List<Secret> secrets);
}
