package org.example.services.dataAccess;

import org.example.models.dao.transport.RootDirectoryTransport;
import org.example.models.entities.directory.RootDirectory;
import org.example.repositories.RootDirectoryRepository;
import org.example.services.RootDirectoryService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class RootDirectoryDataAccess implements RootDirectoryService {
    private final RootDirectoryRepository rootDirectoryRepository;

    public RootDirectoryDataAccess(RootDirectoryRepository rootDirectoryRepository) {
        this.rootDirectoryRepository = rootDirectoryRepository;
    }

    public RootDirectory addDirectory(RootDirectoryTransport rootDirectoryTransport) {
        var directory = new RootDirectory(rootDirectoryTransport);
        return rootDirectoryRepository.save(directory);
    }

    public Optional<RootDirectory> getDirectoryById(UUID id) {
        return rootDirectoryRepository.findById(id);
    }
}
