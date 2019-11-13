package cmpe451.group6.rest.follow.service;

import cmpe451.group6.authorization.exception.CustomException;
import cmpe451.group6.authorization.model.RegistrationStatus;
import cmpe451.group6.authorization.model.User;
import cmpe451.group6.authorization.repository.UserRepository;
import cmpe451.group6.rest.follow.DTO.UsernameWrapper;
import cmpe451.group6.rest.follow.model.FollowDAO;
import cmpe451.group6.rest.follow.model.FollowStatus;
import cmpe451.group6.rest.follow.repository.FollowRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;

@Service
public class FollowService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private FollowRepository followRepository;

    @Autowired
    private ModelMapper modelMapper;

    public String followUser(String username, String requesterUsername) {

        if(username.equals(requesterUsername)){
            throw new CustomException("Self following is not acceptable.", HttpStatus.EXPECTATION_FAILED);
        }

        User userToFollow = userRepository.findByUsername(username);
        User requesterUser = userRepository.findByUsername(requesterUsername);

        if (userToFollow == null) {
            throw new CustomException("There is no user named " + username + ".", HttpStatus.NOT_ACCEPTABLE);
        }

        if (userToFollow.getRegistrationStatus() == RegistrationStatus.PENDING) {
            throw new CustomException("The user is not activate his/her account. Therefore s/he cannot be followed ",
                    HttpStatus.PRECONDITION_FAILED);
        }

        FollowStatus status = getFollowStatus(username, requesterUsername);

        if (status == FollowStatus.PENDING) {
            throw new CustomException("A request has already been sent to that user.", HttpStatus.PRECONDITION_FAILED);
        } else if (status == FollowStatus.APPROVED){
            throw new CustomException("The user has already been followed", HttpStatus.PRECONDITION_FAILED);
        }

        // status = null here, which means it's safe to create new followDAO

        FollowDAO temp = new FollowDAO();
        temp.setFollower(requesterUser);
        temp.setFollowee(userToFollow);

        String message ;
        if (userToFollow.getIsPrivate()) {
            message = "Request has been sent to ";
            temp.setFollowStatus(FollowStatus.PENDING);
        } else {
            message = "User followed: ";
            temp.setFollowStatus(FollowStatus.APPROVED);
        }

        followRepository.save(temp);
        return (message + userToFollow.getUsername());
        
    }

    public String unfollowUser(String username, String requesterUsername) {

        boolean userExists = userRepository.existsByUsername(username);

        if (!userExists) {
            throw new CustomException("There is no user named " + username, HttpStatus.NOT_ACCEPTABLE);
        }

        String message;
        FollowStatus status = getFollowStatus(username,requesterUsername);
        if(status == null) {
            throw new CustomException("Already not following " + username, HttpStatus.PRECONDITION_FAILED);
        } else if (status == FollowStatus.PENDING){
            message = "Request has been cancelled.";
        } else {
            message = "User has been unfollowed.";
        }

        followRepository.deleteByFolloweeUsernameAndFollowerUsername(username, requesterUsername);

        return message;
    }

    public String removeFollower(String followerName, String username){
        if(!userRepository.existsByUsername(username))
            throw new CustomException("No such user exists", HttpStatus.NOT_ACCEPTABLE);

        FollowDAO dao = followRepository.getFollowDAO(followerName,username);

        if(dao.getFollowStatus() != FollowStatus.APPROVED)
            throw new CustomException("User is not following.", HttpStatus.NOT_ACCEPTABLE);

        followRepository.delete(dao);
        return followerName + " has been removed from followers.";

    }

    public List<UsernameWrapper> getFollowingList(String username) {

        User currentUser = userRepository.findByUsername(username);

        if (currentUser == null) {
            throw new CustomException("There is no user named " + username + ". ", HttpStatus.NOT_ACCEPTABLE);
        } else {
            List<UsernameWrapper> followeeList = new ArrayList<UsernameWrapper>();
            followRepository.findByFollower_username(currentUser.getUsername())
                    .forEach(item -> followeeList.add(modelMapper.map(item.getFollowee(), UsernameWrapper.class)));
            return followeeList;
        }
    }

    public List<UsernameWrapper> getFollowerList(String username) {

        User currentUser = userRepository.findByUsername(username);

        if (currentUser == null) {
            throw new CustomException("There is no user named " + username + ". ", HttpStatus.NOT_ACCEPTABLE);
        } else {
            List<UsernameWrapper> followerList = new ArrayList<UsernameWrapper>();
            followRepository.findByFollowee_username(currentUser.getUsername())
                    .forEach(item -> followerList.add(modelMapper.map(item.getFollower(), UsernameWrapper.class)));
            return followerList;
        }
    }

    public List<UsernameWrapper> getPendingRequests(String username) {

        User currentUser = userRepository.findByUsername(username);

        if (currentUser == null) {
            throw new CustomException("There is no user named " + username + ". ", HttpStatus.NOT_ACCEPTABLE);
        } else {
            List<UsernameWrapper> followerList = new ArrayList<UsernameWrapper>();
            followRepository.findByFollowee_UsernameAndFollowStatus(username,FollowStatus.PENDING)
                    .forEach(item -> followerList.add(modelMapper.map(item.getFollowee(), UsernameWrapper.class)));
            return followerList;
        }
    }

    public int getFollowingsCount(String username) {
        return followRepository.getFollowingsCount(username);
    }

    public int getFollowersCount(String username) {
        return followRepository.getFollowersCount(username);
    }

    public FollowStatus getFollowStatus(String user, String follower){
        FollowDAO dao = followRepository.getFollowDAO(follower,user);
        if(dao == null) return null;
        return dao.getFollowStatus();
    }

    public void checkPermission(String username, String requesterUsername){
        User user = userRepository.findByUsername(username);

        if(user == null ) return; // Null check is handled by other methods.
        if(username.equals(requesterUsername)) return; // Allow self profile information

        if(user.getIsPrivate() && getFollowStatus(username,requesterUsername) != FollowStatus.APPROVED)
            throw new CustomException("Profile is private.", HttpStatus.PRECONDITION_REQUIRED);
    }

    public String answerRequest(String username, String requesterUsername, boolean accept){
        FollowDAO dao = followRepository.getFollowDAO(requesterUsername,username);
        if (dao == null) throw new CustomException("No such request exists",HttpStatus.PRECONDITION_FAILED);//412
        if (dao.getFollowStatus()==FollowStatus.APPROVED)
            throw new CustomException("Request is already approved.",HttpStatus.NOT_ACCEPTABLE);//406
        if(accept){
            dao.setFollowStatus(FollowStatus.APPROVED);
            followRepository.save(dao);
        } else {
            followRepository.delete(dao);
        }
        return "Success";
    }
}
