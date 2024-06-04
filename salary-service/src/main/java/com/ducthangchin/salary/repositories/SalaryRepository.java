package com.ducthangchin.salary.repositories;

import com.ducthangchin.salary.entities.Salary;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SalaryRepository extends JpaRepository<Salary, Long> {
    List<Salary> findAllByEmployeeId(Long userId);
    List<Salary> findAllByCreatedBy(Long userId);

}
