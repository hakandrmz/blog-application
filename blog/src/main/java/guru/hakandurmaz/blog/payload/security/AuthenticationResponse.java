package guru.hakandurmaz.blog.payload.security;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthenticationResponse {

  private String accessToken;
  private String refreshToken;
  private String tokenType = "Bearer";

  public AuthenticationResponse(String responseToken) {
    this.accessToken=responseToken;
  }
}
