package com.woromedia.api.task.controller;

import com.woromedia.api.task.entity.Appointment;
import com.woromedia.api.task.service.AppointmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/appointments")
public class AppointmentController {

    @Autowired
    private AppointmentService appointmentService;

    // API tạo lịch hẹn mới
    @PostMapping("/create")
    public ResponseEntity<?> createAppointment(@RequestBody Appointment appointment) {
        try {
            if (appointment.getUserId() <= 0) {
                return ResponseEntity.badRequest().body("Error: user_id không hợp lệ!");
            }
            if (appointment.getHospital_id() <= 0) {
                return ResponseEntity.badRequest().body("Error: hospital_id không hợp lệ!");
            }
            if (appointment.getAppointment_date() == null) {
                return ResponseEntity.badRequest().body("Error: appointment_date không được để trống!");
            }

            Appointment savedAppointment = appointmentService.createAppointment(appointment);
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Lịch hẹn tạo thanh cong!");
            response.put("data", savedAppointment);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Lỗi hệ thống: " + e.getMessage());
        }
    }

    // API lấy danh sách lịch hẹn theo user_id
    @GetMapping("/user/{user_id}")
    public ResponseEntity<?> getAppointmentsByUser(@PathVariable int user_id) {
        try {
            if (user_id <= 0) {
                return ResponseEntity.badRequest().body("Error: user_id không hợp lệ!");
            }

            List<Map<String, Object>> appointments = appointmentService.getAppointmentsByUser(user_id);
            if (appointments.isEmpty()) {
                return ResponseEntity.ok("Không có lịch hẹn nào cho user_id: " + user_id);
            }

            return ResponseEntity.ok(appointments);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Lỗi hệ thống: " + e.getMessage());
        }
    }

    @GetMapping("/times")
    public Map<String, Object> getAvailableTimes(
            @RequestParam int hospital_id,
            @RequestParam int department_id,
            @RequestParam String appointment_date) {

        LocalDate date = LocalDate.parse(appointment_date);
        List<LocalTime> times = appointmentService.getAppointmentTimes(hospital_id, department_id, date);

        Map<String, Object> response = new HashMap<>();
        if (times.isEmpty()) {
            response.put("message", "Không có lịch hẹn nào vào ngày này.");
        } else {
            response.put("message", "Danh sách thời gian đã đặt.");
        }
        response.put("times", times);

        return response;
    }
}
