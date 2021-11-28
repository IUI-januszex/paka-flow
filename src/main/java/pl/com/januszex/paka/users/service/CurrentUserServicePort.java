package pl.com.januszex.paka.users.service;

public interface CurrentUserServicePort {
    boolean isAnonymous();

    String getCurrentJwt();

    boolean isClient();

    boolean isWorker();

    boolean isCourier();
}
