package cmpe451.group6.authorization.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

  @Autowired
  private JwtTokenProvider jwtTokenProvider;

  @Override
  protected void configure(HttpSecurity http) throws Exception {

    http.cors();

    // Disable CSRF (cross site request forgery)
    http.csrf().disable();

    // No session will be created or used by spring security
    http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

    // Entry points
    http.authorizeRequests()//
        .antMatchers("/login").permitAll()//
        .antMatchers("/signup").permitAll()//
        .antMatchers("/password/forgot").permitAll()
        .antMatchers("/password/renew").permitAll()
        .antMatchers("/signup/confirm").permitAll()
        .antMatchers("/h2-console/**/**").permitAll()
        .antMatchers("/trial/public").permitAll()
        .antMatchers("/equipment/**").permitAll()
        .antMatchers("/transaction/equipment/**").permitAll()
        .antMatchers("/transaction/all").permitAll()
        .antMatchers("/transaction/byDate").permitAll()
        .antMatchers("/transaction/count/all").permitAll()
        .antMatchers("/transaction/count/equipment/**").permitAll()
        // Disallow everything else..
        .anyRequest().authenticated();

    // If a user try to access a resource without having enough permissions
    http.exceptionHandling().accessDeniedPage("/login");

    // Apply JWT
    http.apply(new JwtTokenFilterConfigurer(jwtTokenProvider));

  }

  @Override
  public void configure(WebSecurity web) throws Exception {
    // Allow swagger to be accessed without authentication
    web.ignoring().antMatchers("/v2/api-docs")//
        .antMatchers("/swagger-resources/**")//
        .antMatchers("/swagger-ui.html")//
        .antMatchers("/configuration/**")//
        .antMatchers("/webjars/**")//
        .antMatchers("/public")
        .antMatchers("/login")
        .antMatchers("/signup")
        .antMatchers("/password/forgot")
        .antMatchers("/password/renew")
        .antMatchers("/signup/confirm")
        .antMatchers("/h2-console/**/**")
        .antMatchers("/trial/public")
        .antMatchers("/equipment/**")
        .antMatchers("/transaction/equipment/**")
        .antMatchers("/transaction/all")
        .antMatchers("/transaction/byDate")
        .antMatchers("/transaction/count/all")
        .antMatchers("/transaction/count/equipment/**")
        
        // Un-secure H2 Database (for testing purposes, H2 console shouldn't be unprotected in production)
        .and()
        .ignoring()
        .antMatchers("/h2-console/**/**");;
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder(12);
  }

}
