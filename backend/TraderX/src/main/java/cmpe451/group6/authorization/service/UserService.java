package cmpe451.group6.authorization.service;

import javax.servlet.http.HttpServletRequest;

import cmpe451.group6.authorization.dto.EditProfileDTO;
import cmpe451.group6.authorization.dto.PrivateProfileDTO;
import cmpe451.group6.authorization.dto.TokenWrapperDTO;
import cmpe451.group6.authorization.dto.UserResponseDTO;
import cmpe451.group6.authorization.model.Role;
import cmpe451.group6.rest.comment.service.EquipmentCommentService;
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

  @Autowired
  private EquipmentCommentService equipmentCommentService;

  public void deleteUser(String username) {
    User user = userRepository.findByUsername(username);
    if (user == null) {
      throw new CustomException("The user doesn't exist", HttpStatus.UNPROCESSABLE_ENTITY);
    }
    userRepository.deleteByUsername(username);
  }

  public TokenWrapperDTO refreshToken(String username, HttpServletRequest req) {
    String currentToken = jwtTokenProvider.resolveToken(req);
    hazelcastService.invalidateToken(currentToken, username);
    return new TokenWrapperDTO(jwtTokenProvider.createToken(username, userRepository.findByUsername(username).getRoles()));
  }

  public String setPrivate(String username){
    User user = userRepository.findByUsername(username);
    user.setIsPrivate(true);
    userRepository.save(user);
    return "Profile has been set as private";
  }

  public String setPublic(String username){
    User user = userRepository.findByUsername(username);
    user.setIsPrivate(false);
    userRepository.save(user);
    return "Profile has been set as public";
  }

  public String editProfile(EditProfileDTO editProfileDTO, String username){
    User user = userRepository.findByUsername(username);
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
      // TODO : [CRITICAL] Tokens are saved for the previous username and have to be updated !!
      user.setUsername(newUsername);
    }*/

    userRepository.save(user);
    return "Changes has been saved.";
  }

  public List<Object> getAll(String senderUsername, int limit){
    List<Object> userList = userRepository.getUsersByIdLessThan(limit);
    ListIterator iterator = userList.listIterator();
    while(iterator.hasNext()) {
      User usr = (User) iterator.next();
      if (usr.getUsername().equals(senderUsername)) {
        // ignore self
        iterator.remove();
        continue;
      }
      iterator.set(getUserProfile(usr.getUsername(),senderUsername));
    }
    return userList;
  }

  public Object getUserProfile(String profileOwner, String requestSender){
      User owner = userRepository.findByUsername(profileOwner);

      if(owner == null) throw new CustomException("No such a user is found",HttpStatus.NOT_ACCEPTABLE);
      if(profileOwner.equals(requestSender)) return getDetailedProfile(owner,null);

      FollowStatus status = followService.getFollowStatus(profileOwner,requestSender);
      if(owner.getIsPrivate() && status != FollowStatus.APPROVED){
        return getRestrictedProfile(owner,status);
      } else {
        return getDetailedProfile(owner,status);
      }
  }

  private UserResponseDTO getDetailedProfile(User profileOwner, FollowStatus status){
    UserResponseDTO response = modelMapper.map(profileOwner, UserResponseDTO.class);
    response.setFollowersCount(followService.getFollowersCount(profileOwner.getUsername()));
    response.setFollowingsCount(followService.getFollowingsCount(profileOwner.getUsername()));
    response.setFollowingStatus(convertStatus(status));
    response.setArticlesCount(0); // not active yet
    response.setCommentsCount(equipmentCommentService.getCommentsCount(profileOwner.getUsername()));
    return response;
  }

  private PrivateProfileDTO getRestrictedProfile(User profileOwner, FollowStatus status){
    PrivateProfileDTO response = modelMapper.map(profileOwner, PrivateProfileDTO.class);
    response.setFollowingStatus(convertStatus(status));
    response.setFollowersCount(followService.getFollowersCount(profileOwner.getUsername()));
    response.setFollowingsCount(followService.getFollowingsCount(profileOwner.getUsername()));
    return response;
  }

  private UserResponseDTO.FollowingStatus convertStatus(FollowStatus status){
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
