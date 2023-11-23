package bg.pazar.pazarbg.config;

import bg.pazar.pazarbg.interceptor.LoginInterceptor;
import bg.pazar.pazarbg.interceptor.IndexPageInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfiguration implements WebMvcConfigurer {
    private final IndexPageInterceptor indexPageInterceptor;
    private final LoginInterceptor loginInterceptor;

    public WebConfiguration(IndexPageInterceptor indexPageInterceptor, LoginInterceptor loginInterceptor) {
        this.indexPageInterceptor = indexPageInterceptor;
        this.loginInterceptor = loginInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(loginInterceptor).order(1);
        registry.addInterceptor(indexPageInterceptor).order(2);
    }
}
