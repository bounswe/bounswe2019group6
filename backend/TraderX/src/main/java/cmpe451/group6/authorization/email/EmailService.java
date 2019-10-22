package cmpe451.group6.authorization.email;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import javax.mail.*;
import javax.mail.internet.*;
import java.io.IOException;
import java.util.Date;
import java.util.Properties;

@Service
public class EmailService {

    @Autowired
    EmailServiceConfig emailServiceConfig;

    public void sendmail(String receiverAdress, String subject, String content, String mimeContent, String attachFilePath) throws  MessagingException, IOException {


        String sender = emailServiceConfig.getSenderAdress();
        String senderPw = emailServiceConfig.getSenderPassword();
        Properties props = emailServiceConfig.getProps();

        Session session = Session.getInstance(props, new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(sender , senderPw);
            }
        });
        Message msg = new MimeMessage(session);
        msg.setFrom(new InternetAddress(sender , false));

        msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(receiverAdress));
        msg.setSubject(subject);
        msg.setContent(content, "text/html");
        msg.setSentDate(new Date());

        MimeBodyPart messageBodyPart = new MimeBodyPart();
        messageBodyPart.setContent(mimeContent, "text/html");

        Multipart multipart = new MimeMultipart();
        multipart.addBodyPart(messageBodyPart);
        MimeBodyPart attachPart = new MimeBodyPart();

        if (attachFilePath != null) {
            attachPart.attachFile(attachFilePath);
            multipart.addBodyPart(attachPart);
        }

        msg.setContent(multipart);
        Transport.send(msg);
    }
}