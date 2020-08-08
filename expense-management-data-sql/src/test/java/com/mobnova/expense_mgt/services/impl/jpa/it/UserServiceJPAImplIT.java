package com.mobnova.expense_mgt.services.impl.jpa.it;

import com.mobnova.expense_mgt.dto.v1.UserDto;
import com.mobnova.expense_mgt.exception.constant.Fields;
import com.mobnova.expense_mgt.exceptions.DataNotFoundException;
import com.mobnova.expense_mgt.model.User;
import com.mobnova.expense_mgt.services.impl.jpa.UserServiceJPAImpl;
import com.mobnova.expense_mgt.util.AssertionUtils;
import com.mobnova.expense_mgt.util.IntegrationTestHelper;
import org.assertj.core.api.Assertions;
import org.assertj.core.api.AssertionsForClassTypes;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.validation.ConstraintViolationException;
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
        UserDto userDto = UserDto.builder().username("user_one").password("123456").email("user_one@domain.com")
                .firstName("Lucas").lastName("Silva").build();

        UserDto savedUserDto = userServiceJPA.save(userDto);

        assertThat(savedUserDto.getId()).isNotNull();
        assertThat(savedUserDto.getCreationDate()).isNotNull();
    }


    @Test
    void saveValidationError() {
        UserDto userDto = UserDto.builder().username("user_one").build();

        ConstraintViolationException constraintViolationException = assertThrows(ConstraintViolationException.class, () -> userServiceJPA.save(userDto));
        Assertions.assertThat(constraintViolationException.getMessage()).contains("save.arg0.email: must not be blank");
        Assertions.assertThat(constraintViolationException.getMessage()).contains("save.arg0.password: must not be blank");
        Assertions.assertThat(constraintViolationException.getMessage()).contains("save.arg0.firstName: must not be blank");
        Assertions.assertThat(constraintViolationException.getMessage()).contains("save.arg0.lastName: must not be blank");
    }


    @Test
    void update() {
        UserDto userDto = UserDto.builder().username("user_seven").password("123456").email("user_seven@domain.com")
                .firstName("Lucas").lastName("Silva").build();

        userDto = userServiceJPA.save(userDto);

        assertThat(userDto).isNotNull();
        assertThat(userDto.getId()).isNotNull();

        UserDto updatedUserDto = userServiceJPA.findById(userDto.getId());

        updatedUserDto.setEmail("user_seven1@domain.com");

        updatedUserDto = userServiceJPA.save(updatedUserDto);

        AssertionUtils.dtoUpdateAssertions(userDto, updatedUserDto);
        assertThat(userDto.getEmail()).isNotEqualTo(updatedUserDto.getEmail());
    }

    @Test
    void saveBulk() {
        UserDto userDto1 = UserDto.builder().username("user_two").password("123456").email("user_two@domain.com")
                .firstName("Lucas").lastName("Silva").build();
        UserDto userDto2 = UserDto.builder().username("user_three").password("123456").email("user_three@domain.com")
                .firstName("Lucas").lastName("Silva").build();
        Set<UserDto> userDtos = new HashSet<>();

        userDtos.add(userDto1);
        userDtos.add(userDto2);

        Set<UserDto> savedUserDtos = userServiceJPA.saveBulk(userDtos);

        for(UserDto userDto : savedUserDtos){
            assertThat(userDto.getId()).isNotNull();
            assertThat(userDto.getCreationDate()).isNotNull();
        }
    }

    @Test
    void findById() {
        UserDto userDto = UserDto.builder().username("user_four").password("123456").email("user_foura@domain.com")
                .firstName("Lucas").lastName("Silva").build();

        UserDto savedUserDto = userServiceJPA.save(userDto);
        Long savedUserId = savedUserDto.getId();

        UserDto userById = userServiceJPA.findById(savedUserId);

        assertThat(userById).isEqualTo(savedUserDto);
    }

    @Test
    void deleteById() {
        UserDto userDto = UserDto.builder().username("user_five").password("123456").email("user_five@domain.com")
                .firstName("Lucas").lastName("Silva").build();

        UserDto savedUser = userServiceJPA.save(userDto);
        Long savedUserId = savedUser.getId();

        userServiceJPA.deleteById(savedUserId);

        assertThrows(DataNotFoundException.class, () -> userServiceJPA.findById(savedUserId));

    }

    @Test
    void findByUsername() {
        UserDto userDto = UserDto.builder().username("user_six").password("123456").email("user_six@domain.com")
                .firstName("Lucas").lastName("Silva").build();

        UserDto savedUserDto = userServiceJPA.save(userDto);
        String savedUserUsername = savedUserDto.getUsername();

        UserDto userByUsernameDto = userServiceJPA.findByUsername(savedUserUsername);

        assertThat(userByUsernameDto).isEqualTo(savedUserDto);
    }

    @Test
    void findByUsernameNotFound() {
        String username = "someUser";
        DataNotFoundException dataNotFoundException = assertThrows(DataNotFoundException.class, () -> userServiceJPA.findByUsername(username));
        AssertionsForClassTypes.assertThat(dataNotFoundException.getMessage()).containsIgnoringCase(User.class.getSimpleName());
        AssertionsForClassTypes.assertThat(dataNotFoundException.getMessage()).containsIgnoringCase(Fields.USERNAME.toString());
        AssertionsForClassTypes.assertThat(dataNotFoundException.getMessage()).containsIgnoringCase(username);
    }

    @Test
    void findByIdNotFound() {
        Long id = 1000L;
        DataNotFoundException dataNotFoundException = assertThrows(DataNotFoundException.class, () -> userServiceJPA.findById(id));
        AssertionsForClassTypes.assertThat(dataNotFoundException.getMessage()).containsIgnoringCase(User.class.getSimpleName());
        AssertionsForClassTypes.assertThat(dataNotFoundException.getMessage()).containsIgnoringCase(Fields.ID.toString());
        AssertionsForClassTypes.assertThat(dataNotFoundException.getMessage()).containsIgnoringCase(id.toString());
    }
}