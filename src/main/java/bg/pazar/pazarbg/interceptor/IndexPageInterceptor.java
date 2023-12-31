package bg.pazar.pazarbg.interceptor;

import bg.pazar.pazarbg.service.impl.AuthenticationService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.util.UrlPathHelper;

@Component
public class IndexPageInterceptor implements HandlerInterceptor {
    private final AuthenticationService authenticationService;
    final UrlPathHelper urlPathHelper = new UrlPathHelper();

    public IndexPageInterceptor(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    //Redirect logged-in users from the index page to the homepage
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        //Get requested url
        String requestPath = urlPathHelper.getLookupPathForRequest(request);

        //If requested url matches / and user is logged-in
        if (requestPath.equals("/") && authenticationService.isAuthenticated()) {
            //Redirect to the homepage
            String encodedRedirectURL = response.encodeRedirectURL("/home");
            response.setStatus(HttpServletResponse.SC_FOUND);
            response.setHeader("Location", encodedRedirectURL);
            return false;
        } else {
            return true;
        }
    }
}
