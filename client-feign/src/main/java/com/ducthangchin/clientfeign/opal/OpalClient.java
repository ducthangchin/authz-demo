package com.ducthangchin.clientfeign.opal;

import com.ducthangchin.commons.models.opal.OpalRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "OPAL")
public interface OpalClient {
    @GetMapping("/api/v1/opal/health")
    public String health();

    @PostMapping("/api/v1/opal/allow")
    public boolean allow(@RequestBody OpalRequest input);
}
