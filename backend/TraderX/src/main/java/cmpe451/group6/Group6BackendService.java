package cmpe451.group6;

import java.util.ArrayList;
import java.util.Arrays;

import cmpe451.group6.authorization.model.RegistrationStatus;
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

// TODO: Interface for user to supply new password when resent link is sent. (Frontend related.)

@SpringBootApplication
public class Group6BackendService implements CommandLineRunner {

  @Autowired
  SignupService signupService;

  @Autowired
  HazelcastService hazelcastService;

  public static void main(String[] args) {
    SpringApplication.run(Group6BackendService.class, args);
  }

  @Bean
  public ModelMapper modelMapper() {
    return new ModelMapper();
  }

  // Predefined admin user with full privileges
  @Override
  public void run(String... params) throws Exception {
    User admin = new User();
    admin.setUsername("admin");
    admin.setPassword("admin");
    admin.setEmail("admin@email.com");
    admin.setLatitude("46.123");
    admin.setLongitude("46.123");
    admin.setStatus(RegistrationStatus.ENABLED);
    admin.setRoles(new ArrayList<Role>(Arrays.asList(Role.ROLE_ADMIN)));

    String token = signupService.admin_signup(admin);

    hazelcastService.invalidateToken(token,"admin");

  }

}