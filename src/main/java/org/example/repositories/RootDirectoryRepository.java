package org.example.repositories;

import org.example.models.entities.directory.RootDirectory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface RootDirectoryRepository extends JpaRepository<RootDirectory, UUID> {
}
