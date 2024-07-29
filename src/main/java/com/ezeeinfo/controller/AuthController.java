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
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestBody;


import java.security.Principal;

/**
 * AuthController class useful for register and login.
 */
@RestController
@RequestMapping("/api/auth")
public class AuthController {


    /**
     * CustomUserDetailService class and customUserDetailsService variable.
     */
    private CustomUserDetailsService customUserDetailsService;
    /**
     *  PasswordEncoder class and passwordEncoder.
     */
    private PasswordEncoder passwordEncoder;
    /**
     * AuthenticationManager class and authenticationManager variable.
     */
    private AuthenticationManager authenticationManager;
    /**
     *  JwtGenerator class and jwtGenerator variable.
     */
    private JwtGenerator jwtGenerator;
    /**
     * The AuthController useful for login and register.
     * @param detailsService
     * @param password
     * @param authentication
     * @param generator
     */
    @Autowired
    public AuthController(final CustomUserDetailsService detailsService,
                          final PasswordEncoder password,
                          final AuthenticationManager authentication,
                          final JwtGenerator generator) {
        this.customUserDetailsService = detailsService;
        this.passwordEncoder = password;
        this.authenticationManager = authentication;
        this.jwtGenerator = generator;
        return;
    }

    /**
     * The Mapping.
     * @param principal
     * @return principal
     */
    @GetMapping("me")
    public final ResponseEntity<Principal> me(
            final Principal principal) {
        return ResponseEntity.ok().body(principal);
    }

    /**
     * It is a register.
     * @param tid
     * @param dto
     * @return user
     */
    @PostMapping("register")
    public ResponseEntity<String> register(@RequestHeader("X-PrivateTenant")
                                               final String tid,
                                           @RequestBody final RegisterDto dto) {

        AppUser appUser = new AppUser();
        appUser.setUsername(dto.getUsername());
        appUser.setPassword(passwordEncoder.encode(dto.getPassword()));
        customUserDetailsService.registerUser(appUser, dto.getRole());

        return new ResponseEntity<>("User registered success!", HttpStatus.OK);
    }

    /**
     * It is login.
     * @param tid
     * @param dto
     * @return login
     */
    @PostMapping("login")
    public ResponseEntity<AuthResponseDto>
                         login(@RequestHeader("X-PrivateTenant")
                             final String tid,
                             @RequestBody final LoginDto dto) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(dto.getUsername(),
                                dto.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String token = jwtGenerator.generateToken(authentication, tid);
        return new ResponseEntity<>(new AuthResponseDto(token), HttpStatus.OK);
    }
}
