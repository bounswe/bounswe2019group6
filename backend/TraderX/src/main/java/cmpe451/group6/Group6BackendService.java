package cmpe451.group6;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import cmpe451.group6.authorization.model.RegistrationStatus;
import cmpe451.group6.authorization.repository.UserRepository;
import cmpe451.group6.authorization.service.SignupService;
import cmpe451.group6.rest.equipment.service.EquipmentUpdateService;
import cmpe451.group6.rest.event.service.EventService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import cmpe451.group6.authorization.model.Role;
import cmpe451.group6.authorization.model.User;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@SpringBootApplication
@EnableScheduling
@EnableAsync
public class Group6BackendService implements CommandLineRunner {

  @Autowired
  SignupService signupService;

  @Autowired
  EquipmentUpdateService equipmentUpdateService;

  @Autowired
  EventService eventService;

  @Autowired
  UserRepository userRepository;

  @Value("${security.admin-un}")
  String adminUsername;

  @Value("${security.admin-pw}")
  String adminPassword;

  public static void main(String[] args) {
    SpringApplication.run(Group6BackendService.class, args);
  }

  @Bean(name="threadPoolTaskExecutor")
  public TaskExecutor threadPoolTaskExecutor() {
    ThreadPoolTaskExecutor threadPoolTaskExecutor = new ThreadPoolTaskExecutor();
    threadPoolTaskExecutor.setThreadNamePrefix("Async-");
    threadPoolTaskExecutor.setCorePoolSize(3);
    threadPoolTaskExecutor.setMaxPoolSize(3);
    threadPoolTaskExecutor.afterPropertiesSet();
    return threadPoolTaskExecutor;
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

  // Predefined users
  @Override
  public void run(String... params) throws Exception {

    if (!userRepository.existsByUsername(adminUsername)) {
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
    }

    equipmentUpdateService.initializeEquipments();
    eventService.initializeIfRequired();

  }

}
