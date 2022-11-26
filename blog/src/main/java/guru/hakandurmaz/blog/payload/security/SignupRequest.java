package guru.hakandurmaz.blog.payload.security;

import lombok.Data;

@Data
public class SignupRequest {

  private String name;
  private String username;
  private String email;
  private String password;
}
