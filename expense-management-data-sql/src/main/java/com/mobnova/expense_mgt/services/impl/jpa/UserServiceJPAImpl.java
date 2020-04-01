package com.mobnova.expense_mgt.services.impl.jpa;

import com.mobnova.expense_mgt.model.User;
import com.mobnova.expense_mgt.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Profile("jpa")
@RequiredArgsConstructor
public class UserServiceJPAImpl implements UserService {

    private final com.mobnova.expense_mgt.repositories.UserRepository repository;

    @Override
    public User save(User user) {
        return repository.save(user);
    }

    @Override
    public Set<User> saveBulk(Set<User> users) {
        return users.stream().map(this::save).collect(Collectors.toSet());
    }

    @Override
    public Optional<User> findById(Long id) {
        return repository.findById(id);
    }

    @Override
    public void deleteById(Long id) {
        repository.deleteById(id);
    }

    @Override
    public Optional<User> findByUsername(String username) {
        return repository.findByUsername(username);
    }
}
