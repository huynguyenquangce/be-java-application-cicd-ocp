package com.woromedia.api.task.service;

import com.woromedia.api.task.entity.Appointment;
import com.woromedia.api.task.repository.AppointmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.stream.Collectors;

@Service
public class AppointmentService {

    @Autowired
    private AppointmentRepository appointmentRepository;

    // Tạo lịch hẹn mới
    public Appointment createAppointment(Appointment appointment) {
        return appointmentRepository.save(appointment);
    }

    // Lấy danh sách lịch hẹn theo user_id
    public List<Map<String, Object>> getAppointmentsByUser(int userId) {
        List<Object[]> results = appointmentRepository.findAppointmentsWithDetails(userId);
        List<Map<String, Object>> formattedResults = new ArrayList<>();

        for (Object[] row : results) {
            Map<String, Object> appointment = new HashMap<>();
            appointment.put("appointment_id", row[0]);
            appointment.put("appointment_date", row[1]);
            appointment.put("appointment_time", row[2]);
            appointment.put("status", row[3]);
            appointment.put("notes", row[4]);

            // Hospital object
            Map<String, Object> hospital = new HashMap<>();
            hospital.put("hospital_id", row[5]);
            hospital.put("hospital_name", row[6]);
            appointment.put("hospital", hospital);

            // Department object
            Map<String, Object> department = new HashMap<>();
            department.put("department_id", row[7]);
            department.put("department_name", row[8]);
            appointment.put("department", department);

            formattedResults.add(appointment);
        }

        return formattedResults;
    }

    public List<LocalTime> getAppointmentTimes(int hospital_id, int department_id, LocalDate date) {
        List<Appointment> appointments = appointmentRepository
                .findAppointments(hospital_id, department_id, date);

        return appointments.stream()
                .map(Appointment::getAppointment_time)
                .collect(Collectors.toList());
    }
}
