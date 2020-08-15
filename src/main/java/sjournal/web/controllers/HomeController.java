package sjournal.web.controllers;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;
import sjournal.service.TopicService;
import sjournal.service.UserService;
import sjournal.web.annotations.PageTitle;



@Controller

public class HomeController extends BaseController {
    private final UserService userService;
    private final TopicService topicService;

    public HomeController(UserService userService, TopicService topicService) {
        this.userService = userService;
        this.topicService = topicService;
    }





    @GetMapping("/")
    @PreAuthorize("isAnonymous()")
    @PageTitle("Index")
    public ModelAndView index(){
        return super.view("index");
    }

    @GetMapping("/home")
    @PageTitle("Home")
    @PreAuthorize("isAuthenticated()")
    public String home(Model model){


        model.addAttribute("latestTopic",this.topicService.findAllTopicNames());

        return "home";
    }

}
