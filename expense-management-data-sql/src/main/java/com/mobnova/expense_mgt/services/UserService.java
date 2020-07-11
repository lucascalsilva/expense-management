package com.mobnova.expense_mgt.services;

import com.mobnova.expense_mgt.model.User;

public interface UserService extends BaseService<User, Long> {

    User findByUsername(String username);
}
