package org.example.models.dao.response;

import org.example.models.entities.directory.DirectoryInterface;

import java.util.List;

public class ListDirectoriesResponse {
    List<DirectoryInterface> directories;

    public ListDirectoriesResponse() {
    }

    public List<DirectoryInterface> getDirectories() {
        return directories;
    }

    public void setDirectories(List<DirectoryInterface> directories) {
        this.directories = directories;
    }
}
