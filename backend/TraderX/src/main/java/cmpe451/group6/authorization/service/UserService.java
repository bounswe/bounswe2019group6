package cmpe451.group6.authorization.service;

import javax.servlet.http.HttpServletRequest;

import cmpe451.group6.authorization.dto.PrivateProfileDTO;
import cmpe451.group6.authorization.dto.TokenWrapperDTO;
import cmpe451.group6.authorization.dto.UserResponseDTO;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import cmpe451.group6.authorization.exception.CustomException;
import cmpe451.group6.authorization.model.User;
import cmpe451.group6.authorization.repository.UserRepository;
import cmpe451.group6.authorization.security.JwtTokenProvider;

import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

@Service
public class UserService {

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private JwtTokenProvider jwtTokenProvider;

  @Autowired
  private HazelcastService hazelcastService;

  @Autowired
  private UserService userService;

  @Autowired
  private ModelMapper modelMapper;

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

  public String setPrivate(HttpServletRequest req){
    User user = userRepository.findByUsername(jwtTokenProvider.getUsername(jwtTokenProvider.resolveToken(req)));
    user.setIsPrivate(true);
    userRepository.save(user);
    return "Profile has been set as private";
  }

  public String setPublic(HttpServletRequest req){
    User user = userRepository.findByUsername(jwtTokenProvider.getUsername(jwtTokenProvider.resolveToken(req)));
    user.setIsPrivate(false);
    userRepository.save(user);
    return "Profile has been set as public";
  }

  public List<Object> getAll(){
    List<Object> userList = userRepository.getUsersByIdLessThan(20);
    ListIterator iterator = userList.listIterator();
    while(iterator.hasNext()) {
      User usr = (User) iterator.next();
      iterator.set(modelMapper.map(usr, usr.getIsPrivate() ? PrivateProfileDTO.class : UserResponseDTO.class));
    }
    return userList;
  }
}
