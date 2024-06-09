package com.ducthangchin.clientfeign.document;

import com.ducthangchin.commons.models.dto.DocumentDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "DOCUMENT")
public interface DocumentClient {
    @GetMapping("/api/v1/document/dto/{id}")
    public ResponseEntity<DocumentDTO> getDocument(@PathVariable(value = "id") Long id);
}