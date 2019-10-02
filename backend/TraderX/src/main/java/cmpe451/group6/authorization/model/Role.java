package cmpe451.group6.authorization.model;

import org.springframework.security.core.GrantedAuthority;

public enum Role implements GrantedAuthority {
  ROLE_ADMIN, ROLE_TRADER, ROLE_BASIC;

  public String getAuthority() {
    return name();
  }

}
