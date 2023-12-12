package org.example.services.dataAccess;

import org.example.models.dao.transport.DirectoryTransport;
import org.example.models.entities.User;
import org.example.models.entities.directory.Directory;
import org.example.models.entities.directory.RootDirectory;
import org.example.repositories.DirectoryRepository;
import org.example.services.DirectoryService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class DirectoryDataAccess implements DirectoryService {

    private final DirectoryRepository directoryRepository;

    public DirectoryDataAccess(DirectoryRepository directoryRepository) {
        this.directoryRepository = directoryRepository;
    }

    public Directory addDirectory(DirectoryTransport directoryTransport) {
        var directory = new Directory(directoryTransport);

        return directoryRepository.save(directory);
    }

    public Optional<Directory> getDirectoryById(UUID id) {
        return directoryRepository.findById(id);
    }

    public List<Directory> getDirectoriesByUserAndParentId(User user, UUID parentId) {
        return directoryRepository.findAllByUserAndParentId(user, parentId);
    }
}
