package guru.hakandurmaz.blog.security;

import guru.hakandurmaz.blog.entity.User;
import guru.hakandurmaz.blog.exception.BlogAPIException;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

@Component
public class JwtTokenProvider {

  @Value("${app.jwt-secret}")
  private String jwtSecret;

  @Value("${app.jwt-expiration-milliseconds}")
  private long jwtExpirationMilliseconds;

  @Value("${app.jwt-refresh-token.expiration}")
  private long refreshExpiration;

  public String generateToken(User user) {

    String username = user.getEmail();
    Date currentDate = new Date();
    Date expireDate = new Date(currentDate.getTime() + jwtExpirationMilliseconds);

    return Jwts.builder()
        .setSubject(username)
        .setIssuedAt(new Date())
        .setExpiration(expireDate)
        .signWith(key(), SignatureAlgorithm.HS256)
        .compact();
  }

  private String buildToken(Map<String, Object> extraClaims, User user, long expiration) {
    return Jwts.builder()
            .setClaims(extraClaims)
            .setSubject(user.getEmail())
            .setIssuedAt(new Date(System.currentTimeMillis()))
            .setExpiration(new Date(System.currentTimeMillis() + expiration))
            .signWith(key(), SignatureAlgorithm.HS256)
            .compact();
  }

  private Key key() {
    return Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecret));
  }

  public String extractUsername(String token) {

    return Jwts.parserBuilder()
        .setSigningKey(key())
        .build()
        .parseClaimsJws(token)
        .getBody()
        .getSubject();
  }
  
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

  public boolean isTokenValid(String token, User userDetails) {
    final String username = extractUsername(token);
    return (username.equals(userDetails.getEmail())) && !isTokenExpired(token);
  }

  private boolean isTokenExpired(String token) {
    return extractExpiration(token).before(new Date());
  }

  private Date extractExpiration(String token) {
    return extractClaim(token, Claims::getExpiration);
  }

  public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
    final Claims claims = extractAllClaims(token);
    return claimsResolver.apply(claims);
  }

  private Claims extractAllClaims(String token) {
    return Jwts.parserBuilder().setSigningKey(key()).build().parseClaimsJws(token).getBody();
  }

  public String generateRefreshToken(User user) {
    return buildToken(new HashMap<>(), user, refreshExpiration);
  }


}
