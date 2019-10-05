package cmpe451.group6.authorization.service;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;

import cmpe451.group6.authorization.email.EmailService;
import cmpe451.group6.authorization.model.RegistrationStatus;
import cmpe451.group6.authorization.model.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import cmpe451.group6.authorization.exception.CustomException;
import cmpe451.group6.authorization.model.User;
import cmpe451.group6.authorization.repository.UserRepository;
import cmpe451.group6.authorization.security.JwtTokenProvider;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

@Service
public class UserService {

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private PasswordEncoder passwordEncoder;

  @Autowired
  private JwtTokenProvider jwtTokenProvider;

  @Autowired
  private AuthenticationManager authenticationManager;

  @Autowired
  private EmailService emailService;

  public String confirmUser(String token){
    try {
      String username = jwtTokenProvider.getUsername(token);
      User user = userRepository.findByUsername(username);
      user.setStatus(RegistrationStatus.ENABLED);
      userRepository.save(user);
      return "Confirmation completed";
    } catch (AuthenticationException e) {
      throw new CustomException("Invalid TOKEN", HttpStatus.UNPROCESSABLE_ENTITY);
    }
  }

  public String signin(String username, String password) {
    try {
      authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
      return jwtTokenProvider.createToken(username, userRepository.findByUsername(username).getRoles());
    } catch (AuthenticationException e) {
      throw new CustomException("Invalid username/password supplied", HttpStatus.UNPROCESSABLE_ENTITY);
    }
  }


  public String admin_signup(User user){
    if (!userRepository.existsByUsername(user.getUsername())) {
      user.setPassword(passwordEncoder.encode(user.getPassword()));
      userRepository.save(user);
      return jwtTokenProvider.createToken(user.getUsername(), user.getRoles());
    } else {
      throw new CustomException("Username is already in use", HttpStatus.UNPROCESSABLE_ENTITY);
    }
  }



  public String sendPasswordRenewalMail(String mail){
    User user = userRepository.findByEmail(mail);
    if(user != null){
      String token = jwtTokenProvider.createToken(user.getUsername(), user.getRoles());
      try {
        emailService.sendmail(user.getEmail(), "Forgot Password", "xxx", buildPasswordRenewalURL(token), null);
      } catch (MessagingException | IOException e) {
        e.printStackTrace();
        throw new CustomException("Failed to send verification email", HttpStatus.INTERNAL_SERVER_ERROR);
      }
      return "Link to reset password has been sent to your email.";

    } else {
      throw new CustomException("No user found for the email", HttpStatus.BAD_REQUEST);
    }
  }


  // TODO : Invalidate the token after the password hes been changed.
  public String renewPassword(String token, String newPassword){
    try {
      String username = jwtTokenProvider.getUsername(token);
      User user = userRepository.findByUsername(username);
      user.setPassword(passwordEncoder.encode(newPassword));
      userRepository.save(user);
      return "Password has been changed.";
    } catch (AuthenticationException e) {
      throw new CustomException("Invalid TOKEN", HttpStatus.UNPROCESSABLE_ENTITY);
    }
  }

  public String signup(User user) {
    if (!userRepository.existsByUsername(user.getUsername()) && !userRepository.existsByEmail(user.getEmail())) {
      user.setPassword(passwordEncoder.encode(user.getPassword()));
      Role role = validateIBAN(user.getIBAN());
      user.setRoles(new ArrayList<>(Arrays.asList(role)));
      user.setStatus(RegistrationStatus.VERIFICATION_SENT);
      String token = jwtTokenProvider.createToken(user.getUsername(), user.getRoles());

      try {
        emailService.sendmail(user.getEmail(), "verification", "xxx", buildVerificationURL(token), null);
      } catch (MessagingException | IOException e) {
        e.printStackTrace();
        throw new CustomException("Failed to send verification email", HttpStatus.INTERNAL_SERVER_ERROR);
      }

      userRepository.save(user);
      return "Confirmation Email is sent. Success";
    } else {
      throw new CustomException("Username or email is already in use", HttpStatus.UNPROCESSABLE_ENTITY);
    }
  }

  public void delete(String username) {
    userRepository.deleteByUsername(username);
  }

  public User search(String username) {
    User user = userRepository.findByUsername(username);
    if (user == null) {
      throw new CustomException("The user doesn't exist", HttpStatus.NOT_FOUND);
    }
    return user;
  }

  public User whoami(HttpServletRequest req) {
    return userRepository.findByUsername(jwtTokenProvider.getUsername(jwtTokenProvider.resolveToken(req)));
  }

  public String refresh(String username) {
    return jwtTokenProvider.createToken(username, userRepository.findByUsername(username).getRoles());
  }



  /**
   * If an IBAN number is provided by client, then check if it's valid. If so, set it's role as TRADER.
   * else, throw exception. If it's not provided, than the user wants to be a BASIC_USER.
   * @param IBAN IBAN provided
   * @return Desired and validted Role for the user
   */
  private Role validateIBAN(String IBAN) throws  CustomException{
    if(IBAN == null){ return Role.ROLE_BASIC; }
    if(IBAN.matches("^[A-Z]{2}[0-9]{18}$")){ return Role.ROLE_TRADER; }

    // IBAN is provided but not valid
    throw new CustomException("Invalid IBAN number. Must match: ^[A-Z]{2}[0-9]{18}$", HttpStatus.UNPROCESSABLE_ENTITY);
  }

  private String buildVerificationURL(String token){
    return "http://localhost:8080/users/confirmation?token=" + token;
  }

  private String buildPasswordRenewalURL(String token){
    return "http://localhost:8080/users/renewpassword?token=" + token;
  }

}
