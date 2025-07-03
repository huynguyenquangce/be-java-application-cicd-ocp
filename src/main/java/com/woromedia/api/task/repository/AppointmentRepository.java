package com.woromedia.api.task.repository;

import com.woromedia.api.task.entity.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Integer> {

    // Lấy danh sách lịch hẹn theo user_id
    @Query("SELECT a.appointment_id, a.appointment_date, a.appointment_time, a.status, a.notes, " +
            "h.id AS hospital_id, h.name AS hospital_name, " +
            "d.id AS department_id, d.name AS department_name " +
            "FROM Appointment a " +
            "JOIN Hospital h ON a.hospital_id = h.id " +
            "JOIN Department d ON a.department_id = d.id " +
            "WHERE a.userId = :userId")
    List<Object[]> findAppointmentsWithDetails(@Param("userId") int userId);

    @Query("SELECT a FROM Appointment a WHERE a.hospital_id = :hospitalId AND a.department_id = :departmentId AND a.appointment_date = :appointmentDate")
    List<Appointment> findAppointments(
            @Param("hospitalId") int hospitalId,
            @Param("departmentId") int departmentId,
            @Param("appointmentDate") LocalDate appointmentDate);

}
