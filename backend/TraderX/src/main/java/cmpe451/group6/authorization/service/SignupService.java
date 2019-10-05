package cmpe451.group6.authorization.service;

import cmpe451.group6.authorization.dto.TokenWrapperDTO;
import cmpe451.group6.authorization.email.EmailService;
import cmpe451.group6.authorization.exception.CustomException;
import cmpe451.group6.authorization.model.RegistrationStatus;
import cmpe451.group6.authorization.model.Role;
import cmpe451.group6.authorization.model.User;
import cmpe451.group6.authorization.repository.UserRepository;
import cmpe451.group6.authorization.security.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import javax.mail.MessagingException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

@Service
public class SignupService {

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


    public String signup(User user) {
        if (!userRepository.existsByUsername(user.getUsername()) && !userRepository.existsByEmail(user.getEmail())) {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            Role role = validateIBAN(user.getIBAN());
            user.setRoles(new ArrayList<>(Arrays.asList(role)));
            user.setStatus(RegistrationStatus.PENDING);
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

    public TokenWrapperDTO admin_signup(User user){
        if (!userRepository.existsByUsername(user.getUsername())) {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            userRepository.save(user);
            return new TokenWrapperDTO(jwtTokenProvider.createToken(user.getUsername(), user.getRoles()));
        } else {
            throw new CustomException("Username is already in use", HttpStatus.UNPROCESSABLE_ENTITY);
        }
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
}
