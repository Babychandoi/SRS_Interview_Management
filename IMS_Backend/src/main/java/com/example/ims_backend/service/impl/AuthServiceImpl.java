package com.example.ims_backend.service.impl;

import java.util.Arrays;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.server.ResponseStatusException;

import com.example.ims_backend.common.TokenType;
import com.example.ims_backend.config.UserMapper;
import com.example.ims_backend.dto.AuthResponseDTO;
import com.example.ims_backend.dto.UserRegistrationDTO;
import com.example.ims_backend.entity.RefreshToken;
import com.example.ims_backend.entity.User;
import com.example.ims_backend.repository.RefreshTokenRepository;
import com.example.ims_backend.repository.UserRepository;
import com.example.ims_backend.service.AuthService;
import com.example.ims_backend.service.JwtTokenGenerator;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final JwtTokenGenerator jwtTokenGenerator;
    private final RefreshTokenRepository refreshTokenRepository;
    private final UserMapper userInfoMapper;

    public AuthResponseDTO getJwtTokensAfterAuthentication(
            @RequestHeader("Authorization") Authentication authentication,
            HttpServletResponse response) {
        try {
            var userInfoEntity = userRepository.findByEmail(authentication.getName())
                    .orElseThrow(() -> {
                        log.error("[AuthService:userSignInAuth] User :{} not found", authentication.getName());
                        return new ResponseStatusException(HttpStatus.NOT_FOUND, "USER NOT FOUND ");
                    });

            String accessToken = jwtTokenGenerator.generateAccessToken(authentication);
            String refreshToken = jwtTokenGenerator.generateRefreshToken(authentication);

            saveUserRefreshToken(userInfoEntity, refreshToken);
            creatRefreshTokenCookie(response, refreshToken);

            log.info("[AuthService:userSignInAuth] Access token for user:{}, has been generated",
                    userInfoEntity.getEmail());

            return AuthResponseDTO.builder()
                    .accessToken(accessToken)
                    .accessTokenExpiry(60 * 60 * 24) // 24 hours
                    .fullName(userInfoEntity.getFullName())
                    .email(userInfoEntity.getEmail())
                    .role(userInfoEntity.getRole())
                    .tokenType(TokenType.Bearer)
                    .build();

        } catch (Exception e) {
            e.printStackTrace();
            log.error("[AuthService:userSignInAuth]Exception while authenticating the user due to :{}", e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Please Try Again");

        }
    }

    private Cookie creatRefreshTokenCookie(HttpServletResponse response, String refreshToken) {
        Cookie refreshTokenCookie = new Cookie("refresh_token", refreshToken);
        refreshTokenCookie.setHttpOnly(true);
        refreshTokenCookie.setSecure(false);
        refreshTokenCookie.setPath("/");
        refreshTokenCookie.setMaxAge(15 * 24 * 60 * 60); // 15 days
        response.addCookie(refreshTokenCookie);
        return refreshTokenCookie;
    }

    private void saveUserRefreshToken(User userInfoEntity, String refreshToken) {
        var refreshTokenEntity = RefreshToken.builder()
                .user(userInfoEntity)
                .refreshToken(refreshToken)
                .revoked(false)
                .build();
        refreshTokenRepository.save(refreshTokenEntity);
    }

    public Object getAccessTokenUsingRefreshToken(String authorizationHeader) {

        if (!authorizationHeader.startsWith(TokenType.Bearer.name())) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Please verify your token type");
        }

        final String refreshToken = authorizationHeader.substring(7);

        // Find refreshToken from database and should not be revoked : Same thing can be
        // done through filter.
        var refreshTokenEntity = refreshTokenRepository.findByRefreshToken(refreshToken)
                .filter(tokens -> !tokens.isRevoked())
                .orElseThrow(
                        () -> new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Refresh token revoked"));

        User userInfoEntity = refreshTokenEntity.getUser();

        // Now create the Authentication object
        Authentication authentication = createAuthenticationObject(userInfoEntity);

        // Use the authentication object to generate new accessToken as the
        // Authentication object
        // that we will have may not contain correct role.
        String accessToken = jwtTokenGenerator.generateAccessToken(authentication);

        return AuthResponseDTO.builder()
                .accessToken(accessToken)
                .accessTokenExpiry(5 * 60)
                .email(userInfoEntity.getEmail())
                .fullName(userInfoEntity.getFullName())
                .tokenType(TokenType.Bearer)
                .build();
    }

    private static Authentication createAuthenticationObject(User userInfoEntity) {
        // Extract user details from UserDetailsEntity
        String username = userInfoEntity.getEmail();
        String password = userInfoEntity.getPassword();
        String roles = String.valueOf(userInfoEntity.getRole());

        // Extract authorities from roles (comma-separated)
        String[] roleArray = roles.split(",");
        GrantedAuthority[] authorities = Arrays.stream(roleArray)
                .map(role -> (GrantedAuthority) role::trim)
                .toArray(GrantedAuthority[]::new);

        return new UsernamePasswordAuthenticationToken(username, password, Arrays.asList(authorities));
    }

    public AuthResponseDTO registerUser(UserRegistrationDTO userRegistrationDto,
            HttpServletResponse httpServletResponse) {

        try {
            log.info("[AuthService:registerUser]User Registration Started with :::{}", userRegistrationDto);

            Optional<User> user = userRepository.findByEmail(userRegistrationDto.email());
            if (user.isPresent()) {
                throw new Exception("User Already Exist");
            }

            User userAccount = userInfoMapper.convertToEntity(userRegistrationDto);
            Authentication authentication = createAuthenticationObject(userAccount);

            // Generate a JWT token
            String accessToken = jwtTokenGenerator.generateAccessToken(authentication);
            String refreshToken = jwtTokenGenerator.generateRefreshToken(authentication);

            User savedUserDetails = userRepository.save(userAccount);
            saveUserRefreshToken(userAccount, refreshToken);

            creatRefreshTokenCookie(httpServletResponse, refreshToken);

            log.info("[AuthService:registerUser] User:{} Successfully registered", savedUserDetails.getEmail());
            return AuthResponseDTO.builder()
                    .accessToken(accessToken)
                    .accessTokenExpiry(5 * 60)
                    .email(savedUserDetails.getEmail())
                    .tokenType(TokenType.Bearer)
                    .build();

        } catch (Exception e) {
            log.error("[AuthService:registerUser]Exception while registering the user due to :" + e.getMessage());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }
}
