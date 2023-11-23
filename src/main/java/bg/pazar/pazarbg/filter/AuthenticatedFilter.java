package bg.pazar.pazarbg.filter;

import bg.pazar.pazarbg.service.impl.AuthenticationService;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;

import java.io.IOException;


@Component
public class AuthenticatedFilter implements Filter {
    private final AuthenticationService authenticationService;

    public AuthenticatedFilter(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest servletRequest = (HttpServletRequest) request;
        HttpServletResponse servletResponse = (HttpServletResponse) response;

        String requestURI = servletRequest.getRequestURI();

        if ((requestURI.equals("/users/login") || requestURI.equals("/users/register")) && authenticationService.isAuthenticated()) {

            String encodedRedirectURL = ((HttpServletResponse) response).encodeRedirectURL("/home");

            servletResponse.setStatus(HttpServletResponse.SC_FOUND);
            servletResponse.setHeader("Location", encodedRedirectURL);
        }

        chain.doFilter(servletRequest, servletResponse);
    }
}
