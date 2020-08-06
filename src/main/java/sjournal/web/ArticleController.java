package sjournal.web;

import org.modelmapper.ModelMapper;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import sjournal.model.binding.ArticleAddBindingModel;
import sjournal.model.binding.ReviewAddBindingModel;
import sjournal.model.service.ArticleServiceModel;
import sjournal.model.service.ReviewServiceModel;
import sjournal.model.service.TopicServiceModel;
import sjournal.model.service.UserServiceModel;
import sjournal.service.ArticleService;
import sjournal.service.ReviewService;
import sjournal.service.TopicService;
import sjournal.service.UserService;

import javax.validation.Valid;
import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/article")
public class ArticleController extends BaseController   {
    private final TopicService topicService;
    private final ModelMapper modelMapper;
    private final ArticleService articleService;
    private final ReviewService reviewService;
    private final UserService userService;

    public ArticleController(TopicService topicService, ModelMapper modelMapper, ArticleService articleService, ReviewService reviewService, UserService userService) {
        this.topicService = topicService;
        this.modelMapper = modelMapper;
        this.articleService = articleService;
        this.reviewService = reviewService;
        this.userService = userService;
    }

    @GetMapping("/add")
    @PreAuthorize("isAuthenticated()")
    public String add(Model model){
        if(!model.containsAttribute("articleAddBindingModel")){
            model.addAttribute("articleAddBindingModel",new ArticleAddBindingModel());
        }

        model.addAttribute("allNewTopics",this.topicService.findAllTopicNames());
        return "article-add";
    }

    @PostMapping("/add")
    @PreAuthorize("isAuthenticated()")
    public String addConfirm(@Valid @ModelAttribute("articleAddBindingModel")ArticleAddBindingModel articleAddBindingModel,
                             BindingResult bindingResult, RedirectAttributes redirectAttributes, Principal principal){

        TopicServiceModel topicServiceModel=this.topicService.findByName(articleAddBindingModel.getTopic());

        if(bindingResult.hasErrors()){
            redirectAttributes.addFlashAttribute("articleAddBindingModel",articleAddBindingModel);
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.articleAddBindingModel",bindingResult);
            return "redirect:add";
        }

        ArticleServiceModel articleServiceModel=this.modelMapper
                .map(articleAddBindingModel,ArticleServiceModel.class);
        articleServiceModel.setAddedOn(LocalDateTime.now());
        articleServiceModel.setTopic(topicServiceModel);
        String name = principal.getName();
        UserServiceModel userByUsername = userService.findByUsername(name);
        articleServiceModel.setAuthor(userByUsername);

        this.articleService.add(articleServiceModel);
        return "redirect:/home";
    }

    @GetMapping("/rate")
    @PreAuthorize("hasAuthority('ADMIN')")
    public String rate(Model model){

        if(!model.containsAttribute("reviewAddBindingModel")){
         model.addAttribute("reviewAddBindingModel",new ReviewAddBindingModel());
        }
        model.addAttribute("allLatestArticles",this.articleService.findAllArticles());

        return "article-rate";
    }

    @PostMapping("/rate")
    @PreAuthorize("hasAuthority('ADMIN')")
    public String rateConfirm(@Valid @ModelAttribute("reviewAddBindingModel")ReviewAddBindingModel reviewAddBindingModel
    , BindingResult bindingResult, RedirectAttributes redirectAttributes, Principal principal){

        if(bindingResult.hasErrors()){
            redirectAttributes.addFlashAttribute("reviewAddBindingModel",reviewAddBindingModel);
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.reviewAddBindingModel", bindingResult);

            return "redirect:rate";
        }

        ReviewServiceModel reviewServiceModel=this.modelMapper.map(reviewAddBindingModel,ReviewServiceModel.class);
        reviewServiceModel.setArticle(this.articleService.findByName(reviewAddBindingModel.getArticleId()));
        String name = principal.getName();
        UserServiceModel userByUsername = userService.findByUsername(name);
        reviewServiceModel.setAuthor(userByUsername);


        this.reviewService.add(reviewServiceModel);

        return "redirect:/home";

    }



    @GetMapping("/all")
    @PreAuthorize("isAuthenticated()")
    public ModelAndView showAllArticles(ModelAndView modelAndView){
        List<ArticleServiceModel> articles = this.articleService.findWholeArticles();

        modelAndView.addObject("articles", articles);
        return super.view("all-articles", modelAndView);
    }


}
