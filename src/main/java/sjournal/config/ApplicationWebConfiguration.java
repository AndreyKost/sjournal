package sjournal.config;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import sjournal.web.interceptors.FaviconInterceptor;
import sjournal.web.interceptors.MyTitleInterceptor;

@Configuration
public class ApplicationWebConfiguration implements WebMvcConfigurer {


   private final MyTitleInterceptor myTitleInterceptor;
    private final FaviconInterceptor faviconInterceptor;


    @Autowired
    public ApplicationWebConfiguration(MyTitleInterceptor myTitleInterceptor, FaviconInterceptor faviconInterceptor) {
        this.myTitleInterceptor = myTitleInterceptor;
        this.faviconInterceptor = faviconInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(this.myTitleInterceptor);
        registry.addInterceptor(this.faviconInterceptor);
    }
}
