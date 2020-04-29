package com.mobnova.expense_mgt.services.impl.jpa.test;

import com.mobnova.expense_mgt.model.User;
import com.mobnova.expense_mgt.repositories.UserRepository;
import com.mobnova.expense_mgt.services.impl.jpa.UserServiceJPAImpl;
import com.mobnova.expense_mgt.validation.BeanValidator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceJPAImplTest {

    @InjectMocks
    @Spy
    private UserServiceJPAImpl userServiceJPA;

    @Mock
    private UserRepository userRepository;

    @Mock
    private BeanValidator beanValidator;

    @Test
    void save() {
        User user = User.builder().username("lucas.silva").password("123456").email("lucas.silva@camunda.com")
                .firstName("Lucas").lastName("Silva").build();

        doAnswer(returnsFirstArg()).when(userRepository).save(any(User.class));

        User savedUser = userServiceJPA.save(user);

        verify(userRepository, times(1)).save(user);
    }

    @Test
    void saveBulk() {
        User user1 = User.builder().username("lucas.silva").password("123456").email("lucas.silva@email.com")
                .firstName("Lucas").lastName("Silva").build();

        User user2 = User.builder().username("silva.lucas").password("123456").email("silva.lucas@email.com")
                .firstName("Silva").lastName("Lucas").build();

        Set<User> users = new HashSet<>();
        users.add(user1);
        users.add(user2);

        doAnswer(returnsFirstArg()).when(userRepository).save(any(User.class));

        Set<User> savedUsers = userServiceJPA.saveBulk(users);

        for(User user : savedUsers){
            verify(userRepository, times(1)).save(user);
            verify(userServiceJPA, times(1)).save(user);
        }
    }

    @Test
    void findById() {
        User user = User.builder().id(1L).username("lucas.silva").password("123456").email("lucas.silva@camunda.com")
                .firstName("Lucas").lastName("Silva").build();

        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));

        Optional<User> userById = userServiceJPA.findById(1L);

        verify(userRepository, times(1)).findById(1L);

        assertThat(userById.isPresent());
        assertThat(userById.get()).isEqualTo(user);
    }

    @Test
    void deleteById() {
        userServiceJPA.deleteById(1L);

        verify(userRepository, times(1)).deleteById(1L);
    }

    @Test
    void findByUsername() {
        User user = User.builder().username("lucas.silva").password("123456").email("lucas.silva@camunda.com")
                .firstName("Lucas").lastName("Silva").build();

        when(userRepository.findByUsername(user.getUsername())).thenReturn(Optional.of(user));

        Optional<User> userByUsername = userServiceJPA.findByUsername("lucas.silva");

        verify(userRepository, times(1)).findByUsername(user.getUsername());

        assertThat(userByUsername.isPresent());
        assertThat(userByUsername.get()).isEqualTo(user);
    }
}