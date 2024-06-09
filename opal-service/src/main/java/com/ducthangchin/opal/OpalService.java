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
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Component
@Slf4j
public class OpalService {
    @Value("${opal.client.url}")
    private String opalServiceUrl;

    private final RestTemplate restTemplate;
    private final AuthClient authClient;
    private final DocumentClient documentClient;
    private final SalaryClient salaryClient;

    public OpalService(
            RestTemplate restTemplate,
            AuthClient authClient,
            DocumentClient documentClient,
            SalaryClient salaryClient
    ) {
        this.restTemplate = restTemplate;
        this.authClient = authClient;
        this.documentClient = documentClient;
        this.salaryClient = salaryClient;
    }

    public UserDTO getUserById(Long id) {
        return authClient.getProfile(id).getBody();
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
        try {
            DocumentDTO document = documentClient.getDocument(id).getBody();
            if (document != null) {
                resourceInput.setId(document.getId());
                resourceInput.setCreated_by(document.getCreatedBy());
                resourceInput.setBlocked(document.getBlocked());
            }

        } catch (Exception e) {
            log.info("Exception e");
        }
        return resourceInput;
    }

    private SalaryResourceInput getSalaryResourceInput(Long id) {
        SalaryResourceInput resourceInput = new SalaryResourceInput();

        return resourceInput;
    }

    public boolean allow(OpalRequestInput request) {
        String url = opalServiceUrl + "/v1/data/app/rbac/allow";
        log.info("opal service url {}", url);
        log.info("Sending to opal with request input {}", request);

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
