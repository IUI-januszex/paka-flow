package pl.com.januszex.paka.users.service;

public interface CurrentUserServicePort {
    boolean isAnonymous();

    String getCurrentJwt();
}
