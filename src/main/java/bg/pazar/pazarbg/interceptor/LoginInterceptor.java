package bg.pazar.pazarbg.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.util.UrlPathHelper;

@Component
public class LoginInterceptor implements HandlerInterceptor {
    final UrlPathHelper urlPathHelper = new UrlPathHelper();

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        if ( "/login".equals(urlPathHelper.getLookupPathForRequest(request))){

            String encodedRedirectURL = response.encodeRedirectURL("/users/login");
            response.setStatus(HttpServletResponse.SC_FOUND);
            response.setHeader("Location", encodedRedirectURL);
            return false;
        } else{
            return true;
        }
    }
}
