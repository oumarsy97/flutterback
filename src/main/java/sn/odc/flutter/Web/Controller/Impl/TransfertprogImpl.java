package sn.odc.flutter.Web.Controller.Impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import sn.odc.flutter.Datas.Entity.Transaction;
import sn.odc.flutter.Datas.Entity.Transfertprogramme;
import sn.odc.flutter.Services.Interfaces.TransfertprogrammeService;
import sn.odc.flutter.Web.Controller.Interfaces.TransfertprogrammeControllerInterface;
import sn.odc.flutter.Web.Dtos.request.CreateTransfertprogrammeDTO;

import java.util.Base64;
import java.util.List;

@RestController
@RequestMapping("/transfertprogramme")
public class TransfertprogImpl extends BaseControllerImpl<Transfertprogramme, Long>
        implements TransfertprogrammeControllerInterface {

    private final TransfertprogrammeService service;
    @Autowired
    private HttpServletRequest request;

    public TransfertprogImpl(TransfertprogrammeService service) {
        super(service);
        this.service = service;
    }

    @GetMapping("/myprogrammes")
    @Override
    public List<Transfertprogramme> getTransfertprogrammeBySender() {
        String authHeader = request.getHeader("Authorization");
        System.out.println("getProfile" + authHeader);

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return null;
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

            return this.service.getTransfertprogrammeBySender(username);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

        @PostMapping("create")
    @Override
    public Transfertprogramme createTransfertprogramme(@RequestBody CreateTransfertprogrammeDTO createTransfertprogrammeDTO) {
        return this.service.createTransfertprogramme(createTransfertprogrammeDTO);
    }

    @Override
    public Transaction executeTransfertprogramme(@RequestBody Transfertprogramme transfertprogramme) {
        return this.service.executeTransfertprogramme(transfertprogramme);
    }
}