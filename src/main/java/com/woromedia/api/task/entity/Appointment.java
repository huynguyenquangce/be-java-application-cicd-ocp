package com.woromedia.api.task.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "appointments")
public class Appointment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int appointment_id;

    @Column(name = "user_id")
    private int userId;
    private int hospital_id;
    private int department_id;
    private LocalDate appointment_date;
    private LocalTime appointment_time;
    private String status;
    private String notes;

    @Column(nullable = false, updatable = false)
    private LocalDateTime created_at = LocalDateTime.now();
}
