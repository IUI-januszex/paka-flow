package pl.com.januszex.paka.users.dao;

import pl.com.januszex.paka.users.dto.UserDto;

import java.util.Optional;


public interface UserDao {

    Optional<UserDto> getUserByEmail(String email);

}
