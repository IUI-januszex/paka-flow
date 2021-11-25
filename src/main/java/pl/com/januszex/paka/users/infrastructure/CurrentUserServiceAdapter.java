package pl.com.januszex.paka.users.infrastructure;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import pl.com.januszex.paka.security.CurrentUser;
import pl.com.januszex.paka.users.service.CurrentUserServicePort;

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

    private CurrentUser getAuthentication() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication instanceof CurrentUser) {
            return (CurrentUser) authentication;
        }
        return null;
    }
}
