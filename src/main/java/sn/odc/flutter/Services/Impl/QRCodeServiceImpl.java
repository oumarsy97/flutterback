package sn.odc.flutter.Services.Impl;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import org.springframework.stereotype.Service;
import sn.odc.flutter.Services.Interfaces.QRCodeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;

@Service
public class QRCodeServiceImpl implements QRCodeService {

    private static final Logger logger = LoggerFactory.getLogger(QRCodeServiceImpl.class);
    private static final int DEFAULT_WIDTH = 300;
    private static final int DEFAULT_HEIGHT = 300;

    @Override
    public byte[] generateQRCodeImage(String content) throws RuntimeException {
        // Validation du contenu
        if (content == null || content.trim().isEmpty()) {
            throw new IllegalArgumentException("Le contenu du QR code ne peut pas être vide");
        }

        try {
            QRCodeWriter qrCodeWriter = new QRCodeWriter();
            BitMatrix bitMatrix = qrCodeWriter.encode(content, BarcodeFormat.QR_CODE, DEFAULT_WIDTH, DEFAULT_HEIGHT);

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            MatrixToImageWriter.writeToStream(bitMatrix, "PNG", outputStream);

            return outputStream.toByteArray();
        } catch (WriterException e) {
            logger.error("Erreur lors de la génération du QR code", e);
            throw new RuntimeException("Erreur lors de la génération du QR code: " + e.getMessage());
        } catch (IOException e) {
            logger.error("Erreur lors de l'écriture du QR code", e);
            throw new RuntimeException("Erreur lors de l'écriture du QR code: " + e.getMessage());
        }
    }

    @Override
    public void saveQRCodeImage(String content, String filePath) throws RuntimeException {
        // Validation des paramètres
        if (content == null || content.trim().isEmpty()) {
            throw new IllegalArgumentException("Le contenu du QR code ne peut pas être vide");
        }
        if (filePath == null || filePath.trim().isEmpty()) {
            throw new IllegalArgumentException("Le chemin du fichier ne peut pas être vide");
        }

        try {
            // Génération du QR code
            byte[] qrCodeImage = generateQRCodeImage(content);

            // Création du répertoire parent si nécessaire
            Path path = Paths.get(filePath);
            Files.createDirectories(path.getParent());

            // Sauvegarde du fichier
            Files.write(path, qrCodeImage);

            logger.info("QR code sauvegardé avec succès: {}", filePath);
        } catch (IOException e) {
            logger.error("Erreur lors de la sauvegarde du QR code", e);
            throw new RuntimeException("Erreur lors de la sauvegarde du QR code: " + e.getMessage());
        }
    }

    @Override
    public byte[] generateQRCodeImage(String content, int width, int height) throws RuntimeException {
        // Validation des paramètres
        if (content == null || content.trim().isEmpty()) {
            throw new IllegalArgumentException("Le contenu du QR code ne peut pas être vide");
        }
        if (width <= 0 || height <= 0) {
            throw new IllegalArgumentException("Les dimensions du QR code doivent être positives");
        }

        try {
            QRCodeWriter qrCodeWriter = new QRCodeWriter();
            BitMatrix bitMatrix = qrCodeWriter.encode(content, BarcodeFormat.QR_CODE, width, height);

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            MatrixToImageWriter.writeToStream(bitMatrix, "PNG", outputStream);

            return outputStream.toByteArray();
        } catch (WriterException e) {
            logger.error("Erreur lors de la génération du QR code", e);
            throw new RuntimeException("Erreur lors de la génération du QR code: " + e.getMessage());
        } catch (IOException e) {
            logger.error("Erreur lors de l'écriture du QR code", e);
            throw new RuntimeException("Erreur lors de l'écriture du QR code: " + e.getMessage());
        }
    }
}