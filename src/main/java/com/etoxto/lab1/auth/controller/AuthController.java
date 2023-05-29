package com.etoxto.lab1.auth.controller;

import com.etoxto.lab1.auth.model.User;
import com.etoxto.lab1.auth.service.AuthService;
import com.etoxto.lab1.network.request.UserRequest;
import com.etoxto.lab1.network.respons.ResponseWrapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/users")
public class AuthController {
    private final AuthService authService;

    public AuthController(AuthService authService){
        this.authService = authService;
    }

    @PostMapping("")
    public ResponseEntity<?> register(@RequestBody UserRequest request){
        String username = request.getUsername();
        if(authService.loadUserByUsername(username)!=null){
            return ResponseEntity.status(HttpStatus.CONFLICT).body(new ResponseWrapper("User with such username has been already registered"));
        }
        authService.saveUser(new User(username, request.getPassword()));

        return ResponseEntity.ok().build();
    }
}
