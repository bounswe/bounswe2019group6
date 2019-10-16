package cmpe451.group6;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import cmpe451.group6.authorization.model.RegistrationStatus;
import cmpe451.group6.authorization.repository.UserRepository;
import cmpe451.group6.authorization.service.HazelcastService;
import cmpe451.group6.authorization.service.SignupService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import cmpe451.group6.authorization.model.Role;
import cmpe451.group6.authorization.model.User;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

// TODO: Interface for user to supply new password when resent link is sent. (Frontend related.)

@SpringBootApplication
public class Group6BackendService implements CommandLineRunner {

  @Autowired
  SignupService signupService;

  @Autowired
  HazelcastService hazelcastService;

  @Autowired
  UserRepository userRepository;

  public static void main(String[] args) {
    SpringApplication.run(Group6BackendService.class, args);
  }

  @Bean
  public ModelMapper modelMapper() {
    return new ModelMapper();
  }

  @Bean
  public CorsFilter corsFilter() {
    final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    final CorsConfiguration config = new CorsConfiguration();
    config.setAllowCredentials(true);
    config.setAllowedOrigins(Collections.singletonList("*"));
    config.setAllowedHeaders(Arrays.asList("Origin", "Content-Type", "Accept"));
    config.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "OPTIONS", "DELETE", "PATCH"));
    source.registerCorsConfiguration("/**", config);
    return new CorsFilter(source);
  }

  // Predefined admin user with full privileges
  @Override
  public void run(String... params) throws Exception {
    if(!userRepository.existsByUsername("admin")) {
      User admin = new User();
      admin.setUsername("admin");
      admin.setPassword("admin");
      admin.setEmail("admin@email.com");
      admin.setLatitude("46.123");
      admin.setLongitude("46.123");
      admin.setStatus(RegistrationStatus.ENABLED);
      admin.setRoles(new ArrayList<Role>(Arrays.asList(Role.ROLE_ADMIN)));

      String token = signupService.admin_signup(admin);


      hazelcastService.invalidateToken(token, "admin");
    }

  }

}
