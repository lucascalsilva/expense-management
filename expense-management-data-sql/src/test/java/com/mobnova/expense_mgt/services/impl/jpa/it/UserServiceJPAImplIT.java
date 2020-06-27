package com.mobnova.expense_mgt.services.impl.jpa.it;

import com.mobnova.expense_mgt.exception.constant.Fields;
import com.mobnova.expense_mgt.exceptions.DataNotFoundException;
import com.mobnova.expense_mgt.model.User;
import com.mobnova.expense_mgt.services.impl.jpa.UserServiceJPAImpl;
import com.mobnova.expense_mgt.util.AssertionsUtil;
import com.mobnova.expense_mgt.util.IntegrationTestHelper;
import org.assertj.core.api.AssertionsForClassTypes;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.HashSet;
import java.util.Set;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class UserServiceJPAImplIT {

    @Autowired
    private UserServiceJPAImpl userServiceJPA;

    @Autowired
    private IntegrationTestHelper integrationTestHelper;

    private static Boolean dbInitialized = false;

    @BeforeEach
    public void init(){
        if(!dbInitialized) {
            integrationTestHelper.cleanAllData();
            dbInitialized = true;
        }
    }

    @Test
    void save() {
        User user = User.builder().username("user_one").password("123456").email("user_one@domain.com")
                .firstName("Lucas").lastName("Silva").build();

        User savedUser = userServiceJPA.save(user);

        assertThat(savedUser.getId()).isNotNull();
        assertThat(savedUser.getCreationDate()).isNotNull();
    }

    @Test
    void update() {
        User user = User.builder().username("user_seven").password("123456").email("user_seven@domain.com")
                .firstName("Lucas").lastName("Silva").build();

        user = userServiceJPA.save(user);

        assertThat(user).isNotNull();
        assertThat(user.getId()).isNotNull();

        User updatedUser = userServiceJPA.findById(user.getId());

        updatedUser.setEmail("user_seven1@domain.com");

        updatedUser = userServiceJPA.save(updatedUser);

        AssertionsUtil.entityUpdateAssertions(user, updatedUser);
        assertThat(user.getEmail()).isNotEqualTo(updatedUser.getEmail());
    }

    @Test
    void saveBulk() {
        User user1 = User.builder().username("user_two").password("123456").email("user_two@domain.com")
                .firstName("Lucas").lastName("Silva").build();
        User user2 = User.builder().username("user_three").password("123456").email("user_three@domain.com")
                .firstName("Lucas").lastName("Silva").build();
        Set<User> users = new HashSet<>();

        users.add(user1);
        users.add(user2);

        Set<User> savedUsers = userServiceJPA.saveBulk(users);

        for(User user : savedUsers){
            assertThat(user.getId()).isNotNull();
            assertThat(user.getCreationDate()).isNotNull();
        }
    }

    @Test
    void findById() {
        User user = User.builder().username("user_four").password("123456").email("user_foura@domain.com")
                .firstName("Lucas").lastName("Silva").build();

        User savedUser = userServiceJPA.save(user);
        Long savedUserId = savedUser.getId();

        User userById = userServiceJPA.findById(savedUserId);

        assertThat(userById).isEqualTo(user);
    }

    @Test
    void deleteById() {
        User user = User.builder().username("user_five").password("123456").email("user_five@domain.com")
                .firstName("Lucas").lastName("Silva").build();

        User savedUser = userServiceJPA.save(user);
        Long savedUserId = savedUser.getId();

        userServiceJPA.deleteById(savedUserId);

        assertThrows(DataNotFoundException.class, () -> userServiceJPA.findById(savedUserId));

    }

    @Test
    void findByUsername() {
        User user = User.builder().username("user_six").password("123456").email("user_six@domain.com")
                .firstName("Lucas").lastName("Silva").build();

        User savedUser = userServiceJPA.save(user);
        String savedUserUsername = savedUser.getUsername();

        User userByUsername = userServiceJPA.findByUsername(savedUserUsername);

        assertThat(userByUsername).isEqualTo(user);
    }

    @Test
    void findByUsernameNotFound() {
        String username = "someUser";
        DataNotFoundException dataNotFoundException = assertThrows(DataNotFoundException.class, () -> userServiceJPA.findByUsername(username));
        AssertionsForClassTypes.assertThat(dataNotFoundException.getMessage()).containsIgnoringCase(User.class.getName());
        AssertionsForClassTypes.assertThat(dataNotFoundException.getMessage()).containsIgnoringCase(Fields.USERNAME.toString());
        AssertionsForClassTypes.assertThat(dataNotFoundException.getMessage()).containsIgnoringCase(username);
    }

    @Test
    void findByIdNotFound() {
        Long id = 1000L;
        DataNotFoundException dataNotFoundException = assertThrows(DataNotFoundException.class, () -> userServiceJPA.findById(id));
        AssertionsForClassTypes.assertThat(dataNotFoundException.getMessage()).containsIgnoringCase(User.class.getName());
        AssertionsForClassTypes.assertThat(dataNotFoundException.getMessage()).containsIgnoringCase(Fields.ID.toString());
        AssertionsForClassTypes.assertThat(dataNotFoundException.getMessage()).containsIgnoringCase(id.toString());
    }
}