package back.api.persistence.models.enums;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

public enum Role {
    ADMIN("ROLE_ADMIN"),
    USER("ROLE_USER");

    private final SimpleGrantedAuthority authority;

    Role(String authority) {
        this.authority = new SimpleGrantedAuthority(authority);
    }

    public GrantedAuthority getAuthority() {
        return authority;
    }
}
