package sn.odc.flutter.Services.Impl;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.common.BitMatrix;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import sn.odc.flutter.Datas.Entity.Compte;
import sn.odc.flutter.Datas.Repository.Interfaces.CompteRepository;
import sn.odc.flutter.Services.Interfaces.CompteService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;

@Service
public class CompteServiceImpl extends BaseServiceImpl<Compte, Long> implements CompteService {

    private static final Logger logger = LoggerFactory.getLogger(CompteServiceImpl.class);

    private final CompteRepository compteRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    @Value("${security.jwt.secret-key}")
    private String jwtSecret;

    public CompteServiceImpl(CompteRepository compteRepository) {
        super(compteRepository);
        this.compteRepository = compteRepository;
        this.passwordEncoder = new BCryptPasswordEncoder();
    }

    @Override
    public boolean existsByEmail(String email) {
        return compteRepository.existsByEmail(email);
    }

    @Override
    public boolean existsByTelephone(String telephone) {
        return compteRepository.existsByTelephone(telephone);
    }

    @Override
    public Compte createCompte(Compte compte) {
        // Cryptage du mot de passe
        compte.setPassword(passwordEncoder.encode(compte.getPassword()));
        compte.setTelephone(compte.getTelephone());


        // Génération du QR code
        try {
            compte.setQrcode(generateQRCode(compte.getReference()));
        } catch (Exception e) {
            logger.error("Erreur lors de la génération du QR code", e);
        }

        return compteRepository.save(compte);
    }

    @Override
    public Compte findCompteByToken(String telephone) {
        try {


            // Rechercher et retourner le compte correspondant à l'email
            Compte compte = compteRepository.findCompteByTelephone(telephone)
                    .orElse(null);
            // Masquer le mot de passe dans la réponse
            compte.setPassword(null);
            return compte;

        } catch (Exception e) {
            logger.error("Erreur lors du décodage du token JWT", e);
            return null;
        }
    }

    // Méthode pour générer le QR code
    private byte[] generateQRCode(String reference) throws WriterException, IOException {
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        BitMatrix bitMatrix = qrCodeWriter.encode(reference, BarcodeFormat.QR_CODE, 200, 200);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        MatrixToImageWriter.writeToStream(bitMatrix, "PNG", outputStream);
        byte[] qrCodeBytes = outputStream.toByteArray();

        return Base64.getEncoder().encodeToString(qrCodeBytes).getBytes();
    }
}