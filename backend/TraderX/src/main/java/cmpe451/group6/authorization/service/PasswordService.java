package cmpe451.group6.authorization.service;

import cmpe451.group6.authorization.email.EmailService;
import cmpe451.group6.authorization.exception.CustomException;
import cmpe451.group6.authorization.model.User;
import cmpe451.group6.authorization.repository.UserRepository;
import cmpe451.group6.authorization.security.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@Service
public class PasswordService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private EmailService emailService;

    @Value("${server.url}")
    private String baseURL;

    @Value("${server.port}")
    private String port;

    public String sendRenewalMail(String mail){
        User user = userRepository.findByEmail(mail);
        if(user != null){
            String token = jwtTokenProvider.createToken(user.getUsername(), user.getRoles());
            try {
                emailService.sendmail(user.getEmail(), "Forgot Password", "xxx", buildPasswordRenewalURL(token), null);
            } catch (MessagingException | IOException e) {
                e.printStackTrace();
                throw new CustomException(String.format("Failed to send verification email to the address: %s", mail) , HttpStatus.INTERNAL_SERVER_ERROR);
            }

            return "Link to reset password has been sent to your email.";
        } else {
            throw new CustomException("No user found for the email", HttpStatus.BAD_REQUEST);
        }
    }


    public String setNewPassword(String token, String newPassword){
        try {
            String username = jwtTokenProvider.getUsername(token);
            User user = userRepository.findByUsername(username);
            user.setPassword(passwordEncoder.encode(newPassword));
            userRepository.save(user);
            // Invalidate token;
            HazelcastService.invalidateToken(token, user.getUsername());
            return "Password has been changed.";
        } catch (AuthenticationException e) {
            throw new CustomException("Invalid TOKEN", HttpStatus.UNAUTHORIZED);
        }
    }

    public String changePassword(HttpServletRequest req, String newPassword){
        User user = userRepository.findByUsername(jwtTokenProvider.getUsername(jwtTokenProvider.resolveToken(req)));
        if(!newPassword.matches(User.passwordRegex)) throw  new CustomException("Password does not conform to restrictions.",HttpStatus.EXPECTATION_FAILED);
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
        return "Password has been changed.";
    }

    private String buildPasswordRenewalURL(String token){
        return baseURL+":"+port+"/users/renew?token=" + token;
    }

}
