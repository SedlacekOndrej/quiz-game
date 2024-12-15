package com.sedlacek.quiz.service;

import com.sedlacek.quiz.dto.EditUserDto;
import com.sedlacek.quiz.dto.LoginResponseDto;
import com.sedlacek.quiz.dto.UserDto;
import com.sedlacek.quiz.entity.User;
import com.sedlacek.quiz.exception.ResourceNotFoundException;
import com.sun.istack.NotNull;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface UserService {
    /**
     * Creates a new <code>User</code> object based on information provided on UI and saves it to the database.
     * Also checks if the provided data are not duplicated and exists in database already.
     *
     * @param userDTO userDto object with all the necessary information to register
     * @return response with appropriate message and ok status if the provided data was ok, bad request status otherwise
     */
    String registerNewUser(@NotNull UserDto userDTO);

    /**
     * Selects all the <code>User</code> objects from the database and returns it ordered by experience points.
     *
     * @return response with ok status containing list of user objects
     */
    List<User> getAllUsersOrderByExp();

    /**
     * Checks if the username and password data provided in the <code>UserDto</code> object is valid. If so, selects particular
     * <code>User</code> object from the database, converts it to <code>UserDto</code> object with all the stored information
     * and sends it back to the UI part of the application with corresponding message.
     *
     * @param userDto userDto object containing username and password required to log in
     * @return response with appropriate message and ok status if the username and password was correct, bad request status otherwise
     */
    ResponseEntity<LoginResponseDto> loginUser(@NotNull UserDto userDto);

    /**
     * Gets particular <code>User</code> object from the database based on ID provided as a param and returns it as a DTO in a response.
     * If the user can not be found throws an exception.
     *
     * @param id  unique primary key number identifier
     * @return response with UserDto object if the User was found, exception otherwise
     * @throws ResourceNotFoundException if the user can not be found
     */
    User getUserById(@NotNull long id) throws ResourceNotFoundException;

    /**
     * Gets particular <code>User</code> object from the database based on ID provided as a param, updates corresponding properties and returns a response.
     * If the user can not be found throws an exception.
     *
     * @param id unique primary key number identifier
     * @param editUserDto object containing password and UserDto object
     * @return response with appropriate message if the data is valid, exception otherwise
     * @throws ResourceNotFoundException if the user can not be found
     */
    String updateUser(long id, EditUserDto editUserDto) throws ResourceNotFoundException;

    /**
     * Gets particular <code>User</code> object from the database based on ID provided as a param and deletes it.
     * If the user can not be found throws an exception.
     *
     * @param id unique primary key number identifier
     * @param password string value of user's password
     * @return response with no content status if the data is valid, exception otherwise
     * @throws ResourceNotFoundException if the user can not be found
     */
    String deleteUser(long id, String password) throws ResourceNotFoundException;
}
