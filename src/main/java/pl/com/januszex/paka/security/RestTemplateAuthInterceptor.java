package pl.com.januszex.paka.security;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import pl.com.januszex.paka.users.api.service.CurrentUserServicePort;

import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
public class RestTemplateAuthInterceptor implements ClientHttpRequestInterceptor {

    private final CurrentUserServicePort currentUserService;

    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {
        if (!currentUserService.isAnonymous()) {
            request.getHeaders().setBearerAuth(currentUserService.getCurrentJwt());
        }
        return execution.execute(request, body);
    }
}
