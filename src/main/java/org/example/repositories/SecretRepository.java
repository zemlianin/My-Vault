package org.example.repositories;

import org.example.models.entities.Secret;
import org.example.models.entities.User;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface SecretRepository extends JpaRepository<Secret, UUID> {
    List<Secret> findAllByUserAndDirectoryId(User user, UUID directoryId);
    List<Secret> findAllByUserAndDirectoryId(User user, UUID directoryId, PageRequest pageable);
    Optional<Secret> findByIdAndUser(UUID id, User user);
    Optional<Secret> findById(UUID id);
    List<Secret> deleteAllByUser(User user);
}
