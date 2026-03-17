package com.GimPay.Integration_APIs.controllers;

import com.GimPay.Integration_APIs.dtos.UserDto;
import com.GimPay.Integration_APIs.services.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "*")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    /**
     * POST /api/users
     * Crée un utilisateur OU connecte si l'email existe déjà.
     * Accepte UserDto.Request (compatible avec CreateRequest via héritage).
     */
    @PostMapping
    public ResponseEntity<UserDto.Response> createOrLogin(@RequestBody UserDto.Request request) {
        UserDto.CreateRequest createRequest = new UserDto.CreateRequest();
        createRequest.setEmail(request.getEmail());
        createRequest.setFirstName(request.getFirstName());
        createRequest.setLastName(request.getLastName());
        createRequest.setPhone(request.getPhone());
        createRequest.setAddress(request.getAddress());
        return ResponseEntity.ok(userService.createOrLogin(createRequest));
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDto.Response> getById(@PathVariable Long id) {
        return ResponseEntity.ok(userService.getById(id));
    }
}