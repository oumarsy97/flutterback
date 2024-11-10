package sn.odc.flutter.Services.Impl;

import sn.odc.flutter.Services.Interfaces.EmailService;
import sn.odc.flutter.Services.Interfaces.QRCodeService;
import sn.odc.flutter.Datas.Entity.Client;
import sn.odc.flutter.Datas.Entity.Compte;
import sn.odc.flutter.Datas.Repository.Interfaces.ClientRepository;
import sn.odc.flutter.Services.Interfaces.ClientService;
import sn.odc.flutter.Services.Interfaces.CompteService;
import sn.odc.flutter.Web.Dtos.request.CreateClientDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;

@Service
public class ClientServiceImpl extends BaseServiceImpl<Client, Long> implements ClientService {

    private static final Logger logger = LoggerFactory.getLogger(ClientServiceImpl.class);

    private final CompteService compteService;
    private final ClientRepository clientRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;
    private final QRCodeService qrCodeService;

    @Autowired
    public ClientServiceImpl(ClientRepository clientRepository,
                             CompteService compteService,
                             PasswordEncoder passwordEncoder,
                             EmailService emailService,
                             QRCodeService qrCodeService) {
        super(clientRepository);
        this.compteService = compteService;
        this.clientRepository = clientRepository;
        this.passwordEncoder = passwordEncoder;
        this.emailService = emailService;
        this.qrCodeService = qrCodeService;
    }

    @Override
    @Transactional
    public Client createClient(CreateClientDTO request) {
        validateRequest(request);

        try {
            // Création de compte associé au client
            Compte compte = new Compte();
            compte.setEmail(request.getEmail());
            compte.setTelephone(request.getTelephone());
            compte.setPassword(passwordEncoder.encode(request.getPassword()));


            // Générer le QR Code
            byte[] qrCodeBytes = qrCodeService.generateQRCodeImage(request.getTelephone());
            compte.setQrcode(qrCodeBytes);

            // Créer le compte
            compte = compteService.createCompte(compte);

            // Créer le client associé
            Client client = new Client();
            client.setNom(truncateString(request.getNom().trim(), 50));
            client.setPrenom(truncateString(request.getPrenom().trim(), 50));

            client.setCompte(compte);
            compte.setClient(client);

            // Sauvegarder le client
            Client savedClient = clientRepository.save(client);

            // Envoyer l'email avec le QR Code
            sendWelcomeEmailWithQRCode(savedClient, qrCodeBytes);

            return savedClient;

        } catch (Exception e) {
            logger.error("Erreur lors de la création du client", e);
            throw new RuntimeException("Erreur lors de la création du client: " + e.getMessage());
        }
    }

    private void sendWelcomeEmailWithQRCode(Client client, byte[] qrCodeBytes) {
        try {
            // Créer un fichier temporaire pour le QR code
            String tempDir = System.getProperty("java.io.tmpdir");
            String fileName = "qrcode_" + client.getId() + ".png";
            Path qrCodePath = Paths.get(tempDir, fileName);

            // Sauvegarder le QR code temporairement
            Files.write(qrCodePath, qrCodeBytes);

            // Préparer le contenu de l'email
            String emailContent = String.format("""
                Bonjour %s %s,
                
                Bienvenue ! Votre compte a été créé avec succès.
                
                Voici votre QR Code en pièce jointe. Vous en aurez besoin pour vos futures paiements.
                
                Cordialement,
                L'équipe Flutter
                """, client.getPrenom(), client.getNom());

            // Envoyer l'email avec le QR code en pièce jointe
            emailService.sendEmailWithAttachment(
                    client.getCompte().getEmail(),
                    "Bienvenue - dans TransEasy",
                    emailContent,
                    qrCodePath.toString()
            );

            // Supprimer le fichier temporaire
            Files.deleteIfExists(qrCodePath);

        } catch (Exception e) {
            logger.error("Erreur lors de l'envoi de l'email de bienvenue", e);
            // Ne pas bloquer la création du compte si l'envoi d'email échoue
            // mais logger l'erreur pour le suivi
        }
    }

    private String truncateString(String str, int length) {
        if (str == null) return null;
        return str.length() > length ? str.substring(0, length) : str;
    }

    private void validateRequest(CreateClientDTO request) {
        if (request.getEmail() == null || request.getEmail().isBlank()) {
            throw new IllegalArgumentException("L'email est obligatoire");
        }
        if (request.getTelephone() == null || request.getTelephone().isBlank()) {
            throw new IllegalArgumentException("Le numéro de téléphone est obligatoire");
        }
        if (request.getNom() == null || request.getNom().isBlank()) {
            throw new IllegalArgumentException("Le nom est obligatoire");
        }
        if (request.getPrenom() == null || request.getPrenom().isBlank()) {
            throw new IllegalArgumentException("Le prénom est obligatoire");
        }
        if (request.getPassword() == null || request.getPassword().isBlank()) {
            throw new IllegalArgumentException("Le mot de passe est obligatoire");
        }

        // Vérifier si un compte existe déjà avec cet email ou ce téléphone
        if (compteService.existsByEmail(request.getEmail().toLowerCase().trim())) {
            throw new IllegalArgumentException("Un compte existe déjà avec cet email");
        }
        if (compteService.existsByTelephone(formatTelephone(request.getTelephone()))) {
            throw new IllegalArgumentException("Un compte existe déjà avec ce numéro de téléphone");
        }
    }

    private String formatTelephone(String telephone) {
        return telephone.replaceAll("[^0-9+]", "");
    }
}