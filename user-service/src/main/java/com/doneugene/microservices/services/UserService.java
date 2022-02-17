package com.doneugene.microservices.services;


import com.doneugene.microservices.dto.UserResponseDTO;
import com.doneugene.microservices.model.User;

import java.util.List;
import java.util.UUID;

public interface UserService {

    User saveUser(User user);

    User fetchUserById(UUID userId);

    List<User> fetchAllUsers();

    UserResponseDTO fetchUserWithDepartment(UUID userId);
}