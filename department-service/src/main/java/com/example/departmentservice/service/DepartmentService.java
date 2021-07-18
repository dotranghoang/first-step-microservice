package com.example.departmentservice.service;

import com.example.departmentservice.entity.Department;
import com.example.departmentservice.repository.DepartmentReporsitory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class DepartmentService {
    @Autowired
    private DepartmentReporsitory departmentReporsitory;

    public Department saveDepartment(Department department) {
        log.info("saveDepartment serivice");
        return departmentReporsitory.save(department);
    }

    public Department findDepartmentById(Long departmentID) {
        return departmentReporsitory.findByDepartmentId(departmentID);
    }
}
