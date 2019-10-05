package cmpe451.group6.authorization.email;

import java.util.Properties;

// TODO:  Store those values in application.properties
public class EmailServiceConfig {

    static private final String senderAdress = "bounswegroup6@gmail.com";
    static private final String senderPassword = "bestgroupis6";
    static private final Properties props = new Properties();

    static {

        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");

    }

    public static String getSenderAdress() {
        return senderAdress;
    }

    public static String getSenderPassword() {
        return senderPassword;
    }

    public static Properties getProps() {
        return props;
    }
}
