package com.ezeeinfo.controller;


import com.ezeeinfo.dto.AuthResponseDto;
import com.ezeeinfo.dto.LoginDto;
import com.ezeeinfo.dto.RegisterDto;
import com.ezeeinfo.issuemanager.model.AppUser;
import com.ezeeinfo.security.JwtGenerator;
import com.ezeeinfo.service.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private CustomUserDetailsService customUserDetailsService;
    private PasswordEncoder passwordEncoder;
    private AuthenticationManager authenticationManager;
    private JwtGenerator jwtGenerator;
    @Autowired
    public AuthController(CustomUserDetailsService customUserDetailsService, PasswordEncoder passwordEncoder,
                          AuthenticationManager authenticationManager, JwtGenerator jwtGenerator) {
        this.customUserDetailsService = customUserDetailsService;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager=authenticationManager;
        this.jwtGenerator=jwtGenerator;
    }

    @PostMapping("register")
    public ResponseEntity<String> register(@RequestBody RegisterDto registerDto) {

        AppUser appUser = new AppUser();
        appUser.setUsername(registerDto.getUsername());
        appUser.setPassword(registerDto.getPassword());
        customUserDetailsService.registerUser(appUser, registerDto.getRole());

        return new ResponseEntity<>("User registered success!", HttpStatus.OK);
    }

    @PostMapping("login")
    public ResponseEntity<AuthResponseDto> login(@RequestBody LoginDto loginDto) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginDto.getUsername(), loginDto.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String token = jwtGenerator.generateToken(authentication);
        return new ResponseEntity<>(new AuthResponseDto(token), HttpStatus.OK);
    }
}
