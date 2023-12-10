package org.example.repositories;

import org.example.models.entities.Directory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface DirectoryRepository extends JpaRepository<Directory, UUID> {
}
