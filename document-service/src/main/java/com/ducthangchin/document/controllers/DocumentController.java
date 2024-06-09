package com.ducthangchin.document.controllers;

import com.ducthangchin.clientfeign.opal.OpalClient;
import com.ducthangchin.clientfeign.user.AuthClient;
import com.ducthangchin.commons.models.dto.UserDTO;
import com.ducthangchin.commons.models.opal.*;
import com.ducthangchin.commons.utils.JwtUtils;
import com.ducthangchin.commons.models.dto.DocumentDTO;
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
    private final OpalClient opalCLient;
    private final AuthClient authClient;

    @GetMapping
    public ResponseEntity<List<DocumentDTO>> getAllDocuments() {
        return ResponseEntity.ok(documentService.getAllDocuments());
    }

    @GetMapping("/dto/{id}")
    public ResponseEntity<DocumentDTO> getDocument(@PathVariable Long id) {
        DocumentDTO document = documentService.getDocumentDTO(id);
        if (document == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(document);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Document> getDocument(
            @RequestHeader("X-user-id") Long userId,
            @PathVariable Long id
    ) {
        Document document = documentService.getDocument(id);
        if (document == null) {
            return ResponseEntity.notFound().build();
        }

        try {
            OpalRequest opalRequest = OpalRequest.builder()
                    .userId(userId)
                    .action(Action.read)
                    .resource(new Resource(ResourceType.document, id))
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
            @RequestHeader("X-user-id") Long userId,
            @RequestBody DocumentRequest document
    ) {
        try {
            OpalRequest opalRequest = OpalRequest.builder()
                    .userId(userId)
                    .action(Action.create)
                    .resource(new Resource(ResourceType.document))
                    .build();

            log.info("Allowing Opal request: {}", opalRequest);

            if (!opalCLient.allow(opalRequest)) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }
        } catch (Exception e) {
            log.info(e.getMessage());
            return ResponseEntity.internalServerError().body(null);
        }

        UserDTO userDetails = authClient.getProfile(userId).getBody();
        Document newDocument = Document.builder()
                .name(document.getName())
                .content(document.getContent())
                .createdBy(userDetails.getId())
                .createdByUsername(userDetails.getFullName())
                .build();
        return ResponseEntity.ok(documentService.createDocument(newDocument));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Document> updateDocument(
            @RequestHeader("X-user-id") Long userId,
            @PathVariable Long id,
            @RequestBody DocumentRequest document
    ) {
        Document updatedDocument = documentService.getDocument(id);

        if (updatedDocument == null) {
            return ResponseEntity.notFound().build();
        }
        try {
            OpalRequest opalRequest = OpalRequest.builder()
                    .userId(userId)
                    .action(Action.update)
                    .resource(new Resource(ResourceType.document, id))
                    .build();

            log.info("Allowing Opal request: {}", opalRequest);

            if (!opalCLient.allow(opalRequest)) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }
        } catch (Exception e) {
            log.info(e.getMessage());
            return ResponseEntity.internalServerError().body(null);
        }

        UserDTO userDetails = authClient.getProfile(userId).getBody();

        if (userDetails != null) {
            updatedDocument.setCreatedByUsername(userDetails.getFullName());
        }
        return ResponseEntity.ok(documentService.updateDocument(updatedDocument, document));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDocument(
            @RequestHeader("X-user-id") Long userId,
            @PathVariable Long id
    ) {
        Document document = documentService.getDocument(id);
        if (document == null) {
            return ResponseEntity.notFound().build();
        }

        try {
            OpalRequest opalRequest = OpalRequest.builder()
                    .userId(userId)
                    .action(Action.delete)
                    .resource(new Resource(ResourceType.document, id))
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
            @RequestHeader("X-user-id") Long userId,
            @PathVariable Long id,
            @RequestBody Boolean block) {
        try {
            Document document = documentService.getDocument(id);
            if (document == null) {
                return ResponseEntity.notFound().build();
            }

            OpalRequest opalRequest = OpalRequest.builder()
                    .userId(userId)
                    .action(Action.access_control)
                    .resource(new Resource(ResourceType.document, id))
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
