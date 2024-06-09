package com.ducthangchin.document.repositories;

import com.ducthangchin.commons.models.dto.DocumentDTO;
import com.ducthangchin.document.entities.Document;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DocumentRepository extends JpaRepository<Document, Long> {
    @Query("SELECT new com.ducthangchin.commons.models.dto.DocumentDTO(d.id, d.name, d.blocked, d.createdBy, d.createdByUsername, d.createdAt, d.updatedAt) FROM Document d ORDER BY d.updatedAt DESC")
    List<DocumentDTO> findAllDocuments();

    @Query("SELECT new com.ducthangchin.commons.models.dto.DocumentDTO(d.id, d.name, d.blocked, d.createdBy, d.createdByUsername, d.createdAt, d.updatedAt) FROM Document d WHERE d.id = :id")
    DocumentDTO findDocumentDTOById(@Param("id") Long id);

}
