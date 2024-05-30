package com.ducthangchin.salary.controllers;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/salary")
@AllArgsConstructor
@Slf4j
public class SalaryController {
    //health check
    @GetMapping(value = "/health-check")
    public String healthCheck() {
        log.info("Salary service is up and running");
        return "Salary service is up and running";
    }
}
