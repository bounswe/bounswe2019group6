package cmpe451.group6;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Random;

import cmpe451.group6.authorization.model.RegistrationStatus;
import cmpe451.group6.authorization.repository.UserRepository;
import cmpe451.group6.authorization.service.SignupService;
import cmpe451.group6.rest.alert.dto.AlertDTO;
import cmpe451.group6.rest.alert.service.AlertService;
import cmpe451.group6.rest.equipment.service.EquipmentUpdateService;
import cmpe451.group6.rest.event.service.EventService;
import cmpe451.group6.rest.follow.service.FollowService;
import cmpe451.group6.rest.predict.dto.PredictionDTO;
import cmpe451.group6.rest.predict.model.Prediction;
import cmpe451.group6.rest.predict.service.PredictionService;
import com.github.javafaker.Faker;
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
  FollowService followService;

  @Autowired
  PredictionService predictionService;

  @Autowired
  AlertService alertService;

  @Autowired
  UserRepository userRepository;

  @Value("${security.admin-un}")
  String adminUsername;

  @Value("${security.admin-pw}")
  String adminPassword;

  @Value("${mock.demo-user1}")
  String demoUser1;

  @Value("${mock.demo-user2}")
  String demoUser2;

  @Value("${mock.demo-user3}")
  String demoUser3;

  @Value("${mock.password}")
  String mockPassword;

  @Value("${mock.initialize}")
  boolean initialize;

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

    if (initialize) {
      try {
        initializeMock();
      } catch (Exception ignored) {
        // no-op
      }
    }
    equipmentUpdateService.initializeEquipments();
    eventService.initializeIfRequired();

  }

  private void initializeMock(){
    Faker faker = new Faker();
    Random rand = new Random();

    User demo1 = new User();
    demo1.setUsername(demoUser1);
    demo1.setPassword(mockPassword);
    demo1.setEmail(demoUser1 + "@mail.com");
    demo1.setLatitude(faker.address().latitude());
    demo1.setLongitude(faker.address().longitude());
    demo1.setRegistrationStatus(RegistrationStatus.ENABLED);
    demo1.setRoles(new ArrayList<Role>(Arrays.asList(Role.ROLE_TRADER)));
    demo1.setIBAN("TR543456213657432222");
    demo1.setIsPrivate(false);
    signupService.internal_signup(demo1);

    User demo2 = new User();
    demo2.setUsername(demoUser2);
    demo2.setPassword(mockPassword);
    demo2.setEmail(demoUser2 + "@mail.com");
    demo2.setLatitude(faker.address().latitude());
    demo2.setLongitude(faker.address().longitude());
    demo2.setRegistrationStatus(RegistrationStatus.ENABLED);
    demo2.setRoles(new ArrayList<Role>(Arrays.asList(Role.ROLE_BASIC)));
    demo2.setIsPrivate(false);
    signupService.internal_signup(demo2);

    User demo3 = new User();
    demo3.setUsername(demoUser3);
    demo3.setPassword(mockPassword);
    demo3.setEmail(demoUser3 + "@mail.com");
    demo3.setLatitude(faker.address().latitude());
    demo3.setLongitude(faker.address().longitude());
    demo3.setRegistrationStatus(RegistrationStatus.ENABLED);
    demo3.setRoles(new ArrayList<Role>(Arrays.asList(Role.ROLE_BASIC)));
    demo3.setIsPrivate(false);
    signupService.internal_signup(demo3);

    for (int i = 0; i < 80; i++) {
      String name = faker.name().username().replace('.','_');
      User mock = new User();
      mock.setUsername(name);
      mock.setPassword(mockPassword);
      mock.setEmail(name + "@mail.com");
      mock.setLatitude(faker.address().latitude());
      mock.setLongitude(faker.address().longitude());
      mock.setRegistrationStatus(RegistrationStatus.ENABLED);
      if (rand.nextBoolean()){
        mock.setRoles(new ArrayList<Role>(Arrays.asList(Role.ROLE_BASIC)));
      } else {
        mock.setRoles(new ArrayList<Role>(Arrays.asList(Role.ROLE_TRADER)));
        mock.setIBAN("TR223147213634232213");
      }
      mock.setIsPrivate(false);

      try {
        signupService.internal_signup(mock);
        followService.followUser(demoUser1,name);
        if (rand.nextBoolean()) followService.followUser(demoUser2,name);
        if (rand.nextBoolean()) followService.followUser(name,demoUser1);
        if (rand.nextBoolean()) followService.followUser(name,demoUser2);
      } catch (Exception ignored) {
        // no-op
      }
    }

    predictionService.createNewPrediction(demoUser1,new PredictionDTO("EUR", Prediction.PredictionType.INCREASE));
    predictionService.createNewPrediction(demoUser1,new PredictionDTO("CZK", Prediction.PredictionType.INCREASE));
    predictionService.createNewPrediction(demoUser1,new PredictionDTO("JPY", Prediction.PredictionType.DECREASE));
    predictionService.createNewPrediction(demoUser2,new PredictionDTO("TRY", Prediction.PredictionType.INCREASE));
    predictionService.createNewPrediction(demoUser2,new PredictionDTO("JPY", Prediction.PredictionType.DECREASE));
    predictionService.createNewPrediction(demoUser2,new PredictionDTO("GBP", Prediction.PredictionType.DECREASE));
  }

}
