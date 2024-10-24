package com.accept.controllers;

import com.accept.dto.EmployeeDTO;
import com.accept.entities.Employee;
import com.accept.services.EmployeeService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Validated
@RestController
@RequestMapping("employees")
@Tag(name = "Employees Controller", description = "REST API for managing employees")
public class EmployeeController {
    @Autowired
    private final EmployeeService employeeService;

    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @GetMapping("{id}")
    @Operation(summary = "Get Employee by ID", description = "Retrieve a specific employee by their ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successful operation", content = @Content(mediaType = "application/json", schema = @Schema(implementation = EmployeeDTO.class))),
            @ApiResponse(responseCode = "404", description = "Employee not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<EmployeeDTO> getById(@PathVariable UUID id) {
        EmployeeDTO employee = employeeService.getEmployeeById(id);
        return ResponseEntity.ok(employee);
    }

    @GetMapping
    @Operation(summary = "Get all Employees", description = "Retrieve a list of all employees")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successful operation", content = @Content(mediaType = "application/json", schema = @Schema(implementation = EmployeeDTO.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<List<EmployeeDTO>> getAll() {
        List<EmployeeDTO> employees = employeeService.getAllEmployees();
        return ResponseEntity.ok(employees);
    }

    @PostMapping("/login")
    @Operation(summary = "Authenticate Employee", description = "Authenticate an employee with their credentials")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successful authentication", content = @Content(mediaType = "application/json", schema = @Schema(implementation = EmployeeDTO.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized: Invalid credentials"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<EmployeeDTO> authenticateEmployee(@RequestBody Optional<EmployeeDTO> employeeDTO) {

        return employeeService.authenticateEmployee(employeeDTO)
                .map(resposta -> ResponseEntity.status(HttpStatus.OK).body(resposta))
                .orElse(ResponseEntity.status(HttpStatus.UNAUTHORIZED).build());
    }

    @PostMapping("/create")
    @Operation(summary = "Create Employee", description = "Register a new employee with the provided details")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Employee created successfully", content = @Content(mediaType = "application/json", schema = @Schema(implementation = EmployeeDTO.class))),
            @ApiResponse(responseCode = "400", description = "Bad request: Invalid input"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<Employee> registerEmployee (@RequestBody @Valid Employee employee) {

        return employeeService.registerEmployee(employee)
                .map(resposta -> ResponseEntity.status(HttpStatus.CREATED).body(resposta))
                .orElse(ResponseEntity.status(HttpStatus.BAD_REQUEST).build());

    }

    @PutMapping("/update/{id}")
    @Operation(summary = "Update an Employee", description = "Update the details of an existing employee")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Employee updated", content = @Content(mediaType = "application/json", schema = @Schema(implementation = EmployeeDTO.class))),
            @ApiResponse(responseCode = "404", description = "Employee not found"),
            @ApiResponse(responseCode = "400", description = "Bad request"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<Employee> updateEmployee(@Valid @RequestBody Employee employee) {

        return employeeService.updateEmployee(employee)
                .map(resposta -> ResponseEntity.status(HttpStatus.OK).body(resposta))
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());

    }

    @DeleteMapping("{id}")
    @Operation(summary = "Delete an Employee", description = "Deletes a specific employee by their ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Employee deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Employee not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<String> delete(@PathVariable UUID id) {
        employeeService.deleteEmployee(id);
        String message = "Employee with ID " + id + " deleted successfully.";
        return ResponseEntity.status(HttpStatus.OK).body(message);
    }

}
