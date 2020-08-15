package sjournal.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import sjournal.model.service.UserServiceModel;


import java.util.Arrays;
import java.util.List;

public interface UserService extends UserDetailsService {

    UserServiceModel registerUser(UserServiceModel userServiceModel);

    UserServiceModel findByUsername(String username);

    List<String> findAllUsernames();

    UserServiceModel findById(String id);

    UserServiceModel editUserProfile(UserServiceModel userServiceModel, String oldPassword);

    @Override
    UserDetails loadUserByUsername(String username) throws UsernameNotFoundException;

    List<UserServiceModel> findAllUsers();

    void deleteUser(String id);
}
