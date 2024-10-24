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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

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

    @Autowired
    private JwtService jwtService;

    @Autowired
    private AuthenticationManager authenticationManager;

    public EmployeeService(EmployeeRepository employeeRepository, ModelMapper modelMapper) {
        this.employeeRepository = employeeRepository;
        this.modelMapper = modelMapper;
    }

    @Transactional(readOnly = true)
    public EmployeeDTO getEmployeeById(UUID employeeId) {
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new EntityNotFoundException("Employee not found: " + employeeId));
        return modelMapper.map(employee, EmployeeDTO.class);
    }

    public Optional<Employee> registerEmployee(Employee employee) {

        if (employeeRepository.findByEmployeeEmail(employee.getEmployeeEmail()).isPresent())
            return Optional.empty();

        employee.setPassword(criptografarSenha(employee.getPassword()));

        return Optional.of(employeeRepository.save(employee));

    }

    public Optional<Employee> updateEmployee(Employee employee) {

        if (employeeRepository.findById(employee.getEmployeeId()).isPresent()) {

            Optional<Employee> employeeSearch = employeeRepository.findByEmployeeEmail(employee.getEmployeeEmail());

            if ((employeeSearch.isPresent()) && (employeeSearch.get().getEmployeeId() != employee.getEmployeeId()))
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "employee already exists!", null);

            employee.setPassword(criptografarSenha(employee.getPassword()));

            return Optional.ofNullable(employeeRepository.save(employee));

        }

        return Optional.empty();

    }

    public Optional<EmployeeDTO> authenticateEmployee(Optional<EmployeeDTO> employeeDTO) {

        var credenciais = new UsernamePasswordAuthenticationToken(employeeDTO.get().getEmployeeEmail(), employeeDTO.get().getPassword());

        Authentication authentication = authenticationManager.authenticate(credenciais);

        if (authentication.isAuthenticated()) {

            Optional<Employee> employee = employeeRepository.findByEmployeeEmail(employeeDTO.get().getEmployeeEmail());

            if (employee.isPresent()) {

                employeeDTO.get().setEmployeeId(employee.get().getEmployeeId());
                employeeDTO.get().setEmployeeName(employee.get().getEmployeeName());
                employeeDTO.get().setPosition(employee.get().getPosition());
                employeeDTO.get().setToken(gerarToken(employeeDTO.get().getEmployeeEmail()));
                employeeDTO.get().setPassword("");

                return employeeDTO;

            }

        }

        return Optional.empty();

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

    public void deleteEmployee(UUID id) {
        if (employeeRepository.existsById(id)) {
            employeeRepository.deleteById(id);
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuário não encontrado!");
        }
    }


    private String criptografarSenha(String senha) {

        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

        return encoder.encode(senha);

    }

    private String gerarToken(String usuario) {
        return "Bearer " + jwtService.generateToken(usuario);
    }

    private void validateEmployee(EmployeeDTO employeeDTO) {
        Optional<Employee> existingEmployee = employeeRepository.findByEmployeeEmail(employeeDTO.getEmployeeEmail());
        if (existingEmployee.isPresent()) {
            throw new IllegalArgumentException("Employee with the same email already exists");
        }
    }
}