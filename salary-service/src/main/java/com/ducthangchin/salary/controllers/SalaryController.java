package com.ducthangchin.salary.controllers;

import com.ducthangchin.clientfeign.opal.OpalCLient;
import com.ducthangchin.commons.models.UserDetails;
import com.ducthangchin.commons.models.opal.*;
import com.ducthangchin.commons.utils.JwtUtils;
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
    private final OpalCLient opalCLient;
    private final JwtUtils jwtUtils;
    private final SalaryService salaryService;

    //health check
    @GetMapping(value = "/health-check")
    public String healthCheck() {
        log.info("Salary service is up and running");
        return "Salary service is up and running";
    }

    //get all salaries
    @GetMapping
    public ResponseEntity<List<Salary>> getAllSalaries(
            @RequestHeader("Authorization") String token,
            @RequestParam(value = "managed", required = false, defaultValue = "false") boolean managed
    ) {
        UserDetails userDetails = jwtUtils.extractClaimsWithoutKey(token);
        try {
            OpalUserInput opalUserInput = new OpalUserInput(userDetails);
            if (!opalCLient.allow(OpalRequest.builder()
                    .user(opalUserInput)
                    .action(Action.read)
                    .resource(new ResourceInput(ResourceType.salary))
                    .build())) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        if (managed) {
            return ResponseEntity.ok(salaryService.getSalariesByAccountant(userDetails.getId()));
        }
        return ResponseEntity.ok(salaryService.getUserSalaries(userDetails.getId()));
    }


    //get salary by id
    @GetMapping(value = "/{id}")
    public ResponseEntity<Salary> getSalaryById(
            @RequestHeader("Authorization") String token,
            @PathVariable Long id
    ) {
        Salary salary = salaryService.getSalaryById(id);

        if (salary == null) {
            return ResponseEntity.notFound().build();
        }

        UserDetails userDetails = jwtUtils.extractClaimsWithoutKey(token);
        if (!salary.getEmployeeId().equals(userDetails.getId()) || !salary.getCreatedBy().equals(userDetails.getId())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        try {
            OpalUserInput opalUserInput = new OpalUserInput(userDetails);
            if (!opalCLient.allow(OpalRequest.builder()
                    .user(opalUserInput)
                    .action(Action.read)
                    .resource(new ResourceInput(ResourceType.salary))
                    .build())) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        return ResponseEntity.ok(salary);
    }

    //create salary
    @PostMapping
    public ResponseEntity<Salary> createSalary(
            @RequestHeader("Authorization") String token,
            @RequestBody SalaryInput salaryInput
    ) {
        UserDetails userDetails = jwtUtils.extractClaimsWithoutKey(token);
        try {
            OpalUserInput opalUserInput = new OpalUserInput(userDetails);
            if (!opalCLient.allow(OpalRequest.builder()
                    .user(opalUserInput)
                    .action(Action.create)
                    .resource(new ResourceInput(ResourceType.salary))
                    .build())) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }
        } catch (Exception e) {
            log.info(e.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        return ResponseEntity.ok(salaryService.createSalary(salaryInput, userDetails));
    }

    //update salary
    @PutMapping(value = "/{id}")
    public ResponseEntity<Salary> updateSalary(
            @RequestHeader("Authorization") String token,
            @PathVariable Long id,
            @RequestBody SalaryUpdateInput salaryInput
    ) {
        Salary salary = salaryService.getSalaryById(id);
        if (salary == null) {
            return ResponseEntity.notFound().build();
        }

        UserDetails userDetails = jwtUtils.extractClaimsWithoutKey(token);

        try {
            OpalUserInput opalUserInput = new OpalUserInput(userDetails);
            ResourceInput resource = ResourceInput.builder()
                    .type(ResourceType.salary)
                    .id(id)
                    .created_by(userDetails.getId())
                    .build();
            if (!opalCLient.allow(OpalRequest.builder()
                    .user(opalUserInput)
                    .action(Action.update)
                    .resource(resource)
                    .build())) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        return ResponseEntity.ok(salaryService.updateSalary(salary, salaryInput, userDetails));
    }

    //delete salary
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Void> deleteSalary(
            @RequestHeader("Authorization") String token,
            @PathVariable Long id
    ) {
        Salary salary = salaryService.getSalaryById(id);
        if (salary == null) {
            return ResponseEntity.notFound().build();
        }

        UserDetails userDetails = jwtUtils.extractClaimsWithoutKey(token);

        try {
            OpalUserInput opalUserInput = new OpalUserInput(userDetails);
            ResourceInput resource = ResourceInput.builder()
                    .type(ResourceType.salary)
                    .id(id)
                    .created_by(userDetails.getId())
                    .build();
            if (!opalCLient.allow(OpalRequest.builder()
                    .user(opalUserInput)
                    .action(Action.delete)
                    .resource(resource)
                    .build())) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        salaryService.deleteSalary(salary);
        return ResponseEntity.ok().build();
    }
}
