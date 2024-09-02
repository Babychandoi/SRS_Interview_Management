package com.example.ims_backend.config;

import com.example.ims_backend.entity.SecurityUser;
import com.example.ims_backend.entity.User;
import com.example.ims_backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.Objects;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class JwtTokenUtils {

    private final UserRepository repository;

    public String getUserName(Jwt jwtToken){
        return jwtToken.getSubject();
    }

    public boolean isTokenValid(Jwt jwtToken, UserDetails userDetails){
        final String userName = getUserName(jwtToken);
        boolean isTokenExpired = getIfTokenIsExpired(jwtToken);
        boolean isTokenUserSameAsDatabase = userName.equals(userDetails.getUsername());
        return !isTokenExpired  && isTokenUserSameAsDatabase;
    }

    private boolean getIfTokenIsExpired(Jwt jwtToken) {
        return Objects.requireNonNull(jwtToken.getExpiresAt()).isBefore(Instant.now());
    }

    public UserDetails userDetails(String email) {
        Optional<User> user = repository.findByEmail(email);
        if(user.isEmpty()){
            throw new UsernameNotFoundException("User not found with email: "+email);
        }
        return new SecurityUser(user.get());
    }
}
