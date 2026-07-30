package com.parking.system.service;

import com.parking.system.dto.UserDto;
import com.parking.system.entity.User;

import java.util.List;

public interface UserService {
    UserDto registerUser(UserDto userDto);
    User findByEmail(String email);
    UserDto getUserProfile(String userId);
    List<UserDto> getAllUsers();
    void blockUserIfDuesPending(String userId);
    void unblockUser(String userId);
}
