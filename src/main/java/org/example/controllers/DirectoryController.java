package org.example.controllers;

import org.example.congigurations.AppSettings;
import org.example.models.dao.request.DirectoryRequest;
import org.example.models.dao.request.SecretRequest;
import org.example.models.dao.response.DirectoryResponse;
import org.example.models.dao.response.SecretResponse;
import org.example.models.dao.transport.DirectoryTransport;
import org.example.models.entities.Directory;
import org.example.models.entities.Secret;
import org.example.models.entities.User;
import org.example.services.dataAccess.DirectoryDataAccess;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/resource/directory")
public class DirectoryController {

    final private DirectoryDataAccess directoryDataAccess;

    @Autowired
    public DirectoryController(DirectoryDataAccess directoryDataAccess) {
        this.directoryDataAccess = directoryDataAccess;
    }

    @PostMapping("/create")
    public ResponseEntity<DirectoryResponse> createNewSecret(@AuthenticationPrincipal User user, @RequestBody DirectoryRequest request) {
        try {
            if (request.getName() == null) {
                var badResponse = new DirectoryResponse();
                return new ResponseEntity<>(badResponse, HttpStatus.BAD_REQUEST);
            }

            var directoryTransport = new DirectoryTransport();
            directoryTransport.setUser(user);
            directoryTransport.setName(request.getName());
            directoryTransport.setParentId(request.getParentId());
            directoryTransport.setRoot(request.getParentId() == null);

            var directoryCreated = directoryDataAccess.addDirectory(directoryTransport);
            return new ResponseEntity<>(new DirectoryResponse(directoryCreated), HttpStatus.CREATED);
        } catch (Exception ex) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
