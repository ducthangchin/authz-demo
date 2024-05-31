package com.ducthangchin.opal;


import com.ducthangchin.commons.models.opal.OpalRequest;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/opal")
@AllArgsConstructor
@Slf4j
public class OpalController {
    private final OpalService opalService;

    //check authorization
    @PostMapping("/allow")
    public boolean allow(@RequestBody OpalRequest input) {
        log.info("Checking authorization for request: {}", input);
        return opalService.allow(input);
    }

    //health check
    @GetMapping("/health")
    public String health() {
        return "OK";
    }
}
