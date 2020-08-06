package sjournal.service.impl;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import sjournal.error.Constants;
import sjournal.model.entity.Role;
import sjournal.model.entity.User;
import sjournal.model.service.RoleServiceModel;
import sjournal.model.service.UserServiceModel;
import sjournal.repository.UserRepository;
import sjournal.service.RoleService;
import sjournal.service.UserService;

import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService  {
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final RoleService roleService;
    private BCryptPasswordEncoder bCryptPasswordEncoder;


    @Autowired
    public UserServiceImpl(UserRepository userRepository, ModelMapper modelMapper, RoleService roleService, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
        this.roleService = roleService;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @Override
    public UserServiceModel registerUser(UserServiceModel userServiceModel) {
        if(this.userRepository.count()==0){
            userServiceModel.setAuthorities(this.roleService.findAllRoles());
        } else {
            userServiceModel.setAuthorities(new LinkedHashSet<>());
            RoleServiceModel roleAuthorityAdd = this.roleService.findByAuth("USER");
            userServiceModel.getAuthorities().add(roleAuthorityAdd);
        }

        User user=this.modelMapper
                .map(userServiceModel,User.class);
            user.setPassword(this.bCryptPasswordEncoder.encode(userServiceModel.getPassword()));

        return  this.modelMapper.map(this.userRepository.saveAndFlush(user),UserServiceModel.class);
    }

    @Override
    public UserServiceModel findByUsername(String username) {

        return this.userRepository.findByUsername(username)
                .map(user -> this.modelMapper.map(user,UserServiceModel.class))
                .orElse(null);
    }

    @Override
    public List<String> findAllUsernames() {
        return this.userRepository.findAll()
                .stream()
                .map(User::getUsername)
                .collect(Collectors.toList());
    }



    @Override
    public UserServiceModel findById(String id) {

        return this.userRepository
                .findById(id)
                .map(user -> this.modelMapper.map(user,UserServiceModel.class))
                .orElse(null);

    }


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return this.userRepository
                .findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(Constants.USERNAME_NOT_FOUND));
    }
}
