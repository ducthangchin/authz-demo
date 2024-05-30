package com.ducthangchin.clientfeign.salary;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(name = "SALARY")
public interface SalaryClient {
    //health check
    @GetMapping(value = "/api/v1/salary/health-check")
    public String healthCheck();
}
