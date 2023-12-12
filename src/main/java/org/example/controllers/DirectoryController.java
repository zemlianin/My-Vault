package org.example.controllers;

import org.example.models.HideVersionOfSecret;
import org.example.models.dao.request.DirectoryRequest;
import org.example.models.dao.response.DirectoryResponse;
import org.example.models.dao.transport.DirectoryTransport;
import org.example.models.entities.User;
import org.example.services.dataAccess.DirectoryDataAccess;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/resource/directory")
public class DirectoryController {

    final private DirectoryDataAccess directoryDataAccess;

    @Autowired
    public DirectoryController(DirectoryDataAccess directoryDataAccess) {
        this.directoryDataAccess = directoryDataAccess;
    }

    @PostMapping("/create")
    public ResponseEntity<DirectoryResponse> createNewSecret(@AuthenticationPrincipal User user,
                                                             @RequestBody DirectoryRequest request) {
        try {
            if (request.getName() == null) {
                var badResponse = new DirectoryResponse();
                return new ResponseEntity<>(badResponse, HttpStatus.BAD_REQUEST);
            }

            var directoryTransport = new DirectoryTransport(user, request);

            var directoryCreated = directoryDataAccess.addDirectory(directoryTransport);
            return new ResponseEntity<>(new DirectoryResponse(directoryCreated), HttpStatus.CREATED);
        } catch (Exception ex) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/get_all")
    public ResponseEntity<List<DirectoryResponse>> createNewSecret(@AuthenticationPrincipal User user,
                                                                   @RequestParam(value = "directoryId", required = false) UUID directoryId) {
        try {
            var collection = directoryDataAccess.getDirectoriesByUserAndParentId(user, directoryId);
            var directories = collection.stream()
                    .map(DirectoryResponse::new)
                    .toList();

            return new ResponseEntity<>(directories, HttpStatus.OK);
        } catch (Exception ex) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
