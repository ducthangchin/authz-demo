package com.ducthangchin.document.services;

import com.ducthangchin.document.dto.DocumentDTO;
import com.ducthangchin.document.entities.Document;
import com.ducthangchin.document.model.DocumentRequest;
import com.ducthangchin.document.repositories.DocumentRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class DocumentService {
    private final DocumentRepository documentRepository;

    public Document getDocument(Long id) {
        return documentRepository.findById(id).orElse(null);
    }

    public List<DocumentDTO> getAllDocuments() {
        return documentRepository.findAllDocuments();
    }

    public Document createDocument(Document document) {
        return documentRepository.saveAndFlush(document);
    }

    public Document updateDocument(Document document, DocumentRequest documentRequest){
        if (documentRequest.getName() != null) {
            document.setName(documentRequest.getName());
        }

        if (documentRequest.getContent() != null) {
            document.setContent(documentRequest.getContent());
        }

        return documentRepository.saveAndFlush(document);
    }

    public void deleteDocument(Long id) {
        documentRepository.deleteById(id);
    }

    public Document blockDocument(Long id, boolean block) {
        Document document = documentRepository.findById(id).orElse(null);
        if (document != null) {
            document.setBlocked(block);
            return documentRepository.saveAndFlush(document);
        }
        return null;
    }
}
