package com.example.myitemsrest.controller;

import com.example.myitemsrest.dto.*;
import com.example.myitemsrest.dto.Currency;
import com.example.myitemsrest.entity.Role;
import com.example.myitemsrest.entity.User;
import com.example.myitemsrest.repository.UserRepository;
import com.example.myitemsrest.sequrity.CurrentUser;
import com.example.myitemsrest.util.JwtTokenUtil;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.*;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenUtil jwtTokenUtil;
    private final RestTemplate restTemplate;

    @Value("${myitems.cb.url}")
    private String cbUrl;

    @PostMapping("/users/auth")
    public ResponseEntity<UserLoginResponse> userLogin(@RequestBody UserLoginRequest loginRequest) {
        if (loginRequest.getEmail() != null && !loginRequest.getEmail().equals("")) {
            Optional<User> byEmail = userRepository.findByEmail(loginRequest.getEmail());
            if (!byEmail.isPresent() || !passwordEncoder.matches(loginRequest.getPassword(), byEmail.get().getPassword())) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }
            return ResponseEntity.ok(new UserLoginResponse(jwtTokenUtil.generateToken(byEmail.get().getEmail())));
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }


    @GetMapping("/users/{id}")

    public UserResponseDto getUserById(@PathVariable("id") int id) {
        ResponseEntity<University[]> forEntity = restTemplate.getForEntity("http://universities.hipolabs.com/search?country=Armenia", University[].class);
        University[] universities = forEntity.getBody();
//        List<University> universityList = Arrays.asList(universities);
        for (University university : universities) {
            System.out.println(university);
        }
        return modelMapper.map(userRepository.getById(id), UserResponseDto.class);

    }

    @GetMapping("/users/")
    @CrossOrigin(origins = "http://localhost:8080")
    public List<UserResponseDto> getUsers(@AuthenticationPrincipal CurrentUser currentUser) {
        System.out.println(currentUser.getUser().getName());
        List<UserResponseDto> result = new ArrayList<>();
        for (User user : userRepository.findAll()) {
            result.add(modelMapper.map(user, UserResponseDto.class));
        }
        return result;
    }

    @PostMapping("/users/")
    public UserResponseDto saveUser(@RequestBody SaveUserRequest saveUserRequest) {
        User user = modelMapper.map(saveUserRequest, User.class);
        user.setRole(Role.USER);
//        user.setActive(false);
//        user.setToken(UUID.randomUUID().toString());
//        user.setTokenCreatedDate(LocalDateTime.now());
        user.setPassword(passwordEncoder.encode(saveUserRequest.getPassword()));
        String rubCurrencyUrl = cbUrl + "?currency=RUB";
        ResponseEntity<HashMap> rubCurrency = restTemplate.getForEntity(rubCurrencyUrl, HashMap.class);
        HashMap<String, String> currencyMap = rubCurrency.getBody();
        if (currencyMap != null) {
            System.out.println(currencyMap.get("RUB"));
        }
        userRepository.save(user);
        return modelMapper.map(user, UserResponseDto.class);
    }

    @DeleteMapping("/users/{id}")

    public ResponseEntity deleteById(@PathVariable("id") int id) {
        if (userRepository.existsById(id)) {
            userRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/users/{id}")
    public ResponseEntity<UserResponseDto> updateUser(@RequestBody SaveUserRequest saveUserRequest,
                                                      @PathVariable("id") int id) {
        Optional<User> user = userRepository.findById(id);
        if (!user.isPresent()) {
            return ResponseEntity.notFound().build();
        }
        User byId = user.get();
        if (saveUserRequest.getEmail() != null) {
            byId.setEmail(saveUserRequest.getEmail());
        }
        if (saveUserRequest.getName() != null) {
            byId.setName(saveUserRequest.getName());
        }
        if (saveUserRequest.getSurname() != null) {
            byId.setSurname(saveUserRequest.getSurname());
        }
        if (saveUserRequest.getPassword() != null) {
            byId.setPassword(saveUserRequest.getPassword());
        }
        if (saveUserRequest.getPhone() != null) {
            byId.setPhone(saveUserRequest.getPhone());
        }
        userRepository.save(byId);
        UserResponseDto userResponseDto = modelMapper.map(byId, UserResponseDto.class);

        return ResponseEntity.ok(userResponseDto);
    }
}
