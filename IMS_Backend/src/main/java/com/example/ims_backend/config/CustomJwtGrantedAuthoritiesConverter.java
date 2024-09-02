package com.example.ims_backend.config;

import org.springframework.core.convert.converter.Converter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class CustomJwtGrantedAuthoritiesConverter implements Converter<Jwt, Collection<GrantedAuthority>>{
    @Override
    public Collection<GrantedAuthority> convert(Jwt jwt) {
        List<Map<String, String>> scopes = (List<Map<String, String>>) jwt.getClaims().get("scope");

        return scopes.stream()
                .map(scope -> new SimpleGrantedAuthority(scope.get("role")))
                .collect(Collectors.toList());
    }
}
