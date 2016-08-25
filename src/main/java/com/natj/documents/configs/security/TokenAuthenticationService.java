package com.natj.documents.configs.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.impl.TextCodec;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.Optional;

@Service
public class TokenAuthenticationService {
    @Value("${app.jwt.header}")
    private String authHeaderName;
    @Value("${app.jwt.secret}")
    private String secret;
    @Autowired
    private UserDetailsService userDetailsService;

    public String addAuthentication(HttpServletResponse response, UserAuthentication authentication) {
        UserDetails user = authentication.getDetails();
        String token = createTokenForUser(user);
        response.addHeader(authHeaderName, token);
        return token;
    }

    public Authentication getAuthentication(HttpServletRequest request) {
        String token = request.getHeader(authHeaderName);
        if (token == null || token.isEmpty())
            return null;
        return parseUserFromToken(token).map(UserAuthentication::new).orElse(null);
    }

    public Optional<UserDetails> parseUserFromToken(String token) {
        String username = Jwts.parser()
                .setSigningKey(TextCodec.BASE64.encode(secret))
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
        return Optional.ofNullable(userDetailsService.loadUserByUsername(username));
    }

    public String createTokenForUser(UserDetails user) {
        ZonedDateTime afterOneWeek = ZonedDateTime.now().plusWeeks(1);
        return Jwts.builder()
                .setSubject(user.getUsername())
                .claim("admin", user.getAuthorities().contains((GrantedAuthority) () -> "ROLE_ADMIN"))
                .signWith(SignatureAlgorithm.HS256, TextCodec.BASE64.encode(secret))
                .setExpiration(Date.from(afterOneWeek.toInstant()))
                .compact();
    }
}