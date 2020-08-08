package com.mobnova.expense_mgt.services.impl.jpa.test;

import com.mobnova.expense_mgt.config.ModelMapperConfiguration;
import com.mobnova.expense_mgt.dto.v1.UserDto;
import com.mobnova.expense_mgt.exception.constant.Fields;
import com.mobnova.expense_mgt.exceptions.DataNotFoundException;
import com.mobnova.expense_mgt.model.User;
import com.mobnova.expense_mgt.repositories.UserRepository;
import com.mobnova.expense_mgt.services.impl.jpa.UserServiceJPAImpl;
import org.assertj.core.api.AssertionsForClassTypes;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceJPAImplTest {

    private UserServiceJPAImpl userServiceJPA;

    @Mock
    private UserRepository userRepository;

    private ModelMapper modelMapper;

    @BeforeEach
    public void setup() {
        modelMapper = new ModelMapperConfiguration().globalMapper();
        userServiceJPA = Mockito.spy(new UserServiceJPAImpl(userRepository, modelMapper));
    }

    @Test
    void save() {
        UserDto userDto = UserDto.builder().id(1L).username("user_one").password("123456").email("lucas.silva@domain.com")
                .firstName("Lucas").lastName("Silva").build();
        User user = modelMapper.map(userDto, User.class);

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        doAnswer(returnsFirstArg()).when(userRepository).save(any(User.class));

        UserDto savedUserDto = userServiceJPA.save(userDto);

        verify(userRepository, times(1)).save(user);
    }

    @Test
    void saveBulk() {
        UserDto userDto1 = UserDto.builder().id(1L).username("user_one").password("123456").email("lucas.silva@domain.com")
                .firstName("Lucas").lastName("Silva").build();
        User user1 = modelMapper.map(userDto1, User.class);

        UserDto userDto2 = UserDto.builder().id(2L).username("user_two").password("123456").email("silva.lucas@domain.com")
                .firstName("Silva").lastName("Lucas").build();
        User user2 = modelMapper.map(userDto2, User.class);

        Set<UserDto> userDtos = new HashSet<>();
        userDtos.add(userDto1);
        userDtos.add(userDto2);

        when(userRepository.findById(1L)).thenReturn(Optional.of(user1));
        when(userRepository.findById(2L)).thenReturn(Optional.of(user2));

        doAnswer(returnsFirstArg()).when(userRepository).save(any(User.class));

        Set<UserDto> savedUserDtos = userServiceJPA.saveBulk(userDtos);
        assertThat(savedUserDtos).hasSize(2);

        verify(userRepository, times(2)).save(any(User.class));
        verify(userServiceJPA, times(2)).save(any(UserDto.class));
    }

    @Test
    void findById() {
        UserDto userDto = UserDto.builder().id(1L).username("user_one").password("123456").email("lucas.silva@domain.com")
                .firstName("Lucas").lastName("Silva").build();
        User user = modelMapper.map(userDto, User.class);

        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));

        UserDto userByIdDto = userServiceJPA.findById(1L);

        verify(userRepository, times(1)).findById(1L);

        assertThat(userByIdDto).isEqualTo(userDto);
    }

    @Test
    void deleteById() {
        userServiceJPA.deleteById(1L);

        verify(userRepository, times(1)).deleteById(1L);
    }

    @Test
    void findByUsername() {
        UserDto userDto = UserDto.builder().id(1L).username("user_one").password("123456").email("lucas.silva@domain.com")
                .firstName("Lucas").lastName("Silva").build();
        User user = modelMapper.map(userDto, User.class);

        when(userRepository.findByUsername(user.getUsername())).thenReturn(Optional.of(user));

        UserDto userByUsernameDto = userServiceJPA.findByUsername("user_one");

        verify(userRepository, times(1)).findByUsername(user.getUsername());

        assertThat(userByUsernameDto).isEqualTo(userDto);
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