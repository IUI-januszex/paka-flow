package pl.com.januszex.paka.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;
import java.util.Collections;

public class CurrentUser implements Authentication {

    private boolean authenticated;
    private final String token;
    private final String username;
    private final Collection<GrantedAuthority> grantedAuthorities;
    private final String id;
    private final Object details;

    public CurrentUser(String token, UserData userData, Object details) {
        this.token = token;
        this.authenticated = false;
        this.username = userData.getUsername();
        this.id = userData.getId();
        this.grantedAuthorities = Collections.singleton(userData.getAuthority());
        this.details = details;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return grantedAuthorities;
    }

    @Override
    public String getCredentials() {
        return token;
    }

    @Override
    public Object getDetails() {
        return details;
    }


    @Override
    public String getPrincipal() {
        return id;
    }

    @Override
    public boolean isAuthenticated() {
        return authenticated;
    }

    @Override
    public void setAuthenticated(boolean authenticated) throws IllegalArgumentException {
        this.authenticated = authenticated;
    }

    @Override
    public String getName() {
        return username;
    }
}
