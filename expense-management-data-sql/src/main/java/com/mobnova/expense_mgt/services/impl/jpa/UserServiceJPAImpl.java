package com.mobnova.expense_mgt.services.impl.jpa;

import com.mobnova.expense_mgt.dto.v1.UserDto;
import com.mobnova.expense_mgt.exception.constant.Fields;
import com.mobnova.expense_mgt.exceptions.DataNotFoundException;
import com.mobnova.expense_mgt.model.User;
import com.mobnova.expense_mgt.repositories.UserRepository;
import com.mobnova.expense_mgt.services.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

@Service
@Profile("jpa")
public class UserServiceJPAImpl extends AbstractBaseServiceJPA<User, UserDto, Long> implements UserService {

    private final UserRepository userRepository;

    public UserServiceJPAImpl(UserRepository userRepository, ModelMapper modelMapper) {
        super(userRepository, modelMapper, User.class, UserDto.class);
        this.userRepository = userRepository;
    }

    @Override
    public UserDto findByUsername(String username) {
        return modelMapper.map(userRepository.findByUsername(username)
                .orElseThrow(() -> new DataNotFoundException(User.class, Fields.USERNAME, username)), UserDto.class);
    }
}
