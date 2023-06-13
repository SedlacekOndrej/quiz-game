package com.sedlacek.quiz.service;

import com.sedlacek.quiz.dto.LoginResponseDto;
import com.sedlacek.quiz.dto.ResponseMessageDto;
import com.sedlacek.quiz.dto.UserDto;
import com.sedlacek.quiz.entity.EntityBase;
import com.sedlacek.quiz.entity.User;
import com.sedlacek.quiz.repository.UserRepository;
import com.sun.istack.NotNull;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;

    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();


    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public ResponseEntity<ResponseMessageDto> registration(@NotNull UserDto userDTO) {
        if (userRepository.existsByUsername(userDTO.getUsername())) {
            return ResponseEntity.badRequest().body(new ResponseMessageDto("Účet s tímto uživatelským jménem již existuje"));
        }
        if (userRepository.existsByEmail(userDTO.getEmail())) {
            return ResponseEntity.badRequest().body(new ResponseMessageDto("Účet s tímto emailem již existuje"));
        }
        User user = EntityBase.convert(userDTO, User.class);
        user.setPassword(encoder.encode(user.getPassword()));
        userRepository.save(user);
        return ResponseEntity.ok(new ResponseMessageDto("Uživatel " + userDTO.getUsername() + " úspěšně zaregistrován"));
    }

    public ResponseEntity<List<UserDto>> getAllUsersByExp() {
        List<User> users = userRepository.findAllByOrderByExpDesc();
        List<UserDto> userDtos = users.stream().map(user -> EntityBase.convert(user, UserDto.class)).toList();
        return ResponseEntity.ok(userDtos);
    }

    public ResponseEntity<LoginResponseDto> login(@NotNull UserDto userDto) {
        User user = userRepository.findByUsername(userDto.getUsername());
        if (user != null && encoder.matches(userDto.getPassword(), user.getPassword())) {
            UserDto responseUser = EntityBase.convert(user, UserDto.class);
            return ResponseEntity.ok(new LoginResponseDto(responseUser, "Přihlášení proběhlo úspěšně"));
        }
        return ResponseEntity.badRequest().body(new LoginResponseDto(null, "Špatné uživatelské jméno nebo heslo"));
    }

    public ResponseEntity<UserDto> getUser(@NotNull long id) {
        User user = userRepository.findById(id);
        return ResponseEntity.ok(EntityBase.convert(user, UserDto.class));
    }

}
