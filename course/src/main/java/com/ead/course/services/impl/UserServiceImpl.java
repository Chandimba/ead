package com.ead.course.services.impl;

import com.ead.course.repositories.UserRepository;
import com.ead.course.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

}
