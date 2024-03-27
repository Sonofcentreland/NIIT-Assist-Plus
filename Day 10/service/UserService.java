package com.jeeva.service;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.jeeva.dto.request.UserRequest;
import com.jeeva.dto.response.user.UserPartialResponse;
import com.jeeva.model.User;
import com.jeeva.model.enumerate.Role;
import com.jeeva.model.roles.Admin;
import com.jeeva.model.roles.Staff;
import com.jeeva.model.roles.Student;
import com.jeeva.repository.AdminRepository;
import com.jeeva.repository.StaffRepository;
import com.jeeva.repository.StudentRepository;
import com.jeeva.repository.UserRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class UserService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    AdminRepository adminRepository;

    @Autowired
    StudentRepository studentRepository;

    @Autowired
    StaffRepository staffRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    public boolean createUser(UserRequest request) throws IOException {
        Optional<User> isUserExists = userRepository.findByEmail(request.getEmail());
        if (!isUserExists.isPresent()) {
            var user = User.builder()
                    .firstName(request.getFirstName())
                    .lastName(request.getLastName())
                    .email(request.getEmail())
                    .mobile(request.getMobile())
                    .password(passwordEncoder.encode(request.getPassword()))
                    .image(request.getImage())
                    .isEnabled(true)
                    .role(Role.valueOf(request.getRole().toUpperCase()))
                    .build();
            forwardUser(userRepository.save(user));
            return true;
        } else {
            return false;
        }
    }

    public List<UserPartialResponse> getAllUsers() {
        List<User> users = userRepository.findAll();
        return users.stream()
                .map(this::convertToUserPartialResponse)
                .collect(Collectors.toList());
    }

    public UserPartialResponse getUserById(String uid) {
        User user = userRepository.findById(uid).orElse(null);
        return user != null ? convertToUserPartialResponse(user) : null;
    }

    public void deleteUser(String uid) {
        userRepository.deleteById(uid);
    }

    public void updateUser(String uid, UserRequest request) throws IOException {
        User user = userRepository.findById(uid).orElse(null);
        if (user != null) {
            user.setFirstName(request.getFirstName());
            user.setLastName(request.getLastName());
            user.setEmail(request.getEmail());
            user.setMobile(request.getMobile());
            user.setPassword(passwordEncoder.encode(request.getPassword()));
            user.setImage(request.getImage());
            user.setRole(Role.valueOf(request.getRole().toUpperCase()));
            userRepository.save(user);
        }
    }

    private UserPartialResponse convertToUserPartialResponse(User user) {
        return UserPartialResponse.builder()
                .uid(user.getUid())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .mobile(user.getMobile())
                .password(null) // Exclude password from response
                .image(user.getImage())
                .role(user.getRole().name())
                .build();
    }

    private void forwardUser(User user) {
        switch (user.getRole()) {
            case ADMIN:
                Admin admin = Admin.builder()
                        .aid(user.getUid())
                        .name(user.getFirstName() + " " + user.getLastName())
                        .user(user)
                        .build();
                adminRepository.save(admin);
                break;
            case STUDENT:
                Student student = Student.builder()
                        .sid(user.getUid())
                        .name(user.getFirstName() + " " + user.getLastName())
                        .user(user)
                        .build();
                studentRepository.save(student);
                break;
            case STAFF:
                Staff staff = Staff.builder()
                        .sid(user.getUid())
                        .name(user.getFirstName() + " " + user.getLastName())
                        .user(user)
                        .build();
                staffRepository.save(staff);
                break;
            default:
                break;
        }
    }
}
