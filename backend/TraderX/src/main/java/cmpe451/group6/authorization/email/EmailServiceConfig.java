package cmpe451.group6.authorization.email;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Properties;

@Service
public class EmailServiceConfig {

    private final String senderAdress;
    private final String senderPassword;
    private final Properties props = new Properties();

    @Autowired
    public EmailServiceConfig(@Value("${email.sender_address}") String senderAdress,
                              @Value("${email.sender_password}") String senderPassword) {
        this.senderAdress = senderAdress;
        this.senderPassword = senderPassword;

        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");

    }

    public String getSenderAdress() {
        return senderAdress;
    }

    public String getSenderPassword() {
        return senderPassword;
    }

    public Properties getProps() {
        return props;
    }
}
