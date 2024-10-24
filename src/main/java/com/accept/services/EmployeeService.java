package com.accept.services;

import com.accept.dto.EmployeeDTO;
import com.accept.entities.Employee;
import com.accept.repositories.EmployeeRepository;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.persistence.EntityNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Validated
@Tag(name = "Employee Service", description = "Service layer for managing employee")
public class EmployeeService {
    @Autowired
    private final EmployeeRepository employeeRepository;
    private final ModelMapper modelMapper;

    public EmployeeService(EmployeeRepository employeeRepository, ModelMapper modelMapper) {
        this.employeeRepository = employeeRepository;
        this.modelMapper = modelMapper;
    }

    @Transactional(readOnly = true)
    public List<EmployeeDTO> getAllEmployees() {
        List<Employee> employees = employeeRepository.findAll();
        if (employees.isEmpty()) {
            throw new EntityNotFoundException("No employees found.");
        }
        return employees.stream()
                .map(employee -> modelMapper.map(employee, EmployeeDTO.class))
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public EmployeeDTO getEmployeeById(UUID employeeId) {
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new EntityNotFoundException("Employee not found: " + employeeId));
        return modelMapper.map(employee, EmployeeDTO.class);
    }

    @Transactional
    public EmployeeDTO createEmployee(EmployeeDTO employeeDTO) {
        validateEmployee(employeeDTO);
        Employee employee = modelMapper.map(employeeDTO, Employee.class);
        employee.onCreate();
        Employee savedEmployee = employeeRepository.save(employee);
        return modelMapper.map(savedEmployee, EmployeeDTO.class);
    }

    @Transactional
    public EmployeeDTO updateEmployee(UUID employeeId, EmployeeDTO employeeDTO) {
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new EntityNotFoundException("Employee not found: " + employeeId));
        employee.setEmployeeName(employeeDTO.getEmployeeName());
        employee.setEmployeeEmail(employeeDTO.getEmployeeEmail());
        employee.setPassword(employeeDTO.getPassword());
        employee.setPosition(employeeDTO.getPosition());
        employee.onUpdate();
        Employee updatedEmployee = employeeRepository.save(employee);
        return modelMapper.map(updatedEmployee, EmployeeDTO.class);
    }

    @Transactional
    public void deleteEmployee(UUID employeeId) {
        Optional<Employee> employeeOptional = employeeRepository.findById(employeeId);
        if (employeeOptional.isPresent()) {
            employeeRepository.delete(employeeOptional.get());
        } else {
            throw new EntityNotFoundException("Student not found with id: " + employeeId);
        }
    }

    private void validateEmployee(EmployeeDTO employeeDTO) {
        Optional<Employee> existingEmployee = employeeRepository.findByEmployeeEmail(employeeDTO.getEmployeeEmail());
        if (existingEmployee.isPresent()) {
            throw new IllegalArgumentException("Employee with the same email already exists");
        }
    }
}