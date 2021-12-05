package pl.com.januszex.paka.users.api.service;

public interface CurrentUserServicePort {
    boolean isAnonymous();

    String getCurrentJwt();

    boolean isClient();

    boolean isWorker();

    boolean isCourier();

    boolean hasId(String id);
}
