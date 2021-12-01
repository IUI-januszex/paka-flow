package pl.com.januszex.paka.users.infrastructure;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import pl.com.januszex.paka.security.CurrentUser;
import pl.com.januszex.paka.users.api.service.CurrentUserServicePort;

import java.util.Collection;
import java.util.Objects;

@Service
class CurrentUserServiceAdapter implements CurrentUserServicePort {

    public boolean isAnonymous() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication == null || authentication.getPrincipal().equals("anonymousUser");
    }

    public String getCurrentJwt() {
        return Objects.requireNonNull(getAuthentication()).getCredentials();
    }

    @Override
    public boolean isClient() {
        String role = getRole();
        return role != null && (role.equals("ROLE_Courier") || role.equals("ROLE_Logistician"));
    }

    @Override
    public boolean isWorker() {
        String role = getRole();
        return role != null && (role.equals("ROLE_ClientInd") || role.equals("ROLE_ClientBiz"));
    }

    @Override
    public boolean isCourier() {
        String role = getRole();
        return role != null && role.equals("ROLE_Courier");
    }

    private CurrentUser getAuthentication() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication instanceof CurrentUser) {
            return (CurrentUser) authentication;
        }
        return null;
    }

    private String getRole() {
        CurrentUser currentUser = getAuthentication();
        if (currentUser == null) {
            return null;
        }
        Collection<? extends GrantedAuthority> grantedAuthorities = currentUser.getAuthorities();
        if (grantedAuthorities == null || grantedAuthorities.isEmpty()) {
            return null;
        }
        return grantedAuthorities.iterator().next().getAuthority();
    }
}
