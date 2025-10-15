package com.philately.service;


import com.philately.config.UserSession;
import com.philately.model.dto.UserLoginDto;
import com.philately.model.dto.UserRegisterDto;
import com.philately.model.entity.User;
import com.philately.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserSession userSession;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, UserSession userSession) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.userSession = userSession;
    }


    public boolean register(UserRegisterDto userRegisterDto) {
        Optional<User> existingUser = userRepository
                .findByUsernameOrEmail(userRegisterDto.getUsername(), userRegisterDto.getEmail());

        if (existingUser.isPresent()) {
            return false;
        }

        User user = new User();
        user.setUsername(userRegisterDto.getUsername());
        user.setEmail(userRegisterDto.getEmail());
        user.setPassword(userRegisterDto.getPassword());
        user.setPassword(passwordEncoder.encode(userRegisterDto.getPassword()));

        this.userRepository.save(user);
        return true;
    }

    public boolean login(UserLoginDto data) {
        Optional<User> byUsername = userRepository.findByUsername(data.getUsername());

        if (byUsername.isEmpty()) {
            return false;
        }

        boolean passMatch = passwordEncoder.matches(data.getPassword(), byUsername.get().getPassword());

        if (!passMatch) {
            return false;
        }
        userSession.login(byUsername.get().getId(), byUsername.get().getUsername());
        return true;
    }

    public Optional<User> getByUsername(String username) {
        return userRepository.findOptionalByUsername(username);
    }
}
