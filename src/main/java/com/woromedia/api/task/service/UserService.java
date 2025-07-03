package com.woromedia.api.task.service;

import com.woromedia.api.task.entity.User;
import com.woromedia.api.task.payload.SignUpDTO;

public interface UserService {
    void saveUser(SignUpDTO signUpDTO);

    User findUserByEmail(String email);

    User findUserById(Long id);

    User updateUser(Long id, User updatedUser);

    User deleteUserById(Long id);

    User createUser(SignUpDTO signUpDTO);

}
