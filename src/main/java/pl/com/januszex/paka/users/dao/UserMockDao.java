package pl.com.januszex.paka.users.dao;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import pl.com.januszex.paka.users.dto.UserDto;

import java.util.Optional;

@Profile("!prod")
@Service
class UserMockDao implements UserDao {
    @Override
    public Optional<UserDto> getUserByEmail(String email) {
        return Optional.empty();
    }
}
