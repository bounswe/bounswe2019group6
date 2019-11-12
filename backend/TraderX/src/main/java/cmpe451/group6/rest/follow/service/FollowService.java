package cmpe451.group6.rest.follow.service;

import cmpe451.group6.authorization.exception.CustomException;
import cmpe451.group6.authorization.model.RegistrationStatus;
import cmpe451.group6.authorization.model.User;
import cmpe451.group6.authorization.repository.UserRepository;
import cmpe451.group6.authorization.security.JwtTokenProvider;
import cmpe451.group6.rest.follow.DTO.FolloweeDTO;
import cmpe451.group6.rest.follow.model.FollowDAO;
import cmpe451.group6.rest.follow.model.FollowStatus;
import cmpe451.group6.rest.follow.repository.FollowRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

@Service
public class FollowService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private FollowRepository followRepository;

    @Autowired
    private ModelMapper modelMapper;

    /**
     * 
     * @param username
     * @param request
     * @return info about state
     */
    public String followUser(String username, HttpServletRequest request) {
        User userToFollow = userRepository.findByUsername(username);
        User currentUser = userRepository
                .findByUsername(jwtTokenProvider.getUsername(jwtTokenProvider.resolveToken(request)));

        if (userToFollow == null) {
            throw new CustomException("There is no user named " + username + ".", HttpStatus.NOT_ACCEPTABLE);
        } else if (userToFollow.getRegistrationStatus() == RegistrationStatus.PENDING) {
            throw new CustomException("The user is not activate his/her account. Therefore s/he cannot be followed ",
                    HttpStatus.PRECONDITION_FAILED);
        } else if (amIFollowing(username, request)) {
            throw new CustomException("The user has already been followed", HttpStatus.PRECONDITION_FAILED);
        }

        FollowDAO temp = new FollowDAO();
        temp.setFollower(currentUser);
        temp.setFollowee(userToFollow);

        String followStatus ;
        if (userToFollow.getIsPrivate()) {
            followStatus = " wants to follow ";
            temp.setFollowStatus(FollowStatus.PENDING);
        } else {
            followStatus = " is now following ";
            temp.setFollowStatus(FollowStatus.APPROVED);
        }

        followRepository.save(temp);
        return (currentUser.getUsername() + followStatus + userToFollow.getUsername());        
        
    }

    /**
     * 
     * @param username
     * @param request
     * @return info about state
     */
    public String unfollowUser(String followee_username, HttpServletRequest request) {

        User userToUnfollow = userRepository.findByUsername(followee_username);
        String currentUsername = jwtTokenProvider.getUsername(jwtTokenProvider.resolveToken(request));

        if (userToUnfollow == null) {
            throw new CustomException("There is no user named " + followee_username + ". Thus, can not be unfollowed",
                    HttpStatus.NOT_ACCEPTABLE);
        } else if (!amIFollowing(followee_username, request)) {
            throw new CustomException(
                    String.format("%s is not following %s already", currentUsername, followee_username),
                    HttpStatus.PRECONDITION_FAILED);
        } else {
            followRepository.deleteByAndFolloweeUsernameAndFollowerUsername(followee_username, currentUsername);
            return String.format("%s succesfully unfollowed %s", currentUsername, followee_username);
        }
    }

    /**
     * 
     * @param request
     * @return followeeList of the currentuser
     */
    public List<FolloweeDTO> following(HttpServletRequest request) {
        String currentUsername = jwtTokenProvider.getUsername(jwtTokenProvider.resolveToken(request));
        User currentUser = userRepository.findByUsername(currentUsername);
        if (currentUser == null) {
            throw new CustomException("There is no user named " + currentUsername + ". ", HttpStatus.NOT_ACCEPTABLE);
        } else {
            List<FolloweeDTO> followeeList = new ArrayList<FolloweeDTO>();
            followRepository.findByFollower_username(currentUser.getUsername())
                    .forEach(item -> followeeList.add(modelMapper.map(item.getFollowee(), FolloweeDTO.class)));
            return followeeList;
        }

    }

    /**
     * 
     * @param request
     * @return followerList of the currentuser
     */
    public List<FolloweeDTO> followers(HttpServletRequest request) {
        String currentUsername = jwtTokenProvider.getUsername(jwtTokenProvider.resolveToken(request));
        User currentUser = userRepository.findByUsername(currentUsername);
        if (currentUser == null) {
            throw new CustomException("There is no user named " + currentUsername + ". ", HttpStatus.NOT_ACCEPTABLE);
        } else {
            List<FolloweeDTO> followerList = new ArrayList<FolloweeDTO>();
            followRepository.findByFollowee_username(currentUser.getUsername())
                    .forEach(item -> followerList.add(modelMapper.map(item.getFollowee(), FolloweeDTO.class)));
            return followerList;
        }

    }

    /**
     * 
     * @param request
     * @return number of users that current user follows
     */
    public String following_number(HttpServletRequest request) {
        return (following(request).size() + "");
    }

    /**
     * 
     * @param request
     * @return number of users that follows current user
     */
    public String followee_number(HttpServletRequest request) {
        return (followers(request).size() + "");
    }

    /**
     * 
     * @param followee_username
     * @param request
     * @return boolean, true if current user follows the given user
     */
    public boolean amIFollowing(String followee_username, HttpServletRequest request) {

        User userToUnfollow = userRepository.findByUsername(followee_username);

        if (userToUnfollow == null) {
            throw new CustomException("There is no user named " + followee_username + ". Thus, can not be unfollowed",
                    HttpStatus.NOT_ACCEPTABLE);
        } else {
            return followRepository.existsByAndFolloweeUsernameAndFollowerUsername(followee_username,
                    jwtTokenProvider.getUsername(jwtTokenProvider.resolveToken(request)));
        }

    }

    public FollowStatus getFollowStatus(String user, String follower){
        FollowDAO dao = followRepository.findByFolloweeUsernameAndFollowerUsername(follower,user);
        if(dao == null) return null;
        return dao.getFollowStatus();
    }

}
