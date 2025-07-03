package com.woromedia.api.task.service.impl;

import com.woromedia.api.task.entity.User;
import com.woromedia.api.task.repository.UserRepository;
import com.woromedia.api.task.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.woromedia.api.task.payload.SignUpDTO;

import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public void saveUser(SignUpDTO signUpDTO) {
        // Logic để tạo user mới (giữ nguyên code cũ)
    }

    @Override
    public User findUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public User updateUser(Long id, User updatedUser) {
        Optional<User> optionalUser = userRepository.findById(id);

        if (optionalUser.isPresent()) {
            User existingUser = optionalUser.get();

            if (updatedUser.getFullname() != null) {
                existingUser.setFullname(updatedUser.getFullname());
            }
            if (updatedUser.getPhone() != null) {
                existingUser.setPhone(updatedUser.getPhone());
            }
            if (updatedUser.getAddress() != null) {
                existingUser.setAddress(updatedUser.getAddress());
            }
            if (updatedUser.getDob() != null) {
                existingUser.setDob(updatedUser.getDob());
            }
            if (updatedUser.getGender() != null) {
                existingUser.setGender(updatedUser.getGender());
            }

            return userRepository.save(existingUser);
        } else {
            throw new RuntimeException("User not found with id: " + id);
        }
    }

    @Override
    public User deleteUserById(Long id) {
        if (userRepository.existsById(id)) {
            userRepository.deleteById(id);
        } else {
            throw new RuntimeException("User not found with id: " + id);
        }
        return null;
    }

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public User createUser(SignUpDTO signUpDTO) {
        if (userRepository.existsByEmail(signUpDTO.getEmail())) {
            throw new RuntimeException("Email already exists!");
        }

        User user = new User();
        user.setUsername(signUpDTO.getUsername());
        user.setEmail(signUpDTO.getEmail());
        user.setPassword(passwordEncoder.encode(signUpDTO.getPassword()));
        user.setFullname(signUpDTO.getFullname());
        user.setPhone(signUpDTO.getPhone());
        user.setAddress(signUpDTO.getAddress());
        user.setGender(signUpDTO.getGender());
        user.setDob(signUpDTO.getDob());

        return userRepository.save(user);
    }

    @Override
    public User findUserById(Long id) {
        return userRepository.findById(id).orElse(null);
    }
}
