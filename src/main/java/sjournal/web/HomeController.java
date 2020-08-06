package sjournal.web;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import sjournal.service.TopicService;
import sjournal.service.UserService;


import javax.servlet.http.HttpSession;

@Controller

public class HomeController {
    private final UserService userService;
    private final TopicService topicService;

    public HomeController(UserService userService, TopicService topicService) {
        this.userService = userService;
        this.topicService = topicService;
    }

    



    @GetMapping("/")
    public String index(){
        return "index";
    }

    @GetMapping("/home")
    @PreAuthorize("isAuthenticated()")
    public String home(Model model, HttpSession httpSession){

       
        model.addAttribute("latestTopic",this.topicService.findAllTopicNames());

        return "home";
    }

}
