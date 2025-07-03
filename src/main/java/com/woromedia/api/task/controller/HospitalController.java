package com.woromedia.api.task.controller;

import com.woromedia.api.task.entity.Hospital;
import com.woromedia.api.task.service.HospitalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/hospitals")
@CrossOrigin("*")
public class HospitalController {

    @Autowired
    private HospitalService hospitalService;

    @GetMapping
    public ResponseEntity<Map<String, Object>> getAllHospitals() {
        List<Hospital> hospitals = hospitalService.getAllHospitals();

        Map<String, Object> response = new HashMap<>();
        response.put("message", "Danh sách bệnh viện được lấy thành công");
        response.put("data", hospitals);

        return ResponseEntity.ok(response);
    }
}
