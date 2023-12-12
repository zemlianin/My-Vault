package org.example.models.entities.directory;

import jakarta.persistence.*;
import org.example.models.dao.transport.DirectoryTransport;
import org.example.models.entities.Secret;
import org.example.models.entities.User;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
public class Directory implements DirectoryInterface {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    UUID id;

    @ManyToOne()
    @JoinColumn(name = "user_id")
    User user;

    String name;
    UUID parentId;
    @OneToMany
    List<Secret> secrets;

    public Directory() {
    }

    public Directory(UUID id, String name, UUID parentId, User user, List<Secret> secrets) {
        this.id = id;
        this.name = name;
        this.parentId = parentId;
        this.user = user;
        this.secrets = secrets;
    }

    public Directory(DirectoryTransport directoryTransport) {
        this.id = directoryTransport.getId();
        this.user = directoryTransport.getUser();
        this.parentId = directoryTransport.getParentId().isEmpty() ? null : directoryTransport.getParentId().get();
        this.name = directoryTransport.getName();
        this.secrets = new ArrayList<>();
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
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

    public UUID getParentId() {
        return parentId;
    }

    public void setParentId(UUID parentId) {
        this.parentId = parentId;
    }

    public List<Secret> getSecrets() {
        return secrets;
    }

    public void setSecrets(List<Secret> secrets) {
        this.secrets = secrets;
    }
}
