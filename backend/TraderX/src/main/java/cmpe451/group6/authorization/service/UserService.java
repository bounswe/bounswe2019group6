package cmpe451.group6.authorization.service;

import javax.servlet.http.HttpServletRequest;

import cmpe451.group6.authorization.dto.TokenWrapperDTO;
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

  public TokenWrapperDTO signin(String username, String password) {
    try {
      authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
      return new TokenWrapperDTO(jwtTokenProvider.createToken(username, userRepository.findByUsername(username).getRoles()));
    } catch (AuthenticationException e) {
      throw new CustomException("Invalid username/password supplied", HttpStatus.UNPROCESSABLE_ENTITY);
    }
  }


  public TokenWrapperDTO admin_signup(User user){
    if (!userRepository.existsByUsername(user.getUsername())) {
      user.setPassword(passwordEncoder.encode(user.getPassword()));
      userRepository.save(user);
      return new TokenWrapperDTO(jwtTokenProvider.createToken(user.getUsername(), user.getRoles()));
    } else {
      throw new CustomException("Username is already in use", HttpStatus.UNPROCESSABLE_ENTITY);
    }
  }

  public TokenWrapperDTO signup(User user) {
    if (!userRepository.existsByUsername(user.getUsername()) && !userRepository.existsByEmail(user.getEmail())) {
      user.setPassword(passwordEncoder.encode(user.getPassword()));
      Role role = validateIBAN(user.getIBAN());
      user.setRoles(new ArrayList<>(Arrays.asList(role)));
      userRepository.save(user);
      return new TokenWrapperDTO(jwtTokenProvider.createToken(user.getUsername(), user.getRoles()));
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

  public TokenWrapperDTO refresh(String username) {
    return new TokenWrapperDTO(jwtTokenProvider.createToken(username, userRepository.findByUsername(username).getRoles()));
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

}
