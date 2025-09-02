package com.trashheroesbe.feature.user.domain.type;

import java.util.Collection;
import java.util.Set;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

@Getter
@RequiredArgsConstructor
public enum Role {
    USER(Set.of("ROLE_USER")),
    ADMIN(Set.of("ROLE_ADMIN", "ROLE_USER"));

    private final Set<String> authorities;

    public Collection<? extends GrantedAuthority> toAuthorities() {
        return authorities.stream()
            .map(SimpleGrantedAuthority::new)
            .toList();
    }
}
