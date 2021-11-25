package pl.com.januszex.paka.users.infrastructure;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import pl.com.januszex.paka.flow.configuration.RestServiceUrls;
import pl.com.januszex.paka.users.dao.UserDao;
import pl.com.januszex.paka.users.dto.UserDto;

import java.net.URI;
import java.util.Optional;

@RequiredArgsConstructor
@Profile("prod")
@Service
@Slf4j
class UserRestDao implements UserDao {

    private final RestTemplate restTemplate;
    private final RestServiceUrls restServiceUrls;

    @Override
    public Optional<UserDto> getUserByEmail(String email) {
        try {
            URI uri = UriComponentsBuilder.fromUriString(restServiceUrls.getPakaUsersApiUrl())
                    .pathSegment("/{email}")
                    .build(email);
            return Optional.ofNullable(restTemplate.getForObject(uri, UserDto.class));

        } catch (Exception e) {
            log.debug("Exception when request user by email", e);
            return Optional.empty();
        }
    }
}
