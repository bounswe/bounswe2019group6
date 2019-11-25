package cmpe451.group6.authorization.service;


import cmpe451.group6.authorization.dto.LoginInfoDTO;
import cmpe451.group6.authorization.dto.TokenWrapperDTO;
import cmpe451.group6.authorization.exception.CustomException;
import cmpe451.group6.authorization.model.RegistrationStatus;
import cmpe451.group6.authorization.model.User;
import cmpe451.group6.authorization.repository.UserRepository;
import cmpe451.group6.authorization.security.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Service;

@Service
public class LoginService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private AuthenticationManager authenticationManager;

    public TokenWrapperDTO login(LoginInfoDTO loginInfoDTO) {

        String username = loginInfoDTO.getUsername();
        String password = loginInfoDTO.getPassword();
        String googleToken = loginInfoDTO.getGoogleToken();

        if (password == null && googleToken == null) {
            throw new CustomException("Supply either google token or password", HttpStatus.NOT_ACCEPTABLE);
        }
        if (password != null && googleToken != null) {
            throw new CustomException("Supply only one of these: google token or password", HttpStatus.NOT_ACCEPTABLE);
        }

        User user = userRepository.findByUsername(username);

        if (user == null){
            throw new CustomException("No such a user", HttpStatus.GONE);
        }

        if (user.getRegistrationStatus() == RegistrationStatus.PENDING) {
            throw new CustomException("Account is not verified.", HttpStatus.PRECONDITION_FAILED);
        }

        boolean isGoogleLogin = loginInfoDTO.getGoogleToken() != null;

        if(user.getGoogleToken() == null){
            throw new CustomException("User is not registered via Google", HttpStatus.NOT_ACCEPTABLE);
        }

        if (!isGoogleLogin) {
            try {
                authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
                return new TokenWrapperDTO(jwtTokenProvider.createToken(username, user.getRoles()));
            } catch (AuthenticationException e) {
                throw new CustomException("Invalid username/password supplied", HttpStatus.UNAUTHORIZED);
            }
        } else { // Check if google tokes are matched
            if (user.getGoogleToken().equals(googleToken))
                return new TokenWrapperDTO(jwtTokenProvider.createToken(username, user.getRoles()));
            throw new CustomException("Invalid Google Token supplied", HttpStatus.UNAUTHORIZED);
        }
    }
}

