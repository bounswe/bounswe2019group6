package cmpe451.group6.authorization.model;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.Pattern;

@Entity
public class User {

  public static final transient String usernameRegex = "^\\w{3,20}$";
  // TODO: reformat iban regex (limit only a few country ibans)
  public static final transient String IBANRegex = "^[A-Z]{2}[0-9]{18}$";
  public static final transient String locationRegex = "^(-?\\d{1,5}(\\.\\d{1,10})?)$";
  public static final transient String passwordRegex = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{6,}$";
  public static final transient String emailRegex = "^\\w+([-+.']\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*$";

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  @Pattern(regexp = usernameRegex)
  @Column(unique = true, nullable = false)
  // TODO determine size on both database and here
  private String username;

  @Pattern(regexp = emailRegex)
  @Column(unique = true, nullable = false)
  private String email;

  @Pattern(regexp = IBANRegex)
  @Column
  private String IBAN;

  @Column(nullable = false)
  @Pattern(regexp = locationRegex)
  private String latitude;

  @Column(nullable = false)
  @Pattern(regexp = locationRegex)
  private String longitude;



  //   ^               # start-of-string
  // (?=.*[0-9])       # a digit must occur at least once
  // (?=.*[a-z])       # a lower case letter must occur at least once
  // (?=.*[A-Z])       # an upper case letter must occur at least once
  // (?=.*[@#$%^&+=])  # a special character must occur at least once
  // (?=\S+$)          # no whitespace allowed in the entire string
  // .{8,}             # anything, at least eight places though
  // $                 # end-of-string
  @Pattern(regexp = passwordRegex, message ="Password must -include at least a digit, a lower case letter, an uppercase letter, a special char. and not include any whitespaces. Minimum password length: 8 characters ")
  private String password;

  @ElementCollection(fetch = FetchType.EAGER)
  List<Role> roles;

  @Column(nullable = false)
  private RegistrationStatus registrationStatus;

  @Column(nullable = false)
  private boolean isPrivate;

  @Column
  private String googleToken;

  public String getGoogleToken() {
    return googleToken;
  }

  public void setGoogleToken(String googleToken) {
    this.googleToken = googleToken;
  }

  // NOTE : DO NOT CHANGE GETTER and SETTER SIGNATURES FOR THIS FIELD !!
  // Because the mapper seeks the getter & setter fields by these names,
  // not with "isPrivate()" or "setPrivate(boolean _)"
  public boolean getIsPrivate() {
    return isPrivate;
  }

  public void setIsPrivate(boolean aPrivate) {
    isPrivate = aPrivate;
  }


  public RegistrationStatus getRegistrationStatus() {
    return registrationStatus;
  }

  public void setRegistrationStatus(RegistrationStatus registrationStatus) {
    this.registrationStatus = registrationStatus;
  }

  public String getLatitude() {
    return latitude;
  }

  public void setLatitude(String latitude) {
    this.latitude = latitude;
  }

  public String getLongitude() {
    return longitude;
  }

  public void setLongitude(String longitude) {
    this.longitude = longitude;
  }

  public Integer getId() {
    return id;
  }

  public String getIBAN() {
    return IBAN;
  }

  public void setIBAN(String IBAN) {
    this.IBAN = IBAN;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public List<Role> getRoles() {
    return roles;
  }

  public void setRoles(List<Role> roles) {
    this.roles = roles;
  }

}
