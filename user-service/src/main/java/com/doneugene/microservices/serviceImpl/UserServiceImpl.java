package com.doneugene.microservices.serviceImpl;


import com.doneugene.microservices.dto.DepartmentDTO;
import com.doneugene.microservices.dto.ServiceAvailableDTO;
import com.doneugene.microservices.dto.UserResponseDTO;
import com.doneugene.microservices.model.User;
import com.doneugene.microservices.repository.UserRepository;
import com.doneugene.microservices.services.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RestTemplate restTemplate;
    private static final String ghanaGovURL = "https://www.govgh.org/api/v1.1/checkout/";
    private static final String ghanaGovURLPrivateKey = "f5777e6d979478f5ab42c5536aaad1804ef7740f4bb6a72657ae41c027cfb6e2809b20cb5874581b34f44ee8e40bbcd59f2a901fab84ba2306cf52f0998196aec6b3954720dce4d5fafa96e0ca92dc8af6b5b9281eb9b6a49b7fc6d8ed98fd8da6d9180b7c7368db45b4c767fdedf820";
    private static final Integer ghanaGovURLCollection = 210100;


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
        /*try {*/
            /*  Fetch User FROM User Service  */
            User user = this.fetchUserById(userId);

            /*  Fetch Department From Department Service    */
            DepartmentDTO department = restTemplate
                    .getForObject("http://DEPARTMENT-SERVICE/api/departments/" + user.getDepartmentId(),
                            DepartmentDTO.class);

            userResponseDTO.setUser(user);
            userResponseDTO.setDepartment(department);


            /*  Make Call To Doc Service To get Document    */
            /*UriComponentsBuilder builder = UriComponentsBuilder
                    .fromUriString(ghanaGovURL.concat("service.php"))
                    .queryParam("request", "get")
                    .queryParam("api_key", ghanaGovURLPrivateKey)
                    .queryParam("collection_agent_branch_code", ghanaGovURLCollection)
                    .queryParam("search_field", "all")
                    .queryParam("current_page", 0)
                    .queryParam("results_per_page", 1000)
                    .queryParam("sort_by", "name")
                    .queryParam("sort_ascending", true);

            System.out.println("builder.buildAndExpand().toString() = " + builder.build().toUriString());

            ServiceAvailableDTO ghGovApiResponse = restTemplate
                    .getForObject(builder.build().toUriString(), ServiceAvailableDTO.class);

            System.out.println("ghGovApiResponse = " + ghGovApiResponse);*/

            return userResponseDTO;

        /*}catch (Exception e){

            log.error("Error while fetching user with department, Cause -> {}, Message -> {}", e.getCause(), e.getMessage());
        }
        return null;*/
    }
}