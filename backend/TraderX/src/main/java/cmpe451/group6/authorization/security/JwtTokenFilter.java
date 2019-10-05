package cmpe451.group6.authorization.security;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cmpe451.group6.authorization.exception.CustomException;
import cmpe451.group6.authorization.model.RegistrationStatus;
import cmpe451.group6.authorization.model.User;
import cmpe451.group6.authorization.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.springframework.web.filter.OncePerRequestFilter;

// We should use OncePerRequestFilter since we are doing a database call, there is no point in doing this more than once

public class JwtTokenFilter extends OncePerRequestFilter {

  private boolean verificationEnabled;

  UserRepository userRepository;

  private JwtTokenProvider jwtTokenProvider;

  public JwtTokenFilter(JwtTokenProvider jwtTokenProvider) {
    this.jwtTokenProvider = jwtTokenProvider;
  }

  @Override
  protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {

    // Since filter is out of context of SpringApp, we're injecting the bean lazily here.
    // NOTE: Since this filter is OncePerRequest filter, it might be a burden on the
    // service. However, we need user status here. Otherwise, status check has to be
    // applied for every endpoint.

    if(userRepository==null){
      ServletContext servletContext = httpServletRequest.getServletContext();
      WebApplicationContext webApplicationContext = WebApplicationContextUtils.getWebApplicationContext(servletContext);
      userRepository = webApplicationContext.getBean(UserRepository.class);
    }

    String token = jwtTokenProvider.resolveToken(httpServletRequest);
    try {

      if (token != null && jwtTokenProvider.validateToken(token)) {

        Authentication auth = jwtTokenProvider.getAuthentication(token);

        User user = userRepository.findByUsername(jwtTokenProvider.getUsername(token));

        // Check if user has been activated the account via Email or Google
        if(user.getStatus() == RegistrationStatus.PENDING){
          // account is not validated yet. Return error response.

          // Comment out the next line if you disable verification. (FOR THE EASE OF DEVELOPMENT)
          throw new CustomException("Account is not verified. Check email address.", HttpStatus.UNAUTHORIZED);
        }

        SecurityContextHolder.getContext().setAuthentication(auth);
      }
    } catch (CustomException ex) {
      // It guarantees the user is not authenticated
      SecurityContextHolder.clearContext();
      httpServletResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED, ex.getMessage() + ". Are you trying to reach a" +
              " public endpoint with token?");
      return;
    }

    filterChain.doFilter(httpServletRequest, httpServletResponse);
  }

}
