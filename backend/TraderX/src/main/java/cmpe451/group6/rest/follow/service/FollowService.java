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

    public String followUser(String username, HttpServletRequest request) {
        User userToFollow = userRepository.findByUsername(username);
        User currentUser = userRepository
                .findByUsername(jwtTokenProvider.getUsername(jwtTokenProvider.resolveToken(request)));

        if (userToFollow == null) {
            throw new CustomException(
                    "There is no user named " + username
                            + ". Please report this issue backend team to handle this exception",
                    HttpStatus.INTERNAL_SERVER_ERROR);
        } else if (userToFollow.getStatus() == RegistrationStatus.PENDING) {
            throw new CustomException("The user is not activate his/her account. Therefore s/he cannot be followed ",
                    HttpStatus.INTERNAL_SERVER_ERROR);
        } else if (followRepository.existsByAndFolloweeUsernameAndFollowerUsername(username,
                jwtTokenProvider.getUsername(jwtTokenProvider.resolveToken(request)))) {
            throw new CustomException("The user has already been followed", HttpStatus.INTERNAL_SERVER_ERROR);
        }
        FollowDAO temp = new FollowDAO();
        temp.setFollower(currentUser);
        temp.setFollowee(userToFollow);
        temp.setFollowStatus(FollowStatus.PENDING);
        temp.setId(3); // TODO: this has to be checked. If I don't give id it does not generate
                       // automatically. However, nevertheless it does not assign handwritten id(3), it
                       // generates.
        followRepository.save(temp);
        System.out.println("actual usernameToFollow: " + username);
        return String.format("%s want to follow %s", currentUser.getUsername(), userToFollow.getUsername());
    }


    /**
     * TODO
     * @param username
     * @param request
     * @return
     */
    public String unfollowUser(String username, HttpServletRequest request){
    
        String currentUsername=jwtTokenProvider.getUsername(jwtTokenProvider.resolveToken(request));
        followRepository.deleteByAndFolloweeUsernameAndFollowerUsername(username, currentUsername);
        return String.format("%s want to unfollow %s", currentUsername, username);
    }

    public List<FolloweeDTO> following(HttpServletRequest request) {
        User currentUser = userRepository
                .findByUsername(jwtTokenProvider.getUsername(jwtTokenProvider.resolveToken(request)));

        List<FolloweeDTO> followeeList = new ArrayList<FolloweeDTO>();
        followRepository.findByAndFollower_username(currentUser.getUsername())
                .forEach(item -> followeeList.add(modelMapper.map(item.getFollowee(), FolloweeDTO.class)));
        return followeeList;
    }

    public List<FolloweeDTO> followers(HttpServletRequest request) {
        User currentUser = userRepository
                .findByUsername(jwtTokenProvider.getUsername(jwtTokenProvider.resolveToken(request)));
        List<FolloweeDTO> followerList = new ArrayList<FolloweeDTO>();
        followRepository.findByAndFollowee_username(currentUser.getUsername())
                .forEach(item -> followerList.add(modelMapper.map(item.getFollowee(), FolloweeDTO.class)));
        return followerList;
    }

    public String following_number(HttpServletRequest request) {
        return (following(request).size() + "");
    }

    public String followers_number(HttpServletRequest request) {
        return (followers(request).size() + "");
    }

    public boolean amIFollowing(String followee_username, HttpServletRequest request) {
        return followRepository.existsByAndFolloweeUsernameAndFollowerUsername(followee_username,
                jwtTokenProvider.getUsername(jwtTokenProvider.resolveToken(request)));
    }

}
