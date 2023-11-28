package org.example.repositories;

import org.example.models.entities.Token;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface TokenRepository  extends JpaRepository<Token, UUID> {

    Optional<Token> findById(UUID id);

    @Transactional(isolation = Isolation.SERIALIZABLE)
    @Modifying
    @Query("UPDATE Token t SET t.is_deleted = true WHERE t.id = :id AND t.is_deleted = false")
    Integer softDeleteById(UUID id);

    @Transactional(timeout = 6000)
    @Modifying
    @Query("DELETE FROM Token t WHERE t.is_deleted = true AND expires_at >= UNIX_TIMESTAMP()")
    void deleteNotUseTokens();
}
