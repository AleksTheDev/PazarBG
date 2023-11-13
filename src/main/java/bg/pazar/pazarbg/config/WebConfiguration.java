package bg.pazar.pazarbg.config;

import bg.pazar.pazarbg.interceptor.IndexPageInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfiguration implements WebMvcConfigurer {
    private final IndexPageInterceptor indexPageInterceptor;

    public WebConfiguration(IndexPageInterceptor indexPageInterceptor) {
        this.indexPageInterceptor = indexPageInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(indexPageInterceptor);
    }
}
