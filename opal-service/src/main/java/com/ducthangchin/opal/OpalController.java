package com.ducthangchin.opal;


import com.ducthangchin.commons.models.dto.UserDTO;
import com.ducthangchin.commons.models.opal.OpalRequest;
import com.ducthangchin.opal.models.OpalRequestInput;
import com.ducthangchin.opal.models.ResourceInput;
import com.ducthangchin.opal.models.UserInput;
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
    public boolean allow(@RequestBody OpalRequest request) {
        log.info("Checking authorization for request: {}", request);

        UserDTO user = opalService.getUserById(request.getUserId());
        ResourceInput resourceInput = opalService.getResourceInput(request.getResource());

        return opalService.allow(OpalRequestInput.builder()
                .user(new UserInput((user)))
                .action(request.getAction())
                .resource(resourceInput)
                .build());
    }

    //health check
    @GetMapping("/health")
    public String health() {
        return "OK";
    }
}
