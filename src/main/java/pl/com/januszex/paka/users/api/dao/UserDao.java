package pl.com.januszex.paka.users.api.dao;

import pl.com.januszex.paka.users.domain.UserDto;

import java.util.Optional;


public interface UserDao {

    Optional<UserDto> getUserByEmail(String email);

}
