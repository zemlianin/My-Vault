package org.example.services.dataAccess;

import org.example.models.dao.transport.DirectoryTransport;
import org.example.models.entities.Secret;
import org.example.models.entities.User;
import org.example.models.entities.directory.Directory;
import org.example.models.entities.directory.RootDirectory;
import org.example.repositories.DirectoryRepository;
import org.example.repositories.SecretRepository;
import org.example.services.DirectoryService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@Service
public class DirectoryDataAccess implements DirectoryService {

    private final DirectoryRepository directoryRepository;
    private final SecretRepository secretRepository;

    public DirectoryDataAccess(DirectoryRepository directoryRepository,
                               SecretRepository secretRepository) {
        this.directoryRepository = directoryRepository;
        this.secretRepository = secretRepository;
    }

    public Directory addDirectory(DirectoryTransport directoryTransport) {
        if (directoryTransport.getParentId().isPresent() &&
                directoryRepository.findById(directoryTransport.getParentId().get()).isEmpty()) {
            throw new NoSuchElementException();
        }

        var directory = new Directory(directoryTransport);

        return directoryRepository.save(directory);
    }

    public List<Directory> getDirectoriesByUserAndParentId(User user, UUID parentId) {

        if (parentId != null &&
                directoryRepository.findById(parentId).isEmpty()) {
            throw new NoSuchElementException();
        }

        return directoryRepository.findAllByUserAndParentId(user, parentId);
    }

    public Directory getDirectoryByUserAndId(User user, UUID id) {
        var directory = directoryRepository.findByIdAndUser(id, user);

        if (directory.isEmpty()) {
            throw new NoSuchElementException();
        }
        return directory.get();
    }

    public void deleteAllForAdmin() {
        directoryRepository.deleteAll();
    }

    public Secret relocateSecret(Secret secret, UUID directoryId, User user) {
        var directory = directoryRepository.findByIdAndUser(directoryId, user);

        if (directory.isEmpty()) {
            throw new NoSuchElementException();
        }

        secret.setDirectory(directory.get());

        return secretRepository.save(secret);
    }

}
