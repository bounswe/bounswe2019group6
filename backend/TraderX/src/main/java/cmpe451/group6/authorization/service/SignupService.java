package cmpe451.group6.authorization.service;

import cmpe451.group6.authorization.email.EmailService;
import cmpe451.group6.authorization.exception.CustomException;
import cmpe451.group6.authorization.model.RegistrationStatus;
import cmpe451.group6.authorization.model.Role;
import cmpe451.group6.authorization.model.User;
import cmpe451.group6.authorization.repository.UserRepository;
import cmpe451.group6.authorization.security.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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

    @Autowired
    private HazelcastService hazelcastService;

    @Value("${email.frontend_url}")
    private String baseURL;

    @Value("${email.frontend_port}")
    private String port;

    @Value("${email.frontend_verification_path}")
    private String verificationPath;

    @Value("${security.jwt.token.secret-key}")
    private String appSecret;

    public String signup(User user, String appSecret) {

        if (!userRepository.existsByUsername(user.getUsername()) && !userRepository.existsByEmail(user.getEmail())) {

            // Check field validity. Throw exception and return error if at least one of
            // them is wrong
            validateUserData(user);

            boolean isGoogleUser = user.getPassword() == null;

            if(isGoogleUser){
                if(appSecret == null)
                    throw new CustomException("Null app secret.", HttpStatus.UNAUTHORIZED);

                if(!appSecret.equals(this.appSecret))
                    throw new CustomException("Wrong App Secret", HttpStatus.UNAUTHORIZED);
            }

            Role role = validateIBAN(user.getIBAN());
            user.setRoles(new ArrayList<>(Arrays.asList(role)));

            // Only one of password or token is supplied guarenteed here

            if(!isGoogleUser){ // email sign up
                user.setPassword(passwordEncoder.encode(user.getPassword()));
                user.setRegistrationStatus(RegistrationStatus.PENDING);
                String token = jwtTokenProvider.createToken(user.getUsername(), user.getRoles());
                try {
                    emailService.sendmail(user.getEmail(), "verification", "xxx", buildVerificationURL(token), null);
                } catch (MessagingException | IOException e) {
                    e.printStackTrace();
                    throw new CustomException("Failed to send verification email", HttpStatus.INTERNAL_SERVER_ERROR);
                }
            } else { // google sign up
                // google token is already set. no explicit operation is needed.

                user.setRegistrationStatus(RegistrationStatus.GOOGLE_SIGN);
            }

            userRepository.save(user);

            return isGoogleUser ? "Google token is saved for this user." : "Confirmation Email is sent. Success";
        } else {
            throw new CustomException("Username or email is already in use", HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }

    public String confirmUser(String token) {
        try {
            String username = jwtTokenProvider.getUsername(token);
            User user = userRepository.findByUsername(username);
            user.setRegistrationStatus(RegistrationStatus.ENABLED);
            userRepository.save(user);
            hazelcastService.invalidateToken(token, username);
            return "Confirmation completed";
        } catch (AuthenticationException e) {
            throw new CustomException("Invalid TOKEN", HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }

    public String internal_signup(User user){
        if (!userRepository.existsByUsername(user.getUsername())) {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            userRepository.save(user);
            return jwtTokenProvider.createToken(user.getUsername(), user.getRoles());
        } else {
            throw new CustomException("Username is already in use", HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }

    /**
     * If an IBAN number is provided by client, then check if it's valid. If so, set
     * it's role as TRADER. else, throw exception. If it's not provided, than the
     * user wants to be a BASIC_USER.
     * 
     * @param IBAN IBAN provided
     * @return Desired and validted Role for the user
     */
    private Role validateIBAN(String IBAN) throws CustomException {
        if (IBAN == null) {
            return Role.ROLE_BASIC;
        }
        if (IBAN.matches(User.IBANRegex)) {
            return Role.ROLE_TRADER;
        }

        // IBAN is provided but not valid
        throw new CustomException("Invalid IBAN number. Must match: ^[A-Z]{2}[0-9]{18}$",
                HttpStatus.UNPROCESSABLE_ENTITY);
    }

    private String buildVerificationURL(String token){
        return baseURL+"/"+verificationPath+"?token="+token;
    }

    private void validateUserData(User user) {
        if (user.getUsername() == null || !user.getUsername().matches(User.usernameRegex)) {
            throw new CustomException("Invalid username", HttpStatus.PRECONDITION_FAILED);
        }
        if (user.getPassword() == null && user.getGoogleToken() == null) {
            // Supply only one authentication credential
            throw new CustomException("Supply password or google token", HttpStatus.PRECONDITION_FAILED);
        }
        if (user.getPassword() != null && user.getGoogleToken() != null) {
            // Supply only one authentication credential
            throw new CustomException("Supply only one of those: password or google token", HttpStatus.PRECONDITION_FAILED);
        }
        if (user.getPassword() != null && !user.getPassword().matches(User.passwordRegex)) {
            throw new CustomException("Invalid password", HttpStatus.PRECONDITION_FAILED);
        }
        if (user.getEmail() == null || !user.getEmail().matches(User.emailRegex)) {
            throw new CustomException("Invalid email", HttpStatus.PRECONDITION_FAILED);
        }
        if (user.getLatitude() == null || !user.getLatitude().matches(User.locationRegex)) {
            throw new CustomException("Invalid latitude", HttpStatus.PRECONDITION_FAILED);
        }
        if (user.getLongitude() == null || !user.getLongitude().matches(User.locationRegex)) {
            throw new CustomException("Invalid longitude", HttpStatus.PRECONDITION_FAILED);
        }
        if (user.getIBAN() != null) {
            if (!user.getIBAN().matches(User.IBANRegex))
                throw new CustomException("Invalid IBAN", HttpStatus.PRECONDITION_FAILED);
        }

    }
}
