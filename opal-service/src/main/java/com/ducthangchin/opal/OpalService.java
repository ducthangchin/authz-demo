package com.ducthangchin.opal;

import com.ducthangchin.clientfeign.document.DocumentClient;
import com.ducthangchin.clientfeign.salary.SalaryClient;
import com.ducthangchin.clientfeign.user.AuthClient;
import com.ducthangchin.commons.models.dto.DocumentDTO;
import com.ducthangchin.commons.models.dto.UserDTO;
import com.ducthangchin.commons.models.opal.Resource;
import com.ducthangchin.opal.models.DocumentResourceInput;
import com.ducthangchin.opal.models.OpalRequestInput;
import com.ducthangchin.opal.models.ResourceInput;
import com.ducthangchin.opal.models.SalaryResourceInput;
import feign.FeignException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;
import java.util.Map;

@Component
@Slf4j
public class OpalService {
    @Value("${opal.client.url}")
    private String opalServiceUrl;

    @Value("${redis.cache.ttl}")
    private Integer cacheTtl;

    private final RestTemplate restTemplate;
    private final AuthClient authClient;
    private final DocumentClient documentClient;
    private final SalaryClient salaryClient;
    private final RedisTemplate<String, Object> redisTemplate;

    public OpalService(
            RestTemplate restTemplate,
            AuthClient authClient,
            DocumentClient documentClient,
            SalaryClient salaryClient,
            RedisTemplate<String, Object> redisTemplate
    ) {
        this.restTemplate = restTemplate;
        this.authClient = authClient;
        this.documentClient = documentClient;
        this.salaryClient = salaryClient;
        this.redisTemplate = redisTemplate;
    }

    public UserDTO getUserById(Long id) {
        String key = "user-" + id;

        UserDTO user = (UserDTO) redisTemplate.opsForValue().get(key);
        if (user != null) {
            log.info("Get user with id {} in cache", id);
            return user;
        }

        try {
            ResponseEntity<UserDTO> response = authClient.getProfile(id);
            user = response.getBody();

            if (user != null) {
                redisTemplate.opsForValue().set(key, user, Duration.ofSeconds(cacheTtl));
                log.info("Stored user with id {} in cache", id);
            }
        } catch (FeignException e) {
            if (e.status() == 404) {
                log.info("User with id {} not found", id);
                return null;
            }
        } catch (Exception e) {
            log.error("Error fetching user with id {} from auth client", id);
            throw new RuntimeException("Failed to fetch user", e);
        }
        return user;
    }

    public ResourceInput getResourceInput(Resource resource) {
        switch (resource.getType()) {
            case salary -> {
                return getSalaryResourceInput(resource.getId());
            }
            case document -> {
                return getDocumentResourceInput(resource.getId());
            }
            case profile -> {
                // implement later
                return null;
            }
            default -> {
                return null;
            }
        }
    }

    private DocumentResourceInput getDocumentResourceInput(Long id) {
        DocumentResourceInput resourceInput = new DocumentResourceInput();
        String key = "document-" + id;
        DocumentDTO document = (DocumentDTO) redisTemplate.opsForValue().get(key);
        if (document == null) {
            try {
                document = documentClient.getDocument(id).getBody();
                if (document != null) {
                    redisTemplate.opsForValue().set(key, document, Duration.ofSeconds(cacheTtl));
                    log.info("Stored document with id {} in cache", id);
                }
            } catch (Exception e) {
                log.info("Exception {}", e.getMessage());
            }
        }
        if (document != null) {
            resourceInput.setId(document.getId());
            resourceInput.setCreated_by(document.getCreatedBy());
            resourceInput.setBlocked(document.getBlocked());
        }
        return resourceInput;
    }

    private SalaryResourceInput getSalaryResourceInput(Long id) {
        SalaryResourceInput resourceInput = new SalaryResourceInput();

        return resourceInput;
    }

    public boolean allow(OpalRequestInput request) {
        String url = opalServiceUrl + "/v1/data/app/rbac/allow";
//        log.info("opal service url {}", url);
//        log.info("Sending to opal with request input {}", request);

        try {
            Map<String, OpalRequestInput> requestBody = Map.of("input", request);
            Map<String, Boolean> response = restTemplate.postForObject(url, requestBody, Map.class);
            if (response == null) {
                log.error("Received null response from Opal service");
                throw new IllegalStateException("Received null response from Opal service");
            }
            Boolean result = response.get("result");
            if (result == null) {
                log.error("Response does not contain 'result' key");
                throw new IllegalStateException("Response does not contain 'result' key");
            }
            return result;
        } catch (Exception e) {
            throw e;
        }
    }
}
