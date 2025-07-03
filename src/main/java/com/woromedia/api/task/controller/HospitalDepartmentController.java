package com.woromedia.api.task.controller;

import com.woromedia.api.task.entity.Department;
import com.woromedia.api.task.service.HospitalDepartmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@RestController
@RequestMapping("/api/hospitals")
@CrossOrigin("*")
public class HospitalDepartmentController {

    @Autowired
    private HospitalDepartmentService hospitalDepartmentService;

    @GetMapping("/{hospitalId}/departments")
    public ResponseEntity<?> getDepartmentsByHospitalId(@PathVariable Long hospitalId) {
        List<Department> departments = hospitalDepartmentService.getDepartmentsByHospitalId(hospitalId);

        // if (departments.isEmpty()) {
        // return ResponseEntity.status(404).body(Map.of(
        // "message", "Hospital not found or no departments available.",
        // "departments", List.of()
        // ));
        // }

        Map<String, Object> response = new HashMap<>();
        response.put("message", "Departments retrieved successfully.");
        response.put("departments", departments);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/departments")
    public ResponseEntity<?> getUniqueHospitalIdsByDepartments(@RequestParam List<Long> departmentIds) {
        if (departmentIds == null || departmentIds.isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("message", "Danh sách department_ids không hợp lệ!"));
        }

        Set<Long> result = hospitalDepartmentService.getUniqueHospitalIdsByDepartmentIds(departmentIds);

        Map<String, Object> response = new HashMap<>();
        response.put("message", "Lấy danh sách hospital_id thành công");
        response.put("data", result);

        return ResponseEntity.ok(response);
    }
}
