package com.ezeeinfo.security;

import com.ezeeinfo.service.CustomUserDetailsService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

/**
 * This is the JWT authentication filter.
 *
 */
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    /**
     * Constant for substring index.
     */
    private static final int SUBSTRING_INDEX = 7;
    /**
     * JWT token generator.
     */
    @Autowired
    private JwtGenerator tokenGenerator;
    /**
     * Customer user details service.
     */
    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    /**
     * Gets the JWT token from the request. if the token is present it
     * validates the token with the help of JWT token generator.
     * Next extracts the username and roles from the token. Extracts
     * the role details. User details are fetched from
     * CustomerUserDetailsService.
     * Lastly creates the UserPasswordAuthenticationToken with the
     * required details from JWT token and sets the same into
     * SecurityContextHolder
     * @param request
     * @param response
     * @param filterChain
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void doFilterInternal(final HttpServletRequest request,
           final HttpServletResponse response,
           final FilterChain filterChain)
            throws ServletException, IOException {
        String token = getJWTFromRequest(request);
        if (StringUtils.hasText(token) && tokenGenerator.validateToken(token)) {
            String username = tokenGenerator.getUsernameFromJWT(token);

            List<String> roles = tokenGenerator.getRolesFromJWT(token);
            List<GrantedAuthority> authorities = roles.stream()
                    .map(role -> new SimpleGrantedAuthority(
                            role.replace("ROLE_", "")))
                    .collect(Collectors.toList());

            UserDetails userDetails = customUserDetailsService
                    .loadUserByUsername(username);
            UsernamePasswordAuthenticationToken authenticationToken =
                    new UsernamePasswordAuthenticationToken(userDetails,
                            tokenGenerator.getAudianceFromJWT(token),
                            authorities);
            authenticationToken.setDetails(new WebAuthenticationDetailsSource()
                    .buildDetails(request));
            SecurityContextHolder.getContext().
                    setAuthentication(authenticationToken);

        }
        filterChain.doFilter(request, response);
    }

    /**
     *
     * @param request
     * @return JWTToken string from the request
     */

    private String getJWTFromRequest(final HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (StringUtils.hasText(bearerToken)
                && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(SUBSTRING_INDEX,
                    bearerToken.length());
        }
        return null;
    }
}
