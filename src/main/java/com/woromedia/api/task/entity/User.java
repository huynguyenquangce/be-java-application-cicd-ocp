package com.woromedia.api.task.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDate;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "users", uniqueConstraints = {
                @UniqueConstraint(columnNames = { "username" }),
                @UniqueConstraint(columnNames = { "email" })
})
public class User {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private long id;

        @Column(nullable = false, unique = true)
        private String username;

        @Column(nullable = false, unique = true)
        private String email;

        @Column(nullable = false)
        private String password;
        private String fullname;
        private String phone;
        private String address;
        @Column(name = "dob")
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
        private LocalDate dob;
        private String gender;

        private LocalDateTime createdAt;

        @PrePersist
        protected void onCreate() {
                this.createdAt = LocalDateTime.now();
        }

        public User(
                        String username,
                        String email,
                        String password,
                        String fullname,
                        String phone,
                        String address,
                        String dob,
                        String gender
        // Set<Role> roles
        ) {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                this.username = username;
                this.email = email;
                this.password = password;
                this.fullname = fullname;
                this.phone = phone;
                this.address = address;
                this.dob = LocalDate.parse(dob, formatter);
                this.gender = gender;
                // this.roles = roles;
                this.createdAt = LocalDateTime.now();
        }
}