package com.doneugene.microservices.rest;


import com.doneugene.microservices.dto.UserResponseDTO;
import com.doneugene.microservices.model.User;
import com.doneugene.microservices.services.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
@Slf4j
public class UserRestController {

    private static final String USER_WITH_DEPARTMENT = "userWithDepartmentService";
    private final UserService userService;


    @PostMapping(value = "/save_user", consumes = {MediaType.APPLICATION_JSON_VALUE}, produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<?> saveUser(@RequestBody User user) throws JsonProcessingException {
        log.info("Post Request Payload: {}", new ObjectMapper().writer().withDefaultPrettyPrinter().writeValueAsString(user));

        var _user = userService.saveUser(user);

        if (_user != null) return new ResponseEntity<>(_user, HttpStatus.CREATED);
        else return new ResponseEntity<>("Error Saving User", HttpStatus.NOT_MODIFIED);
    }


    @GetMapping(value = "/{userId}", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<?> fetchUserById(@PathVariable UUID userId){
        log.info("Fetch User by ID: {}", userId);

        User _user = userService.fetchUserById(userId);

        if (_user != null)return new ResponseEntity<>(_user, HttpStatus.OK);
        else return new ResponseEntity<>("Error Fetching User by ID " + userId, HttpStatus.NOT_FOUND);
    }


    @GetMapping(produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<?> fetchAllUsers(){
        log.info("Fetch All Users");

        List<User> users = userService.fetchAllUsers();

        if (!users.isEmpty())return new ResponseEntity<>(users, HttpStatus.OK);
        else return new ResponseEntity<>("No Users", HttpStatus.NO_CONTENT);
    }

    @GetMapping(value = "/with-department/{userId}", produces = {MediaType.APPLICATION_JSON_VALUE})
    @CircuitBreaker(name = USER_WITH_DEPARTMENT, fallbackMethod = "userWithDepartmentFallback")
    public ResponseEntity<?> fetchUserWithDepartment(@PathVariable UUID userId){

        log.info("Inside fetchUserWithDepartment of userRestController");
        UserResponseDTO userResponseDTO = userService.fetchUserWithDepartment(userId);
        if (userResponseDTO != null)
            return ResponseEntity.ok(userResponseDTO);
        return new ResponseEntity<>("Error", HttpStatus.NO_CONTENT);
    }

    public ResponseEntity<?> userWithDepartmentFallback(Exception e){
        return new ResponseEntity<>("Department Service is taking Longer to respond, Please retry!", HttpStatus.OK);
    }



}
