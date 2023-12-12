package org.example.repositories;

import org.example.models.entities.User;
import org.example.models.entities.directory.Directory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface DirectoryRepository extends JpaRepository<Directory, UUID> {
    List<Directory> findAllByUserAndParentId(User user, UUID parentId);

    Optional<Directory> findByIdAndUser(UUID id, User user);
}
