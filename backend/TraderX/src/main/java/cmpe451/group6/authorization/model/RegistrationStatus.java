package cmpe451.group6.authorization.model;

public enum RegistrationStatus {
    ENABLED, PENDING, GOOGLE_SIGN;

    public String getStatus(){
        return name();
    }
}
