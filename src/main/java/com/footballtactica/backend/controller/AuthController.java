package com.footballtactica.backend.controller;

import com.footballtactica.backend.constants.ApiConstants;
import com.footballtactica.backend.dto.AuthRequest;
import com.footballtactica.backend.dto.AuthResponse;
import com.footballtactica.backend.service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.footballtactica.backend.constants.ApiConstants;

@RestController
@RequestMapping(ApiConstants.AUTH_BASE)
@CrossOrigin(origins = "*")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@RequestBody AuthRequest request) {
        return ResponseEntity.ok(authService.register(request));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody AuthRequest request) {
        return ResponseEntity.ok(authService.login(request));
    }
}