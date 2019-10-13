package cmpe451.group6.authorization.service;

import javax.servlet.http.HttpServletRequest;

import cmpe451.group6.authorization.dto.TokenWrapperDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import cmpe451.group6.authorization.exception.CustomException;
import cmpe451.group6.authorization.model.User;
import cmpe451.group6.authorization.repository.UserRepository;
import cmpe451.group6.authorization.security.JwtTokenProvider;

@Service
public class UserService {

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private JwtTokenProvider jwtTokenProvider;

  @Autowired
  private HazelcastService hazelcastService;

  public void deleteUser(String username) {
    User user = userRepository.findByUsername(username);
    if (user == null) {
      throw new CustomException("The user doesn't exist", HttpStatus.UNPROCESSABLE_ENTITY);
    }
    userRepository.deleteByUsername(username);
  }

  public User searchUser(String username) {
    User user = userRepository.findByUsername(username);
    if (user == null) {
      throw new CustomException("The user doesn't exist", HttpStatus.UNPROCESSABLE_ENTITY);
    }
    return user;
  }

  public User whoami(HttpServletRequest req) {
    return userRepository.findByUsername(jwtTokenProvider.getUsername(jwtTokenProvider.resolveToken(req)));
  }

  public TokenWrapperDTO refreshToken(String username, HttpServletRequest req) {
    String currentToken = jwtTokenProvider.resolveToken(req);
    hazelcastService.invalidateToken(currentToken, username);
    return new TokenWrapperDTO(jwtTokenProvider.createToken(username, userRepository.findByUsername(username).getRoles()));
  }

}
