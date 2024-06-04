package com.ducthangchin.document.controllers;

import com.ducthangchin.clientfeign.opal.OpalCLient;
import com.ducthangchin.commons.models.UserDetails;
import com.ducthangchin.commons.models.opal.*;
import com.ducthangchin.commons.utils.JwtUtils;
import com.ducthangchin.document.dto.DocumentDTO;
import com.ducthangchin.document.entities.Document;
import com.ducthangchin.document.model.DocumentRequest;
import com.ducthangchin.document.services.DocumentService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/document")
@AllArgsConstructor
@Slf4j
public class DocumentController {
    private final DocumentService documentService;
    private final JwtUtils jwtUtils;
    private final OpalCLient opalCLient;

    @GetMapping
    public ResponseEntity<List<DocumentDTO>> getAllDocuments() {
        return ResponseEntity.ok(documentService.getAllDocuments());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Document> getDocument(
            @RequestHeader("Authorization") String token,
            @PathVariable Long id) {
        Document document = documentService.getDocument(id);
        if (document == null) {
            return ResponseEntity.notFound().build();
        }
        try {
            UserDetails userDetails = jwtUtils.extractClaimsWithoutKey(token);
            ResourceInput resource = ResourceInput.builder()
                    .type(ResourceType.document)
                    .id(id)
                    .created_by(document.getCreatedBy())
                    .blocked(document.isBlocked())
                    .build();
            OpalRequest opalRequest = OpalRequest.builder()
                    .user(new OpalUserInput(userDetails))
                    .action(Action.read)
                    .resource(resource)
                    .build();

            log.info("Allowing Opal request: {}", opalRequest);

            if (!opalCLient.allow(opalRequest)) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }
        } catch (Exception e) {
            log.info(e.getMessage());
            return ResponseEntity.internalServerError().body(null);
        }
        return ResponseEntity.ok(document);
    }

    @PostMapping
    public ResponseEntity<Document> createDocument(
            @RequestHeader("Authorization") String token,
            @RequestBody DocumentRequest document) {
        try {
            UserDetails userDetails = jwtUtils.extractClaimsWithoutKey(token);
            OpalRequest opalRequest = OpalRequest.builder()
                    .user(new OpalUserInput(userDetails))
                    .action(Action.create)
                    .resource(new ResourceInput(ResourceType.document))
                    .build();

            log.info("Allowing Opal request: {}", opalRequest);

            if (!opalCLient.allow(opalRequest)) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }
            Document newDocument = Document.builder()
                    .name(document.getName())
                    .content(document.getContent())
                    .createdBy(userDetails.getId())
                    .build();
            return ResponseEntity.ok(documentService.createDocument(newDocument));
        } catch (Exception e) {
            log.info(e.getMessage());
            return ResponseEntity.internalServerError().body(null);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Document> updateDocument(
            @RequestHeader("Authorization") String token,
            @PathVariable Long id,
            @RequestBody DocumentRequest document) {
        try {
            UserDetails userDetails = jwtUtils.extractClaimsWithoutKey(token);
            ResourceInput resource = ResourceInput.builder()
                    .type(ResourceType.document)
                    .id(id)
                    .created_by(userDetails.getId())
                    .build();
            OpalRequest opalRequest = OpalRequest.builder()
                    .user(new OpalUserInput(userDetails))
                    .action(Action.update)
                    .resource(resource)
                    .build();

            log.info("Allowing Opal request: {}", opalRequest);

            if (!opalCLient.allow(opalRequest)) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }
            Document updatedDocument = Document.builder()
                    .id(id)
                    .name(document.getName())
                    .content(document.getContent())
                    .createdBy(userDetails.getId())
                    .build();
            return ResponseEntity.ok(documentService.updateDocument(updatedDocument));
        } catch (Exception e) {
            log.info(e.getMessage());
            return ResponseEntity.internalServerError().body(null);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDocument(
            @RequestHeader("Authorization") String token,
            @PathVariable Long id) {
        try {
            UserDetails userDetails = jwtUtils.extractClaimsWithoutKey(token);
            ResourceInput resource = ResourceInput.builder()
                    .type(ResourceType.document)
                    .id(id)
                    .created_by(userDetails.getId())
                    .build();
            OpalRequest opalRequest = OpalRequest.builder()
                    .user(new OpalUserInput(userDetails))
                    .action(Action.delete)
                    .resource(resource)
                    .build();

            log.info("Allowing Opal request: {}", opalRequest);

            if (!opalCLient.allow(opalRequest)) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }
            documentService.deleteDocument(id);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            log.info(e.getMessage());
            return ResponseEntity.internalServerError().build();
        }
    }

    @PutMapping("/{id}/block")
    public ResponseEntity<Document> blockDocument(
            @RequestHeader("Authorization") String token,
            @PathVariable Long id,
            @RequestBody Boolean block
    ) {
        try {
            UserDetails userDetails = jwtUtils.extractClaimsWithoutKey(token);
            ResourceInput resource = ResourceInput.builder()
                    .type(ResourceType.document)
                    .id(id)
                    .created_by(userDetails.getId())
                    .build();
            OpalRequest opalRequest = OpalRequest.builder()
                    .user(new OpalUserInput(userDetails))
                    .action(Action.access_control)
                    .resource(resource)
                    .build();

            log.info("Allowing Opal request: {}", opalRequest);

            if (!opalCLient.allow(opalRequest)) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }
            return ResponseEntity.ok(documentService.blockDocument(id, block));
        } catch (Exception e) {
            log.info(e.getMessage());
            return ResponseEntity.internalServerError().body(null);
        }
    }


}
