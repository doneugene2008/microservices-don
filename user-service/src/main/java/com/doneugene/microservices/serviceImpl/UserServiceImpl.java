package com.doneugene.microservices.serviceImpl;


import com.doneugene.microservices.dto.DepartmentDTO;
import com.doneugene.microservices.dto.UserResponseDTO;
import com.doneugene.microservices.model.User;
import com.doneugene.microservices.repository.UserRepository;
import com.doneugene.microservices.services.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RestTemplate restTemplate;


    @Override
    public User saveUser(User user) {
        try {
            log.info("Inside saveUser method.");
            return userRepository.save(user);
        } catch (Exception e) {
            log.info("Error saving user, cause -> {}, message -> {}", e.getCause(), e.getMessage());
            return null;
        }
    }

    @Override
    public User fetchUserById(UUID userId) {
        try {
            log.info("Inside fetch User by ID method.");
            Optional<User> user = userRepository.findById(userId);
            return user.orElse(null);
        } catch (Exception e) {
            log.info("Error fetching User by ID, cause -> {}, message -> {}", e.getCause(), e.getMessage());
            return null;
        }
    }

    @Override
    public List<User> fetchAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public UserResponseDTO fetchUserWithDepartment(UUID userId) {
        var userResponseDTO = new UserResponseDTO();
            /*  Fetch User FROM User Service  */
            User user = this.fetchUserById(userId);

            /*  Fetch Department From Department Service    */
            DepartmentDTO department = restTemplate
                    .getForObject("http://DEPARTMENT-SERVICE/api/departments/" + user.getDepartmentId(),
                            DepartmentDTO.class);

            userResponseDTO.setUser(user);
            userResponseDTO.setDepartment(department);

            return userResponseDTO;

    }
}