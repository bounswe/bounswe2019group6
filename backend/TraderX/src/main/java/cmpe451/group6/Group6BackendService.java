package cmpe451.group6;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import cmpe451.group6.authorization.model.RegistrationStatus;
import cmpe451.group6.authorization.repository.UserRepository;
import cmpe451.group6.authorization.service.HazelcastService;
import cmpe451.group6.authorization.service.SignupService;
import cmpe451.group6.rest.comment.service.EquipmentCommentService;
import cmpe451.group6.rest.equipment.service.EquipmentUpdateService;
import cmpe451.group6.rest.follow.service.FollowService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import cmpe451.group6.authorization.model.Role;
import cmpe451.group6.authorization.model.User;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

// TODO: Interface for user to supply new password when resent link is sent. (Frontend related.)

@SpringBootApplication
@EnableScheduling
public class Group6BackendService implements CommandLineRunner {

  @Autowired
  SignupService signupService;

  @Autowired
  HazelcastService hazelcastService;

  @Autowired
  UserRepository userRepository;

  @Autowired
  EquipmentUpdateService equipmentUpdateService;

  @Autowired
  FollowService followService;

  @Autowired
  EquipmentCommentService commentService;

  @Value("${security.admin-un}")
  String adminUsername;

  @Value("${security.admin-pw}")
  String adminPassword;

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
    config.setAllowedHeaders(Arrays.asList("*"));
    config.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "OPTIONS", "DELETE", "PATCH"));
    source.registerCorsConfiguration("/**", config);
    return new CorsFilter(source);
  }

  // Predefined admin, trader and basic users
  @Override
  public void run(String... params) throws Exception {

    User admin = new User();
    admin.setUsername(adminUsername);
    admin.setPassword(adminPassword);
    admin.setEmail("admin@email.com");
    admin.setLatitude("6");
    admin.setLongitude("6");
    admin.setRegistrationStatus(RegistrationStatus.ENABLED);
    admin.setRoles(new ArrayList<Role>(Arrays.asList(Role.ROLE_ADMIN)));
    admin.setIsPrivate(true);
    signupService.internal_signup(admin);

    User trader = new User();
    trader.setUsername("trader");
    trader.setPassword("trader");
    trader.setEmail("trader@email.com");
    trader.setLatitude("46.123");
    trader.setIBAN("TR123456789012345678");
    trader.setLongitude("46.123");
    trader.setRegistrationStatus(RegistrationStatus.ENABLED);
    trader.setRoles(new ArrayList<Role>(Arrays.asList(Role.ROLE_TRADER)));
    trader.setIsPrivate(true);
    signupService.internal_signup(trader);

    User basic = new User();
    basic.setUsername("basic");
    basic.setPassword("basic");
    basic.setEmail("basic@email.com");
    basic.setLatitude("46.123");
    basic.setLongitude("46.123");
    basic.setRegistrationStatus(RegistrationStatus.ENABLED);
    basic.setRoles(new ArrayList<Role>(Arrays.asList(Role.ROLE_BASIC)));
    basic.setIsPrivate(false);
    signupService.internal_signup(basic);

    followService.followUser("basic",adminUsername);
    followService.followUser("trader",adminUsername);
    followService.answerRequest("trader",adminUsername,true);
    followService.followUser("trader","basic");
    followService.answerRequest("trader","basic",true);
    followService.followUser("basic","trader");

    equipmentUpdateService.initializeEquipments();

    commentService.postEquipmentComment("trader","trader comment","USD");
    commentService.postEquipmentComment("basic","basic comment","USD");

  }

}
