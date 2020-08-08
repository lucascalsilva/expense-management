package com.mobnova.expense_mgt.services;

import com.mobnova.expense_mgt.dto.v1.UserDto;

public interface UserService extends BaseService<UserDto, Long> {

    UserDto findByUsername(String username);
}
