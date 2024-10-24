package com.accept.services;

import com.accept.dto.EmployeeDTO;
import com.accept.entities.Employee;
import com.accept.repositories.EmployeeRepository;
import com.accept.security.JwtService;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.persistence.EntityNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.server.ResponseStatusException;

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

    @Autowired
    private JwtService jwtService;

    @Autowired
    private AuthenticationManager authenticationManager;

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
        employee.setPassword(criptografarSenha(employeeDTO.getPassword())); // Criptografa a senha
        employee.onCreate();
        Employee savedEmployee = employeeRepository.save(employee);
        return modelMapper.map(savedEmployee, EmployeeDTO.class);
    }

    @Transactional
    public EmployeeDTO updateEmployee(UUID employeeId, EmployeeDTO employeeDTO, String token) {
        // Valida o token JWT
        if (!jwtService.isTokenValid(token)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Token JWT inválido ou expirado.");
        }

        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new EntityNotFoundException("Employee not found: " + employeeId));

        employee.setEmployeeName(employeeDTO.getEmployeeName());
        employee.setEmployeeEmail(employeeDTO.getEmployeeEmail());
        employee.setPassword(criptografarSenha(employeeDTO.getPassword())); // Criptografa a senha
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
            throw new EntityNotFoundException("Employee not found with id: " + employeeId);
        }
    }

    public Optional<EmployeeDTO> authenticateEmployee(String email, String password) {
        var credentials = new UsernamePasswordAuthenticationToken(email, password);
        Authentication authentication = authenticationManager.authenticate(credentials);

        if (authentication.isAuthenticated()) {
            Optional<Employee> employee = employeeRepository.findByEmployeeEmail(email);
            if (employee.isPresent()) {
                EmployeeDTO employeeDTO = modelMapper.map(employee.get(), EmployeeDTO.class);
                employeeDTO.setToken(gerarToken(employeeDTO.getEmployeeEmail())); // Gera e atribui o token
                return Optional.of(employeeDTO);
            }
        }
        return Optional.empty();
    }

    private String criptografarSenha(String senha) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        return encoder.encode(senha);
    }

    private String gerarToken(String email) {
        return "Bearer " + jwtService.generateToken(email);
    }

    private void validateEmployee(EmployeeDTO employeeDTO) {
        Optional<Employee> existingEmployee = employeeRepository.findByEmployeeEmail(employeeDTO.getEmployeeEmail());
        if (existingEmployee.isPresent()) {
            throw new IllegalArgumentException("Employee with the same email already exists");
        }
    }
}
