package com.doneugene.microservices.services;

import com.doneugene.microservices.dto.DepartmentDTO;
import com.doneugene.microservices.model.Department;

import java.util.List;
import java.util.UUID;

/**
 * @author Don Eugene
 * */

public interface DepartmentService {


    Department saveDepartment(Department department);

    Department fetchDepartmentById(UUID departmentId);

    List<Department> fetchAllDepartments();
}