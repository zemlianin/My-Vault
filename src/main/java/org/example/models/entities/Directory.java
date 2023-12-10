package org.example.models.entities;

import jakarta.persistence.*;
import org.example.models.dao.transport.DirectoryTransport;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
public class Directory {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    UUID id;
    String name;
    UUID parentId;

    Boolean isRoot;

    @ManyToOne()
    @JoinColumn(name = "user_id")
    User user;

    @OneToMany
    List<Secret> secrets;

    public Directory() {
    }

    public Directory(UUID id, String name, UUID parentId, Boolean isRoot, User user, List<Secret> secrets) {
        this.id = id;
        this.name = name;
        this.parentId = parentId;
        this.isRoot = isRoot;
        this.user = user;
        this.secrets = secrets;
    }

    public Directory(DirectoryTransport directoryTransport) {
        this.user = directoryTransport.getUser();
        this.parentId = directoryTransport.getParentId();
        this.isRoot = directoryTransport.getRoot();
        this.name = directoryTransport.getName();
        this.parentId = directoryTransport.getParentId();
        this.isRoot = directoryTransport.getRoot();
        this.secrets = new ArrayList<>();
    }

    public Boolean getRoot() {
        return isRoot;
    }

    public void setRoot(Boolean root) {
        isRoot = root;
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
