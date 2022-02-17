package com.doneugene.microservices.dto;


import com.doneugene.microservices.model.Department;
import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class DepartmentDTO {

    private UUID id;
    private String departmentName;
    private String departmentAddress;
    private String departmentCode;

}