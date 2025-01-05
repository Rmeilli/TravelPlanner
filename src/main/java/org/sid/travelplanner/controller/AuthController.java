package org.sid.travelplanner.controller;

import lombok.RequiredArgsConstructor;
import org.sid.travelplanner.Security.JwtTokenProvider;
import org.sid.travelplanner.dto.LoginRequest;
import org.sid.travelplanner.dto.LoginResponse;
import org.sid.travelplanner.dto.SignupRequest;
import org.sid.travelplanner.model.User;
import org.sid.travelplanner.service.AuthenticationService;
import org.sid.travelplanner.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
//@RequiredArgsConstructor
public class AuthController {
    public AuthController(UserService userService, AuthenticationService authService, AuthenticationManager authenticationManager, JwtTokenProvider tokenProvider) {
        this.userService = userService;
        this.authService = authService;
        this.authenticationManager = authenticationManager;
        this.tokenProvider = tokenProvider;
    }

    private final UserService userService;
    private final AuthenticationService authService;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider tokenProvider;

//    public AuthController(UserService userService) {
//        this.userService = userService;
//    }


//    @PostMapping("/signup")
//    public ResponseEntity<?> signup(@RequestBody SignupRequest request) {
//        User user = new User();
//        user.setEmail(request.getEmail());
//        user.setPassword(request.getPassword());
//        user.setName(request.getName());
//
//        User createdUser = userService.createUser(user);
//        return ResponseEntity.ok(createdUser);
//    }
//
//
//    @PostMapping("/login")
//    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
//        Authentication authentication = authenticationManager.authenticate(
//                new UsernamePasswordAuthenticationToken(
//                        loginRequest.getEmail(),
//                        loginRequest.getPassword()
//                )
//        );
//
//        SecurityContextHolder.getContext().setAuthentication(authentication);
//        String jwt = tokenProvider.generateToken(loginRequest.getEmail());
//
//        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
//        User user = userService.findByEmail(userDetails.getUsername());
//
//        return ResponseEntity.ok(new LoginResponse(jwt, "Bearer", user.getId(), user.getEmail()));
//    }
@PostMapping("/signup")
public ResponseEntity<?> registerUser(@RequestBody SignupRequest signupRequest) {
    try {
        User user = new User();
        user.setEmail(signupRequest.getEmail());
        user.setPassword(signupRequest.getPassword());
        user.setName(signupRequest.getName());

        User registeredUser = authService.registerUser(user);
        String token = authService.authenticateUser(signupRequest.getEmail(), signupRequest.getPassword());

        Map<String, Object> response = new HashMap<>();
        response.put("token", token);
        response.put("userId", registeredUser.getId());
        response.put("email", registeredUser.getEmail());

        return ResponseEntity.ok(response);
    } catch (Exception e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }
}

    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@RequestBody LoginRequest loginRequest) {
        try {
            String token = authService.authenticateUser(loginRequest.getEmail(), loginRequest.getPassword());

            Map<String, String> response = new HashMap<>();
            response.put("token", token);

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Invalid email or password");
        }
    }}
