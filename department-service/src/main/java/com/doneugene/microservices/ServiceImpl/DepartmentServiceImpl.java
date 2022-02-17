package com.doneugene.microservices.ServiceImpl;

/**
 * @author Don Eugene
 * */

import com.doneugene.microservices.dto.DepartmentDTO;
import com.doneugene.microservices.model.Department;
import com.doneugene.microservices.repository.DepartmentRepository;
import com.doneugene.microservices.services.DepartmentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import java.util.UUID;


@Service
@RequiredArgsConstructor
@Slf4j
public class DepartmentServiceImpl implements DepartmentService {

    private final DepartmentRepository departmentRepository;


    @Override
    public Department saveDepartment(Department department) {
        try {
            log.info("Inside saveDepartment method.");
            return departmentRepository.save(department);
        } catch (Exception e) {
            log.info("Error saving department, cause -> {}, message -> {}", e.getCause(), e.getMessage());
            return null;
        }
    }

    @Override
    public Department fetchDepartmentById(UUID departmentId) {
        try {
            Optional<Department> department = departmentRepository.findById(departmentId);
            return department.orElse(null);
        } catch (Exception e) {
            log.info("Error fetching department by ID, cause -> {}, message -> {}", e.getCause(), e.getMessage());
            return null;
        }
    }

    @Override
    public List<Department> fetchAllDepartments() {
        return departmentRepository.findAll();
    }


}
