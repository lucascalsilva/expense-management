package com.mobnova.expense_mgt.services.dto;

import com.mobnova.expense_mgt.dto.UserDto;

import java.util.Optional;

public interface UserService extends BaseService<UserDto, Long> {

    Optional<UserDto> findByUsername(String username);
}
