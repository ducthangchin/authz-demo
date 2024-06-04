package com.ducthangchin.document.repositories;

import com.ducthangchin.document.dto.DocumentDTO;
import com.ducthangchin.document.entities.Document;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface DocumentRepository extends JpaRepository<Document, Long> {
    @Query("SELECT new com.ducthangchin.document.dto.DocumentDTO(d.id, d.name, d.blocked, d.createdBy, d.createdAt, d.updatedAt) FROM Document d")
    List<DocumentDTO> findAllDocuments();
}
