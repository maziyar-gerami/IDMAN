package parsso.idman.Mobile.RepoImpls;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Service;
import parsso.idman.Models.User;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtUtil {
    private final String SECRET_KEY = "meis";

    public String extractUsrname(String token) {
        return extratClaim(token, Claims::getSubject);
    }

    public Date extractExpiration(String token) {
        return extratClaim(token, Claims::getExpiration);
    }

    public <T> T extratClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClamis(token);
        return claimsResolver.apply(claims);
    }

    public Claims extractAllClamis(String token) {
        return Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token).getBody();

    }

    public Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    public String generateToken(User user) {
        Map<String, Object> claims = new HashMap<>();
        return createToken(claims, user.getUserId());
    }

    public String createToken(Map<String, Object> claims, String subject) {
        return Jwts.builder().setClaims(claims).setSubject(subject).setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1200000))
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY).compact();
    }

    public Boolean validateToken(String token, User user) {
        final String username = extractUsrname(token);
        return (username.equals(user.getUserId()) && !isTokenExpired(token));
    }

    public String externFromCookie(String s) {
        String temp = "s";
        if (s.contains("Authorization=Bearer"))
            temp = s.substring(s.indexOf("Authorization") + 14);
        else
            temp = s;
        return temp;

    }


}
