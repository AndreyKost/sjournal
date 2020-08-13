package sjournal.web;

import org.modelmapper.ModelMapper;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import sjournal.error.ArticleNotFoundException;
import sjournal.model.binding.ArticleAddBindingModel;
import sjournal.model.binding.ReviewAddBindingModel;
import sjournal.model.service.ArticleServiceModel;
import sjournal.model.service.ReviewServiceModel;
import sjournal.model.service.TopicServiceModel;
import sjournal.model.service.UserServiceModel;
import sjournal.model.view.ArticleAllViewModel;
import sjournal.service.ArticleService;
import sjournal.service.ReviewService;
import sjournal.service.TopicService;
import sjournal.service.UserService;

import javax.validation.Valid;
import java.security.Principal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Controller
@RequestMapping("/articles")
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

        for (ArticleServiceModel article : articles) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("uuuu-MM-dd'T'HH:mm");
            article.setAddedOn(LocalDateTime.parse(article.getAddedOn().format(formatter)));
        }


        modelAndView.addObject("articles", articles);
        return super.view("all-articles", modelAndView);
    }

    @GetMapping("/details/{id}")
    @PreAuthorize("isAuthenticated()")
    public ModelAndView detailsArticle(@PathVariable String id, ModelAndView modelAndView) {
        ArticleServiceModel articleById = this.articleService.findArticleById(id);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("uuuu-MM-dd'T'HH:mm");
        articleById.setAddedOn(LocalDateTime.parse(articleById.getAddedOn().format(formatter)));
        //todo ako ima nujda da napravq oshte pri inicializaciqta na datata da se parsva

        modelAndView.addObject("article",articleById);

        return super.view("details-article", modelAndView);
    }

    @GetMapping("/edit/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ModelAndView editArticle(@PathVariable String id, ModelAndView modelAndView){
        ArticleServiceModel articleServiceModel=this.articleService.findArticleById(id);

        modelAndView.addObject("article", articleServiceModel);
        modelAndView.addObject("articleId", id);

        return super.view("edit-article",modelAndView);

    }

    @PostMapping("/edit/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ModelAndView editArticleConfirm(@PathVariable String id,@ModelAttribute("articleAddBindingModel") ArticleAddBindingModel articleAddBindingModel){
        this.articleService.editArticle(id, this.modelMapper.map(articleAddBindingModel, ArticleServiceModel.class));

        return super.redirect("/articles/details/"+id);
    }

    @GetMapping("/delete/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ModelAndView deleteArticle(@PathVariable String id, ModelAndView modelAndView) {
        ArticleServiceModel articleServiceModel = this.articleService.findArticleById(id);

        modelAndView.addObject("article", articleServiceModel);
        modelAndView.addObject("articleId", id);

        return super.view("delete-article", modelAndView);
    }

    @PostMapping("/delete/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ModelAndView deleteArticleConfirm(@PathVariable String id) {

        if(this.reviewService.isArticleExist(id)){
            ReviewServiceModel reviewToDelBefArticle=this.reviewService.findByArticleId(id);
            this.reviewService.deleteReview(reviewToDelBefArticle);
        }


        //todo ako ima nujda pri opravqne na review-tata trqbva da opravq i tozi metodi za da trie vs reviw-ta a ne samo edno
        this.articleService.deleteArticle(id);

        return super.redirect("/articles/all");
    }

    @ExceptionHandler({ArticleNotFoundException.class})
    public ModelAndView handleQuoteNotFound(ArticleNotFoundException e) {
        ModelAndView modelAndView = new ModelAndView("error");
        modelAndView.addObject("message", e.getMessage());
        modelAndView.addObject("statusCode", e.getStatus());

        return modelAndView;
    }

    @GetMapping("/allArticlesRatings")
    @PreAuthorize("isAuthenticated()")
    public ModelAndView showAllArticlesRatings(ModelAndView modelAndView){
        List<ReviewServiceModel> wholeReviews = this.reviewService.findWholeReviews();
        LinkedHashMap<String,List<Double>> articlesNamesWithScores=new LinkedHashMap<>();

        for (ReviewServiceModel wholeReview : wholeReviews) {
            String name = wholeReview.getArticle().getName();
            int score = wholeReview.getScore();
            if(!articlesNamesWithScores.containsKey(name)){
                articlesNamesWithScores.put(name,new LinkedList<>());
                articlesNamesWithScores.get(name).add((double) score);
            }
            else{
                articlesNamesWithScores.get(name).add((double) score);
            }
        }

        LinkedHashMap<String, Double> artWithScore=new LinkedHashMap<>();
        LinkedList<Integer> numberOfReviews=new LinkedList<>();

        for (Map.Entry<String, List<Double>> stringListEntry : articlesNamesWithScores.entrySet()) {
            double asDouble = stringListEntry.getValue().stream().mapToDouble(e -> e).average().getAsDouble();
            List<Double> times = stringListEntry.getValue();
            artWithScore.put(stringListEntry.getKey(),(Math.floor(asDouble * 100) / 100));
            numberOfReviews.add(times.size());
        }

        modelAndView.addObject("artWithScore", artWithScore);
        modelAndView.addObject("numberOfReviews", numberOfReviews);
        return super.view("articles-ratings", modelAndView);
    }



}
