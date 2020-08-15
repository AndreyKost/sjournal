package sjournal.web.interceptors;

import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import sjournal.web.annotations.PageTitle;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class MyTitleInterceptor extends HandlerInterceptorAdapter {
    @Override
    public void postHandle(HttpServletRequest request,
                           HttpServletResponse response, Object handler, ModelAndView modelAndView) {
        String title = "SportsJournal!";

        if (modelAndView == null) {
            modelAndView = new ModelAndView();
        } else {
            if (handler instanceof HandlerMethod) {
                PageTitle methodAnnotation = ((HandlerMethod) handler).getMethodAnnotation(PageTitle.class);

                if (methodAnnotation != null) {
                    modelAndView
                            .addObject("title", title + " - " + methodAnnotation.value());
                }
            }
        }
    }

}
