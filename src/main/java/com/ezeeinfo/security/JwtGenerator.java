package com.ezeeinfo.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * JWT token generator.
 */
@Component
public class JwtGenerator {

    /**
     * token expiration time in milliseconds.
     */
    public static final long JWT_EXPIRATION = 70000;
    /**
     * secret key using HSS512.
     */
    private Key secretKey = Keys.secretKeyFor(SignatureAlgorithm.HS512);

    /**
     * Generates the JWT token with all the required details.
     * @param authentication
     * @param tenantId
     * @return generated token
     */
    public String generateToken(final Authentication authentication,
                                final String tenantId) {
        String username = authentication.getName();
        Date currentDate = new Date();
        Date expireDate = new Date(currentDate.getTime() + JWT_EXPIRATION);

        // Extract roles from the authentication object
        Collection<? extends GrantedAuthority> authorities
                = authentication.getAuthorities();
        List<String> roles = authorities.stream().map(
                GrantedAuthority::getAuthority)
                .collect(Collectors.toList());

        // Build the token with roles included in claims
        String token = Jwts
                .builder()
                .setAudience(tenantId)
                .setSubject(username)
                .claim("roles", roles) // Inclusion of roles in claims
                .setIssuedAt(new Date())
                .setExpiration(expireDate)
                .signWith(SignatureAlgorithm.HS512, secretKey).compact();
        return token;
    }

    /**
     * Gets the user name from token.
     * @param token
     * @return user name
     */

    public String getUsernameFromJWT(final String token) {
        Claims claims = Jwts.parserBuilder().setSigningKey(secretKey)
                .build().parseClaimsJws(token).getBody();
        return claims.getSubject();
    }

    /**
     * validates if the token is valid or expired.
     * @param token
     * @return true if valid else false
     */

    public boolean validateToken(final String token) {
        try {
            Jwts.parserBuilder().setSigningKey(secretKey)
                    .build().parseClaimsJws(token);
            return true;
        } catch (Exception ex) {
            throw new AuthenticationCredentialsNotFoundException(
                    "JWT was exprired or incorrect",
                    ex.fillInStackTrace());
        }
    }

    /**
     * Gets the roles details from JWT token.
     * @param token
     * @return list of roles from the token
     */

    public List<String> getRolesFromJWT(final String token) {
        Claims claims = Jwts.parserBuilder().setSigningKey(secretKey)
                .build().parseClaimsJws(token).getBody();
        return claims.get("roles", List.class);
    }

    /**
     * Gets the Audiance details from token.
     * @param token
     * @return Audiance object
     */
    public Object getAudianceFromJWT(final String token) {
        Claims claims = Jwts.parserBuilder().setSigningKey(secretKey)
                .build().parseClaimsJws(token).getBody();
        return claims.getAudience();
    }
}
