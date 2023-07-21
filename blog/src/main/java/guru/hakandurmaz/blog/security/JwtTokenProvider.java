package guru.hakandurmaz.blog.security;

import guru.hakandurmaz.blog.exception.BlogAPIException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import java.security.Key;
import java.util.Date;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@Component
public class JwtTokenProvider {

  @Value("${app.jwt-secret}")
  private String jwtSecret;

  @Value("${app.jwt-expiration-milliseconds}")
  private int jwtExpirationMilliseconds;

  public String generateToken(Authentication authentication) {

    String username = authentication.getName();
    Date currentDate = new Date();
    Date expireDate = new Date(currentDate.getTime() + jwtExpirationMilliseconds);

    return Jwts.builder()
        .setSubject(username)
        .setIssuedAt(new Date())
        .setExpiration(expireDate)
        .signWith(key(), SignatureAlgorithm.HS256)
        .compact();
  }

  private Key key() {
    return Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecret));
  }

  // get username for token
  public String getUsernameFromJWT(String token) {

    return Jwts.parserBuilder()
        .setSigningKey(key())
        .build()
        .parseClaimsJws(token)
        .getBody()
        .getSubject();
  }

  // validate token
  public boolean validateToken(String token) {

    try {
      Jwts.parserBuilder().setSigningKey(key()).build().parse(token);
      return true;
    } catch (MalformedJwtException ex) {
      throw new BlogAPIException(HttpStatus.BAD_REQUEST, "Invalid JWT token.");
    } catch (UnsupportedJwtException ex) {
      throw new BlogAPIException(HttpStatus.BAD_REQUEST, "Unsupported JWT signature.");
    } catch (IllegalArgumentException ex) {
      throw new BlogAPIException(HttpStatus.BAD_REQUEST, "Invalid JWT signature.");
    }
  }
}
