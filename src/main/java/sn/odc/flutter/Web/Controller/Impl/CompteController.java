package sn.odc.flutter.Web.Controller.Impl;


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import sn.odc.flutter.Datas.Entity.Client;
import sn.odc.flutter.Datas.Entity.Compte;
import sn.odc.flutter.Services.Interfaces.BaseService;
import sn.odc.flutter.Services.Interfaces.CompteService;
import sn.odc.flutter.Web.Controller.Interfaces.CompteControllerInterface;
import sn.odc.flutter.Web.Dtos.response.DataResponse;
import sn.odc.flutter.Web.Dtos.response.GenericResponse;
import sn.odc.flutter.Web.Dtos.response.LoginResponse;

import java.security.Key;
import java.util.Base64;
import java.util.List;

@RestController
@RequestMapping("/comptes")
public class CompteController extends BaseControllerImpl<Compte,Long> implements CompteControllerInterface {
    @Value("${security.jwt.secret-key}")
    private String secretKey;

    private final CompteService compteService;
    @Autowired
    private HttpServletRequest request;

    @Autowired
    public CompteController(CompteService compteService) {
        super(compteService);
        this.compteService = compteService;
    }

    @Override
    @GetMapping("/monprofile")
    public ResponseEntity<DataResponse> getProfile() {
        // Récupérer le token du header Authorization
        String authHeader = request.getHeader("Authorization");
        System.out.println("getProfile" + authHeader);

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.ok(new DataResponse(null, "Token non fourni"));
        }

        // Extraire le token sans le prefix "Bearer "
        String token = authHeader.substring(7);
        System.out.println("Token: " + token);

        try {
            // 2. Extraire et décoder le token
            String token2 = authHeader.substring(7);
            String[] chunks = token.split("\\.");

            // 3. Decoder le payload (deuxième partie du token)
            Base64.Decoder decoder = Base64.getUrlDecoder();
            String payload = new String(decoder.decode(chunks[1]));

            // 4. Parser le JSON du payload
            ObjectMapper mapper = new ObjectMapper();
            JsonNode claims = mapper.readTree(payload);

            // 5. Extraire les informations
            String username = claims.get("sub").asText();  // sub est le claim standard pour username
            System.out.println("username : " + username);

            Compte compte = compteService.findCompteByToken(username);
            if (compte == null) {
                return ResponseEntity
                        .status(HttpStatus.UNAUTHORIZED)
                        .body(new DataResponse(null, "Compte non trouvé"));
            }

            return ResponseEntity.ok(new DataResponse( "Profil recupere avec succes",compte));

        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body(new DataResponse("Token expiré",null));
        }
    }

    private Key getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}

