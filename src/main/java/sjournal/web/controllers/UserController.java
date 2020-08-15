package sjournal.web.controllers;


import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import sjournal.model.binding.UserAddBindingModel;
import sjournal.model.binding.UserEditBindingModel;
import sjournal.model.service.RoleServiceModel;
import sjournal.model.service.UserServiceModel;
import sjournal.model.view.UserProfileViewModel;
import sjournal.service.UserService;
import sjournal.web.annotations.PageTitle;

import javax.validation.Valid;
import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/users")
public class UserController extends BaseController {

    private final UserService userService;
    private final ModelMapper modelMapper;

    @Autowired
    public UserController(UserService userService, ModelMapper modelMapper) {
        this.userService = userService;
        this.modelMapper = modelMapper;
    }



    @GetMapping("/login")
    @PageTitle("Login")
    public ModelAndView login() {
        return super.view("login");
    }

    @GetMapping("/register")
    @PageTitle("Register")
    public String register(@Valid @ModelAttribute("userAddBindingModel")
                                       UserAddBindingModel userAddBindingModel,
                           BindingResult bindingResult){
        return "register";
    }

    @PostMapping("/register")
    public ModelAndView registerConfirm(@Valid @ModelAttribute("userAddBindingModel")
                                                    UserAddBindingModel userAddBindingModel,
                                        BindingResult bindingResult, ModelAndView modelAndView,
                                        RedirectAttributes redirectAttributes){


        if(bindingResult.hasErrors()){

            redirectAttributes.addFlashAttribute("userAddBindingModel",userAddBindingModel);
            modelAndView.setViewName("redirect:/users/register");
        } else {
            UserServiceModel userServiceModel=this.userService.registerUser(modelMapper.map(userAddBindingModel,UserServiceModel.class));

            modelAndView.setViewName("redirect:/users/login");
        }
        return modelAndView;
    }



    @GetMapping("/profile")
    @PreAuthorize("isAuthenticated()")
    @PageTitle("Profile")
    public ModelAndView profile(Principal principal, ModelAndView modelAndView){
        modelAndView
                .addObject("userprofile", this.modelMapper
                        .map(this.userService.findByUsername(principal.getName()), UserProfileViewModel.class));
        return super.view("/profile", modelAndView);
    }

    @GetMapping("/edit")
    @PreAuthorize("isAuthenticated()")
    @PageTitle("Edit User")
    public ModelAndView editProfile(Principal principal, ModelAndView modelAndView) {
        modelAndView
                .addObject("model", this.modelMapper.map(this.userService.findByUsername(principal.getName()), UserProfileViewModel.class));

        return super.view("edit-profile", modelAndView);
    }

    @PostMapping("/edit")
    @PreAuthorize("isAuthenticated()")
    public ModelAndView editProfileConfirm(@ModelAttribute UserEditBindingModel model){
        if (!model.getPassword().equals(model.getConfirmPassword())){
            return super.view("edit-profile");
        }

        this.userService.editUserProfile(this.modelMapper.map(model, UserServiceModel.class), model.getOldPassword());

        return super.redirect("/users/profile");
    }

    @GetMapping("/all")
    @PreAuthorize("isAuthenticated()")
    @PageTitle("All users")
    public ModelAndView showAllUsers(ModelAndView modelAndView){
        List<UserServiceModel> users = this.userService.findAllUsers()
                .stream()
                .map(user -> this.modelMapper.map(user, UserServiceModel.class))
                .collect(Collectors.toList());

        Map<String, Set<RoleServiceModel>> userAndAuthorities = new HashMap<>();
        users.forEach(u -> userAndAuthorities.put(u.getId(), u.getAuthorities()));

        modelAndView.addObject("users", users);
        modelAndView.addObject("usersAndAuths", userAndAuthorities);
        return super.view("all-users", modelAndView);
    }

    @GetMapping("/delete/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    @PageTitle("Delete User")
    public ModelAndView deleteUser(@PathVariable String id, ModelAndView modelAndView) {
        UserServiceModel userServiceModel = this.userService.findById(id);

        modelAndView.addObject("user", userServiceModel);
        modelAndView.addObject("userId", id);

        return super.view("delete-user", modelAndView);
    }

    @PostMapping("/delete/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ModelAndView deleteUserConfirm(@PathVariable String id) {
        this.userService.deleteUser(id);

        return super.redirect("/users/all");
    }

}
