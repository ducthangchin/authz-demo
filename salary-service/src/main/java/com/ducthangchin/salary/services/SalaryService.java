package com.ducthangchin.salary.services;

import com.ducthangchin.commons.models.UserDetails;
import com.ducthangchin.commons.models.dto.UserDTO;
import com.ducthangchin.salary.entities.Salary;
import com.ducthangchin.salary.models.SalaryInput;
import com.ducthangchin.salary.models.SalaryUpdateInput;
import com.ducthangchin.salary.repositories.SalaryRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@AllArgsConstructor
public class SalaryService {
    private final SalaryRepository salaryRepository;

    public Salary getSalaryById(Long id) {
        return salaryRepository.findById(id).orElse(null);
    }

    public List<Salary> getUserSalaries(Long userId) {
        return salaryRepository.findAllByEmployeeId(userId);
    }

    public List<Salary> getAllSalaries() {
        return salaryRepository.findAll();
    }

    public List<Salary> getSalariesByAccountant(Long userId) {
        return salaryRepository.findAllByCreatedBy(userId);
    }

    public Salary createSalary(SalaryInput salary, UserDTO user) {
        Salary salaryEntity = Salary.builder()
                .employeeId(salary.getEmployeeId())
                .amount(salary.getAmount())
                .currency(salary.getCurrency())
                .note(salary.getNote())
                .payPeriod(salary.getPayPeriod())
                .accountantName(user.getFullName())
                .createdBy(user.getId())
                .build();
        return salaryRepository.saveAndFlush(salaryEntity);
    }

    public Salary updateSalary(Salary salary, SalaryUpdateInput salaryInput, UserDTO user) {
        if (salaryInput.getAmount() != null) {
            salary.setAmount(salaryInput.getAmount());
        }

        if (salaryInput.getCurrency() != null) {
            salary.setCurrency(salaryInput.getCurrency());
        }

        if (salaryInput.getNote() != null) {
            salary.setNote(salaryInput.getNote());
        }

        if (salaryInput.getPayPeriod() != null) {
            salary.setPayPeriod(salaryInput.getPayPeriod());
        }

        salary.setAccountantName(user.getFullName());
        return salaryRepository.saveAndFlush(salary);
    }

    public void deleteSalary(Salary salary) {
        salaryRepository.delete(salary);
    }

}
