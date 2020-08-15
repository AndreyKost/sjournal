package sjournal.web.controllers;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import sjournal.model.binding.TopicAddBindingModel;
import sjournal.model.service.TopicServiceModel;
import sjournal.service.TopicService;
import sjournal.web.annotations.PageTitle;


import javax.validation.Valid;

@Controller
@RequestMapping("/topics")
public class TopicController {
    private final TopicService topicService;
    private final ModelMapper modelMapper;

    @Autowired
    public TopicController(TopicService topicService, ModelMapper modelMapper) {
        this.topicService = topicService;
        this.modelMapper = modelMapper;
    }

    @GetMapping("/add")
    @PreAuthorize("hasAuthority('ADMIN')")
    @PageTitle("Add Topic")
    public String add(@Valid @ModelAttribute("topicAddBindingModel") TopicAddBindingModel topicAddBindingModel,
                      BindingResult bindingResult, RedirectAttributes redirectAttributes){
        return "topic-add";
    }

    @PostMapping("/add")
    @PreAuthorize("hasAuthority('ADMIN')")
    public String addConfirm(@Valid @ModelAttribute("topicAddBindingModel") TopicAddBindingModel topicAddBindingModel,
                             BindingResult bindingResult, RedirectAttributes redirectAttributes){

        if(bindingResult.hasErrors()){
            redirectAttributes.addFlashAttribute("topicAddBindingModel",topicAddBindingModel);
            return "redirect:/topics/add";
        } else {
            this.topicService.addTop(this.modelMapper.map(topicAddBindingModel, TopicServiceModel.class));

            return "redirect:/home";
        }
    }

}
