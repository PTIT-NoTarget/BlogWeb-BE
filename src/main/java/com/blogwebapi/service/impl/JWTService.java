package com.blogwebapi.service.impl;

import com.blogwebapi.repository.IUserRepository;
import com.blogwebapi.service.IJWTService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.Map;
import java.util.function.Function;

@Service
public class JWTService implements IJWTService {
    @Autowired
    private IUserRepository userService;

    public String generateToken(UserDetails userDetails) {
        int userid = userService.findByUsername(userDetails.getUsername()).get().getUserId();
        return Jwts.builder()
                .setSubject(Integer.toString(userid))
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24))
                .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                .compact();
        
    }

    public String generateRefreshToken(Map<String, Object> claims, UserDetails userDetails) {
        int userid = userService.findByUsername(userDetails.getUsername()).get().getUserId();
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(Integer.toString(userid))
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000L * 60 * 60 * 24 * 30))
                .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                .compact();

    }

    public String extractUserId(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    private <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);

    }

    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSignInKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private Key getSignInKey() {
        byte[] key = Decoders.BASE64.decode("bb249fa35f9459ffa73393868b4fce1636e059eb95b56ffbc3867817d528452165a628478607bd33ff58dc53e38707a111b40a21f7b8655a9a770125f2519816");
        return Keys.hmacShaKeyFor(key);
    }

    public boolean isTokenValid(String token, UserDetails userDetails) {
        final Integer userid = Integer.parseInt(extractUserId(token));
        final String username = userService.findById(userid).get().getUsername();
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    private boolean isTokenExpired(String token) {
        return extractClaim(token, Claims::getExpiration).before(new Date());
    }
}
