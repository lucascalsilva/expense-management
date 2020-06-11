package com.mobnova.expense_mgt.services;

import com.mobnova.expense_mgt.model.User;

import java.util.Optional;

public interface UserService extends BaseService<User, Long> {

    User findByUsername(String username);
}
