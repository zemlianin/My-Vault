package org.example.services.dataAccess;

import org.example.models.dao.transport.DirectoryTransport;
import org.example.models.entities.Directory;
import org.example.models.entities.Token;
import org.example.models.entities.User;
import org.example.repositories.DirectoryRepository;
import org.example.repositories.SecretRepository;
import org.example.repositories.TokenRepository;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.NoSuchElementException;
import java.util.UUID;

@Service
public class DirectoryDataAccess {

    private final DirectoryRepository directoryRepository;

    public DirectoryDataAccess(DirectoryRepository directoryRepository) {
        this.directoryRepository = directoryRepository;
    }

    public Directory addDirectory(DirectoryTransport directoryTransport) {
        var directory = new Directory(directoryTransport);

        return directoryRepository.save(directory);
    }
}
