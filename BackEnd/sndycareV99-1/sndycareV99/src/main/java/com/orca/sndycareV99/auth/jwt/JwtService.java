package com.orca.sndycareV99.auth.jwt;

import com.orca.sndycareV99.entity.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import io.jsonwebtoken.io.Decoders;

import javax.crypto.SecretKey;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtService {

    @Value("${jwt.secret-key}")
    private String SECRET ;

    @Value("${jwt.expiration-time:86400000}")
    private Long EXPIRATION_TIME;

    public SecretKey getSignedKey(){
        byte[] keyBytes = Decoders.BASE64.decode(SECRET);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public  String generateToken(User user){
        return Jwts.builder()
                .subject(user.getEmail())
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(getSignedKey())
                .compact();
    }

   public <T> T extractClaimByToken(String token, Function<Claims, T> claimsResolver){

        Claims claims = Jwts.parser()
                .verifyWith(getSignedKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();

        return claimsResolver.apply(claims);
   }

    public String extractUserName(String token){
        return extractClaimByToken(token, Claims::getSubject);
    }

    public Date extractDateExpiration(String token){
        return extractClaimByToken(token, Claims::getExpiration);
    }

    public boolean isTokenExpired(String token){
        return extractDateExpiration(token).before(new Date());
    }

    public boolean validateToken(String token , UserDetails userDetails){
        return extractUserName(token).equals(userDetails.getUsername());
    }






}
