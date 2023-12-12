package org.example.models.entities.directory;

import jakarta.persistence.*;
import org.example.models.dao.transport.RootDirectoryTransport;
import org.example.models.entities.Secret;
import org.example.models.entities.User;

import java.util.List;
import java.util.UUID;

@Entity
public class RootDirectory implements DirectoryInterface {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    UUID id;

    @OneToOne()
    @JoinColumn(name = "user_id")
    User user;

    @OneToMany
    List<Secret> secrets;

    public RootDirectory() {
    }

    public RootDirectory(RootDirectoryTransport rootDirectoryTransport) {
        id = rootDirectoryTransport.getId();
        user = rootDirectoryTransport.getUser();
        secrets = rootDirectoryTransport.getSecrets();
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
