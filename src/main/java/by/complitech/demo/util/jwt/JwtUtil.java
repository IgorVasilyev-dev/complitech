package by.complitech.demo.util.jwt;


import by.complitech.demo.model.RefreshToken;
import by.complitech.demo.model.User;
import by.complitech.demo.util.jwt.api.IJwtUtil;
import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.Date;
import java.util.UUID;

import static java.lang.String.format;

@Component
public class JwtUtil implements IJwtUtil<User> {

    @Value("${jwt.issuer}")
    private String jwtIssuer;

    @Value("${jwt.secret}")
    private String jwtSecret;

    @Value("${jwt.jwtExpirationMs}")
    private long jwtExpirationMs;

    @Value("${jwt.jwtRefreshExpirationMs}")
    private long refreshTokenDurationMs;

    @Override
    public String generateAccessToken(User user) {
        return Jwts.builder()
                .setSubject(format("%s,%s", user.getId(), user.getLogin()))
                .setIssuer(jwtIssuer)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpirationMs))
                .signWith(SignatureAlgorithm.HS256, jwtSecret)
                .compact();
    }

    @Override
    public RefreshToken generateRefreshToken(User user) {
        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setUser(user);
        refreshToken.setExpiryDate(Instant.now().plusMillis(refreshTokenDurationMs));
        refreshToken.setToken(UUID.randomUUID().toString());
        return refreshToken;
    }

    @Override
    public String getUserId(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(jwtSecret)
                .parseClaimsJws(token)
                .getBody();

        return claims.getSubject().split(",")[0];
    }

    @Override
    public String getLogin(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(jwtSecret)
                .parseClaimsJws(token)
                .getBody();

        return claims.getSubject().split(",")[1];
    }

    @Override
    public Date getExpirationDate(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(jwtSecret)
                .parseClaimsJws(token)
                .getBody();

        return claims.getExpiration();
    }

    @Override
    public boolean validate(String token) {
        try {
            Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token);
            return true;
        } catch (SignatureException ex) {
            System.out.println(("Invalid JWT signature - " + ex.getMessage()));
        } catch (MalformedJwtException ex) {
            System.out.println(("Invalid JWT token - " + ex.getMessage()));
        } catch (ExpiredJwtException ex) {
            System.out.println(("Expired JWT token - " + ex.getMessage()));
        } catch (UnsupportedJwtException ex) {
            System.out.println(("Unsupported JWT token - " + ex.getMessage()));
        } catch (IllegalArgumentException ex) {
            System.out.println(("JWT claims string is empty - " + ex.getMessage()));
        }
        return false;
    }

}
