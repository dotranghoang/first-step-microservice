package com.example.departmentservice.repository;

import com.example.departmentservice.entity.Department;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DepartmentReporsitory extends JpaRepository<Department, Long> {
    Department findByDepartmentId(Long deparmentID);
}
