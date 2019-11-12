package cmpe451.group6.authorization.service;

import javax.servlet.http.HttpServletRequest;

import cmpe451.group6.authorization.dto.EditProfileDTO;
import cmpe451.group6.authorization.dto.PrivateProfileDTO;
import cmpe451.group6.authorization.dto.TokenWrapperDTO;
import cmpe451.group6.authorization.dto.UserResponseDTO;
import cmpe451.group6.authorization.model.Role;
import cmpe451.group6.rest.follow.model.FollowStatus;
import cmpe451.group6.rest.follow.service.FollowService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import cmpe451.group6.authorization.exception.CustomException;
import cmpe451.group6.authorization.model.User;
import cmpe451.group6.authorization.repository.UserRepository;
import cmpe451.group6.authorization.security.JwtTokenProvider;

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
  private FollowService followService;

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

  public String editProfile(EditProfileDTO editProfileDTO, HttpServletRequest req){
    User user = userRepository.findByUsername(jwtTokenProvider.getUsername(jwtTokenProvider.resolveToken(req)));
    if(user == null) {
      throw new CustomException("Token is not registered for a valid user",HttpStatus.GONE);
    }

    String newLat = editProfileDTO.getNewLatitude();
    String newLon = editProfileDTO.getNewLongitude();
    String newIBAN = editProfileDTO.getNewIBAN();
    //String newUsername = editProfileDTO.getNewUsername();

    if(newLat != null){
      if(!newLat.matches(User.locationRegex)){
        throw new CustomException("Invalid latitude value: " + newLat, HttpStatus.NOT_ACCEPTABLE);
      }
      user.setLatitude(newLat);
    }
    if(newLon != null){
      if(!newLon.matches(User.locationRegex)){
        throw new CustomException("Invalid longitude value: " + newLon, HttpStatus.NOT_ACCEPTABLE);
      }
      user.setLongitude(newLon);
    }
    if(newIBAN != null){
      if(!newIBAN.matches(User.IBANRegex)){
        throw new CustomException("Invalid IBAN value: " + newIBAN, HttpStatus.NOT_ACCEPTABLE);
      }
      if(!(user.getRoles().get(0) == Role.ROLE_TRADER)){
        throw new CustomException("To set an IBAN, be a trader user first", HttpStatus.NOT_ACCEPTABLE);
      }
      user.setIBAN(newIBAN);
    }

    //Changing username property is postponed for now.

    /*if(newUsername != null){
      if(!newUsername.matches(User.usernameRegex)){
        throw new CustomException("Invalid username: " + newUsername, HttpStatus.NOT_ACCEPTABLE);
      }
      if(userRepository.findByUsername(newUsername)!=null){
        throw new CustomException("This username is already in use: " + newUsername, HttpStatus.NOT_ACCEPTABLE);
      }
      // TODO : Tokens are saved for the previous username.
      user.setUsername(newUsername);
    }*/

    userRepository.save(user);
    return "Changes has been saved.";
  }

  public List<Object> getAll(HttpServletRequest req){
    String clientUsername = unwrapUsername(req);
    List<Object> userList = userRepository.getUsersByIdLessThan(20);
    ListIterator iterator = userList.listIterator();
    while(iterator.hasNext()) {
      User usr = (User) iterator.next();
      FollowStatus tmpStatus = followService.getFollowStatus(clientUsername,usr.getUsername());
      UserResponseDTO.FollowingStatus status = convertStatus(tmpStatus);
      if(usr.getIsPrivate() && status != UserResponseDTO.FollowingStatus.FOLLOWING){
        PrivateProfileDTO dto = modelMapper.map(usr, PrivateProfileDTO.class);
        dto.setFollowingStatus(status);
        iterator.set(dto);
      } else { //public or followed private
        UserResponseDTO dto = modelMapper.map(usr, UserResponseDTO.class);
        dto.setFollowingStatus(status);
        iterator.set(dto);
      }
    }
    return userList;
  }

  public String unwrapUsername(HttpServletRequest req){
    return jwtTokenProvider.getUsername(jwtTokenProvider.resolveToken(req));
  }

  public UserResponseDTO.FollowingStatus convertStatus(FollowStatus status){
    UserResponseDTO.FollowingStatus statusDTO;
    if(status == null){ // not following
      statusDTO = UserResponseDTO.FollowingStatus.NOT_FOLLOWING;
    } else if(status == FollowStatus.PENDING){
      statusDTO = UserResponseDTO.FollowingStatus.PENDING;
    } else { // following
      statusDTO = UserResponseDTO.FollowingStatus.FOLLOWING;
    }
    return statusDTO;
  }

}
