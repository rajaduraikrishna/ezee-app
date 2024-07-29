package com.ezeeinfo.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;


/**
 * SecurityConfig class.
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {
    /**
     * Admin initialization.
     */
    public static final String ADMIN = "admin";
    /**
     * user intialization.
     */
    public static final String USER = "user";
    /**
     * JwtAuthEntryPoint declaration.
     */
    private final JwtAuthEntryPoint authEntryPoint;

    /**
     * SecurityConfig method.
     * @param point
     */

    public SecurityConfig(final JwtAuthEntryPoint point) {
        this.authEntryPoint = point;
    }

    /**
     * SecurityFilterChain method.
     * @param http
     * @return build
     * @throws Exception
     */
    @Bean
    public SecurityFilterChain filterChain(final HttpSecurity http)
            throws Exception {
        http.authorizeHttpRequests((authz) ->
                authz.requestMatchers("/api/**")
                .permitAll()
                .requestMatchers(HttpMethod.GET)
                .permitAll()
                .requestMatchers(HttpMethod.DELETE)
                .hasAuthority(ADMIN)
                .requestMatchers(HttpMethod.POST)
                .permitAll()
                .requestMatchers(HttpMethod.PUT)
                .hasAuthority(USER)
                .anyRequest()
                .authenticated());

 http.exceptionHandling((exceptions) ->
         exceptions.authenticationEntryPoint(authEntryPoint));
http.sessionManagement(sess ->
        sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
  http.csrf(c -> c.disable());
        return http.build();
    }

    /**
     * PasswordEncoder method.
     * @return password
     */
    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * AuthenticationManager method.
     * @param authenticationConfiguration
     * @return manager
     * @throws Exception
     */
    @Bean
    public AuthenticationManager authenticationManager(final
      AuthenticationConfiguration authenticationConfiguration)
            throws Exception {
 return authenticationConfiguration.getAuthenticationManager();
    }

    /**
     * JwtAuthenticationFilter method.
     * @return filter
     */
    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter() {
        return new JwtAuthenticationFilter();
    }
}
