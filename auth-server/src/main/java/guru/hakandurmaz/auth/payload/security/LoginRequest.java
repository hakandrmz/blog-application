package guru.hakandurmaz.auth.payload.security;

import lombok.Data;

@Data
public class LoginRequest {

  private String usernameOrEmail;
  private String password;
}
