package sn.odc.oumar.springproject.Services.Impl;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class JwtService {

    @Value("${security.jwt.secret-key}")
    private String secretKey;

    @Value("${security.jwt.expiration-time}")
    private long jwtExpiration;

    // Extraire le nom d'utilisateur (username) à partir du token
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    // Extraire un claim spécifique à partir du token
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    // Générer un token JWT avec les rôles de l'utilisateur
    public String generateToken(UserDetails userDetails) {
        // Récupérer les rôles de l'utilisateur
        String roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        // Construire le JWT avec les rôles comme claim
        return Jwts.builder()
                .setSubject(userDetails.getUsername())
                .claim("roles", roles)  // Ajouter les rôles dans les claims
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpiration))  // Définir l'expiration du token
                .signWith(getSignInKey(), SignatureAlgorithm.HS256)  // Signer le token avec la clé secrète
                .compact();
    }

    // Générer un token avec des claims supplémentaires
    public String generateToken(Map<String, Object> extraClaims, UserDetails userDetails) {
        return buildToken(extraClaims, userDetails, jwtExpiration);
    }

    // Construire un token avec des claims supplémentaires et une durée d'expiration définie
    private String buildToken(Map<String, Object> extraClaims, UserDetails userDetails, long expiration) {
        // Extraire les rôles de l'utilisateur
        List<String> roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());
        // Ajouter les rôles aux extraClaims
        extraClaims.put("roles", roles);

        return Jwts.builder()
                .setClaims(extraClaims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    // Valider le token en vérifiant si l'utilisateur correspond et si le token n'est pas expiré
    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername())) && !isTokenExpired(token);
    }

    // Vérifier si le token est expiré
    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    // Extraire la date d'expiration du token
    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    // Extraire tous les claims du token
    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .setSigningKey(getSignInKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    // Générer la clé pour signer les tokens
    private Key getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
