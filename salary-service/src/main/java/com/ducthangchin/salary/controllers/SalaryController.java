package com.ducthangchin.salary.controllers;

import com.ducthangchin.clientfeign.opal.OpalClient;
import com.ducthangchin.clientfeign.user.AuthClient;
import com.ducthangchin.commons.models.dto.UserDTO;
import com.ducthangchin.commons.models.opal.*;
import com.ducthangchin.salary.entities.Salary;
import com.ducthangchin.salary.models.SalaryInput;
import com.ducthangchin.salary.models.SalaryUpdateInput;
import com.ducthangchin.salary.services.SalaryService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/salary")
@AllArgsConstructor
@Slf4j
public class SalaryController {
    private final OpalClient opalCLient;
    private final SalaryService salaryService;
    private final AuthClient authClient;

    //health check
    @GetMapping(value = "/health-check")
    public String healthCheck() {
        log.info("Salary service is up and running");
        return "Salary service is up and running";
    }

    //get all salaries
    @GetMapping
    public ResponseEntity<List<Salary>> getAllSalaries(
            @RequestHeader("X-user-id") Long userId,
            @RequestParam(value = "managed", required = false, defaultValue = "false") boolean managed
    ) {
        try {
            OpalRequest opalRequest = OpalRequest.builder()
                    .userId(userId)
                    .action(Action.read)
                    .resource(new Resource(ResourceType.salary))
                    .build();

            log.info("Allowing Opal request: {}", opalRequest);

            if (!opalCLient.allow(opalRequest)) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }
        } catch (Exception e) {
            log.info(e.getMessage());
            return ResponseEntity.internalServerError().body(null);
        }

        if (managed) {
            return ResponseEntity.ok(salaryService.getSalariesByAccountant(userId));
        }
        return ResponseEntity.ok(salaryService.getUserSalaries(userId));
    }


    //get salary by id
    @GetMapping(value = "/{id}")
    public ResponseEntity<Salary> getSalaryById(
            @RequestHeader("X-user-id") Long userId,
            @PathVariable Long id
    ) {
        Salary salary = salaryService.getSalaryById(id);

        if (salary == null) {
            return ResponseEntity.notFound().build();
        }

        try {
            OpalRequest opalRequest = OpalRequest.builder()
                    .userId(userId)
                    .action(Action.read)
                    .resource(new Resource(ResourceType.salary))
                    .build();

            log.info("Allowing Opal request: {}", opalRequest);

            if (!opalCLient.allow(opalRequest)) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }
        } catch (Exception e) {
            log.info(e.getMessage());
            return ResponseEntity.internalServerError().body(null);
        }

        return ResponseEntity.ok(salary);
    }

    //create salary
    @PostMapping
    public ResponseEntity<Salary> createSalary(
            @RequestHeader("X-user-id") Long userId,
            @RequestBody SalaryInput salaryInput
    ) {
        try {
            OpalRequest opalRequest = OpalRequest.builder()
                    .userId(userId)
                    .action(Action.create)
                    .resource(new Resource(ResourceType.salary))
                    .build();

            log.info("Allowing Opal request: {}", opalRequest);

            if (!opalCLient.allow(opalRequest)) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }
        } catch (Exception e) {
            log.info(e.getMessage());
            return ResponseEntity.internalServerError().body(null);
        }

        UserDTO userDTO = authClient.getProfile(userId).getBody();

        if (userDTO == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(salaryService.createSalary(salaryInput, userDTO));
    }

    //update salary
    @PutMapping(value = "/{id}")
    public ResponseEntity<Salary> updateSalary(
            @RequestHeader("X-user-id") Long userId,
            @PathVariable Long id,
            @RequestBody SalaryUpdateInput salaryInput
    ) {
        Salary salary = salaryService.getSalaryById(id);
        if (salary == null) {
            return ResponseEntity.notFound().build();
        }

        try {
            OpalRequest opalRequest = OpalRequest.builder()
                    .userId(userId)
                    .action(Action.create)
                    .resource(new Resource(ResourceType.salary))
                    .build();

            log.info("Allowing Opal request: {}", opalRequest);

            if (!opalCLient.allow(opalRequest)) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }
        } catch (Exception e) {
            log.info(e.getMessage());
            return ResponseEntity.internalServerError().body(null);
        }

        UserDTO userDTO = authClient.getProfile(userId).getBody();

        if (userDTO == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(salaryService.updateSalary(salary, salaryInput, userDTO));
    }

    //delete salary
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Void> deleteSalary(
            @RequestHeader("X-user-id") Long userId,
            @PathVariable Long id
    ) {
        Salary salary = salaryService.getSalaryById(id);
        if (salary == null) {
            return ResponseEntity.notFound().build();
        }

        try {
            OpalRequest opalRequest = OpalRequest.builder()
                    .userId(userId)
                    .action(Action.create)
                    .resource(new Resource(ResourceType.salary))
                    .build();

            log.info("Allowing Opal request: {}", opalRequest);

            if (!opalCLient.allow(opalRequest)) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }
        } catch (Exception e) {
            log.info(e.getMessage());
            return ResponseEntity.internalServerError().body(null);
        }

        salaryService.deleteSalary(salary);
        return ResponseEntity.ok().build();
    }
}
