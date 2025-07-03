package com.woromedia.api.task.payload;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Data
public class SignUpDTO {


    @NotNull(message = "Please enter a valid username.")
    private String username;

    @NotNull(message = "Please enter a valid email.")
    @Email
    private String email;

    @NotEmpty(message = "Please enter a valid password.")
    private String password;

    private String fullname;
    private String phone;
    private String address;
    private String gender;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
    @DateTimeFormat(pattern = "dd/MM/yyyy")
    private LocalDate dob;
}
