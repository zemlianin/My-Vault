package org.example.services;

import org.example.models.Secret;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface SecretRepository extends JpaRepository<Secret, UUID> {
}
