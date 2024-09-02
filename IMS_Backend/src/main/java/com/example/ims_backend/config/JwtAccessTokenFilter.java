package com.example.ims_backend.config;


import com.example.ims_backend.common.TokenType;
import jakarta.annotation.Nonnull;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;

@RequiredArgsConstructor
@Slf4j
public class JwtAccessTokenFilter extends OncePerRequestFilter {

    private final RSAKeyRecord rsaKeyRecord;
    private final JwtTokenUtils jwtTokenUtils;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    @Nonnull HttpServletResponse response,
                                    @Nonnull FilterChain filterChain) throws ServletException, IOException {

        try{
            log.info("[JwtAccessTokenFilter:doFilterInternal] :: Started ");
            log.info("[JwtAccessTokenFilter:doFilterInternal]Filtering the Http Request:{}",request.getRequestURI());

            final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);

            JwtDecoder jwtDecoder =  NimbusJwtDecoder.withPublicKey(rsaKeyRecord.rsaPublicKey()).build();

            if(!authHeader.startsWith(TokenType.Bearer.name())){
                filterChain.doFilter(request,response);
                return;
            }

            final String token = authHeader.substring(7);   // Bearer <token>
            final Jwt jwtToken = jwtDecoder.decode(token); // This is where catch block will be executed if access token is expired

            final String username = jwtTokenUtils.getUserName(jwtToken);

            if(!username.isEmpty() && SecurityContextHolder.getContext().getAuthentication() == null) {

                UserDetails userDetails = jwtTokenUtils.userDetails(username);
                if(jwtTokenUtils.isTokenValid(jwtToken,userDetails)){
                    SecurityContext securityContext = SecurityContextHolder.createEmptyContext();

                    UsernamePasswordAuthenticationToken createdToken = new UsernamePasswordAuthenticationToken(
                            userDetails,
                            null,
                            userDetails.getAuthorities()
                    );
                    createdToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    securityContext.setAuthentication(createdToken);
                    SecurityContextHolder.setContext(securityContext);
                }
            }
            log.info("[JwtAccessTokenFilter:doFilterInternal] Completed");
            filterChain.doFilter(request, response);
        }catch (JwtValidationException jwtValidationException) {
            log.error("[JwtAccessTokenFilter:doFilterInternal] Token validation exception: {}", jwtValidationException.getMessage());
            response.setStatus(HttpStatus.NOT_ACCEPTABLE.value());
            response.getWriter().write(jwtValidationException.getMessage());
            response.getWriter().flush();
        } catch (JwtException jwtException) {
            log.error("[JwtAccessTokenFilter:doFilterInternal] JWT exception: {}", jwtException.getMessage());
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            response.getWriter().write(jwtException.getMessage());
            response.getWriter().flush();
        }
    }
}
