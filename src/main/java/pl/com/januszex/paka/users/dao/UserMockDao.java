package pl.com.januszex.paka.users.dao;

import org.springframework.context.annotation.Profile;
import pl.com.januszex.paka.users.dto.UserDto;

import java.util.Optional;

@Profile("!prod")
class UserMockDao implements UserDao {
    @Override
    public Optional<UserDto> getUserByEmail(String email) {
        return Optional.empty();
    }
}
