package com.ducthangchin.opal;

import com.ducthangchin.commons.models.opal.OpalRequest;
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

    public OpalService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public boolean allow(OpalRequest request) {
        String url = opalServiceUrl + "/v1/data/app/rbac/allow";
        log.info("opal service url {}", url);

        try {
            Map<String, OpalRequest> requestBody = Map.of("input", request);
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
