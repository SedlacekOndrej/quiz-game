package com.sedlacek.quiz.service;

import com.sedlacek.quiz.dto.*;
import com.sedlacek.quiz.entity.EntityBase;
import com.sedlacek.quiz.entity.User;
import com.sedlacek.quiz.exception.ResourceNotFoundException;
import com.sedlacek.quiz.repository.UserRepository;
import com.sun.istack.NotNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;

    private final JavaMailSender javaMailSender;

    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();


    public UserService(UserRepository userRepository, JavaMailSender javaMailSender) {
        this.userRepository = userRepository;
        this.javaMailSender = javaMailSender;
    }


    /**
     * Creates a new <code>User</code> object based on information provided on UI and saves it to the database.
     * Also checks if the provided data are not duplicated and exists in database already.
     *
     * @param userDTO userDto object with all the necessary information to register
     * @return response with appropriate message and ok status if the provided data was ok, bad request status otherwise
     */
    public ResponseEntity<String> registerNewUser(@NotNull UserDto userDTO) {
        if (userRepository.existsByUsername(userDTO.getUsername())) {
            return ResponseEntity.badRequest().body("Účet s tímto uživatelským jménem již existuje");
        }

        if (userRepository.existsByEmail(userDTO.getEmail())) {
            return ResponseEntity.badRequest().body("Účet s tímto emailem již existuje");
        }

        User user = EntityBase.convert(userDTO, User.class);

        user.setPassword(encoder.encode(user.getPassword()));

        userRepository.save(user);

        sendConfirmationEmail(user.getEmail(), user.getUsername());

        return ResponseEntity.ok("Uživatel " + userDTO.getUsername() + " úspěšně zaregistrován");
    }

    /**
     * Selects all the <code>User</code> objects from the database and returns it ordered by experience points.
     *
     * @return response with ok status containing list of user objects
     */
    public ResponseEntity<List<UserDto>> getAllUsersOrderByExp() {
        List<User> users = userRepository.findAllByOrderByExpDesc();
        List<UserDto> userDtos = users.stream().map(user -> EntityBase.convert(user, UserDto.class)).toList();

        return ResponseEntity.ok(userDtos);
    }

    /**
     * Checks if the username and password data provided in the <code>UserDto</code> object is valid. If so, selects particular
     * <code>User</code> object from the database, converts it to <code>UserDto</code> object with all the stored information
     * and sends it back to the UI part of the application with corresponding message.
     *
     * @param userDto userDto object containing username and password required to log in
     * @return response with appropriate message and ok status if the username and password was correct, bad request status otherwise
     */
    public ResponseEntity<LoginResponseDto> loginUser(@NotNull UserDto userDto) {
        User user = userRepository.findByUsername(userDto.getUsername());

        if (encoder.matches(userDto.getPassword(), user.getPassword())) {
            UserDto responseUser = EntityBase.convert(user, UserDto.class);
            return ResponseEntity.ok(new LoginResponseDto(responseUser, "Přihlášení proběhlo úspěšně"));
        }

        return ResponseEntity.badRequest().body(new LoginResponseDto(null, "Špatné uživatelské jméno nebo heslo"));
    }

    /**
     * Gets particular <code>User</code> object from the database based on ID provided as a param and returns it as a DTO in a response.
     * If the user can not be found throws an exception.
     *
     * @param id  unique primary key number identifier
     * @return response with UserDto object if the User was found, exception otherwise
     * @throws ResourceNotFoundException if the user can not be found
     */
    public ResponseEntity<UserDto> getUserById(@NotNull long id) throws ResourceNotFoundException {
        User user = userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Uživatel s ID " + id + "nenalezen!"));

        return ResponseEntity.ok(EntityBase.convert(user, UserDto.class));
    }

    /**
     * Gets particular <code>User</code> object from the database based on ID provided as a param, updates corresponding properties and returns a response.
     * If the user can not be found throws an exception.
     *
     * @param id unique primary key number identifier
     * @param editUserDto object containing password and UserDto object
     * @return response with appropriate message if the data is valid, exception otherwise
     * @throws ResourceNotFoundException if the user can not be found
     */
    public ResponseEntity<String> updateUser(long id, EditUserDto editUserDto) throws ResourceNotFoundException {
        User user = userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Uživatel s ID " + id + "nenalezen!"));

        if (encoder.matches(editUserDto.getPassword(), user.getPassword())) {
            if (!user.getUsername().equals(editUserDto.getUser().getUsername())) {
                if (userRepository.existsByUsername(editUserDto.getUser().getUsername())) {
                    return ResponseEntity.badRequest().body("Účet s tímto uživatelským jménem již existuje");
                }
                user.setUsername(editUserDto.getUser().getUsername());

            } else if (!user.getPassword().equals(editUserDto.getUser().getPassword())) {
                user.setPassword(encoder.encode(editUserDto.getUser().getPassword()));

            } else if (!user.getEmail().equals(editUserDto.getUser().getEmail())) {
                if (userRepository.existsByEmail(editUserDto.getUser().getEmail())) {
                    return ResponseEntity.badRequest().body("Účet s tímto emailem již existuje");
                }
                user.setEmail(editUserDto.getUser().getEmail());
            }
            userRepository.save(user);

            return ResponseEntity.ok("Změna proběhla úspěšně!");

        } else {
            return ResponseEntity.badRequest().body("Špatně zadané heslo!");
        }
    }

    /**
     * Gets particular <code>User</code> object from the database based on ID provided as a param and deletes it.
     * If the user can not be found throws an exception.
     *
     * @param id unique primary key number identifier
     * @param password string value of user's password
     * @return response with no content status if the data is valid, exception otherwise
     * @throws ResourceNotFoundException if the user can not be found
     */
    public ResponseEntity<String> deleteUser(long id, String password) throws ResourceNotFoundException {
        User user = userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Uživatel s ID " + id + " nenalezen!"));

        if (encoder.matches(password, user.getPassword())) {
            userRepository.delete(user);

            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
        }

        return ResponseEntity.badRequest().body("Špatně zadané heslo!");
    }

    /**
     * Sends a confirmation email to an email address provided in the registration form.
     *
     * @param email string value of the email address
     * @param username string value of the user's name
     */
    private void sendConfirmationEmail(String email, String username) {
        SimpleMailMessage message = new SimpleMailMessage();

        message.setTo(email);
        message.setSubject("Potvrzení registrace");
        message.setText("Ahoj " + username
                + ",\n\nděkujeme za registraci do hry Kvíz. Přejeme hodně štěstí a pevné nervy ve hře.\n\nS pozdravem\nQuiz Game development team");

        javaMailSender.send(message);
    }

}
