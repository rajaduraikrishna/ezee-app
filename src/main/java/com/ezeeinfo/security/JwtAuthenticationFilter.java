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
 * JwtAuthenticationFilter class.
 * OncePerRequestFilter class
 */
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    /**
     * tokenGenerator declaration.
     */
    @Autowired
    private JwtGenerator tokenGenerator;
    /**
     * customUserDetailsService declaration.
     */
    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    /**
     * doFilterInternal method.
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
   List<GrantedAuthority> authorities =
     roles.stream().map(role -> new SimpleGrantedAuthority(role.replace("ROLE_",
        ""))).collect(Collectors.toList());

 UserDetails userDetails =
  customUserDetailsService.loadUserByUsername(username);
   UsernamePasswordAuthenticationToken authenticationToken =
    new UsernamePasswordAuthenticationToken(userDetails,
  tokenGenerator.getAudianceFromJWT(token), authorities);
  authenticationToken.setDetails(new
 WebAuthenticationDetailsSource().buildDetails(request));
   SecurityContextHolder.getContext().setAuthentication(authenticationToken);

        }
        filterChain.doFilter(request, response);
    }

    /**
     * getJWTFromRequest method.
     * @param request
     * @return beartoken
     */

    private String getJWTFromRequest(final HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (StringUtils.hasText(bearerToken)
          && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7, bearerToken.length());
        }
        return null;
    }
}
