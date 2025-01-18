package com.backend.ServerPrac.domain.user;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service @Transactional @RequiredArgsConstructor
public class UserService
{
    private final UserRepository userRepository;

    public User submit()
    {
        User user = User.builder()
                                .name("admin")
                                .email("admin@gmail.com")
                                .password("123456")
                                .build();

        return userRepository.save(user);
    }

}
