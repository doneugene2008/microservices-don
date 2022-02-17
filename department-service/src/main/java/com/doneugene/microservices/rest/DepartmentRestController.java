package com.doneugene.microservices.rest;

/**
 * @author Don Eugene
 * */

import com.doneugene.microservices.dto.DepartmentDTO;
import com.doneugene.microservices.model.Department;
import com.doneugene.microservices.services.DepartmentService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/departments")
@Slf4j
public class DepartmentRestController {

    private final DepartmentService departmentService;


    @PostMapping(value = "/save_department", consumes = {MediaType.APPLICATION_JSON_VALUE}, produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<?> saveDepartment(@RequestBody Department department) throws JsonProcessingException {
        log.info("Post Request Payload: {}", new ObjectMapper().writer().withDefaultPrettyPrinter().writeValueAsString(department));

        var _department = departmentService.saveDepartment(department);

        if (_department != null) return new ResponseEntity<>(_department, HttpStatus.CREATED);
        else return new ResponseEntity<>("Error Saving Department", HttpStatus.NOT_MODIFIED);
    }


    @GetMapping(value = "/{departmentId}", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<?> fetchDepartmentById(@PathVariable UUID departmentId){
        log.info("Fetch Department by ID: {}", departmentId);

        Department _department = departmentService.fetchDepartmentById(departmentId);

        if (_department != null)return new ResponseEntity<>(_department, HttpStatus.OK);
        else return new ResponseEntity<>("No Department by ID " + departmentId, HttpStatus.NO_CONTENT);
    }


    @GetMapping(produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<?> fetchAllDepartments(){
        log.info("Fetch All Departments");

        List<Department> departments = departmentService.fetchAllDepartments();

        if (!departments.isEmpty())return new ResponseEntity<>(departments, HttpStatus.OK);
        else return new ResponseEntity<>("No Departments", HttpStatus.NO_CONTENT);
    }

}