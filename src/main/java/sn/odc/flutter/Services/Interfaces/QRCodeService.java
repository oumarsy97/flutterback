package sn.odc.flutter.Services.Interfaces;

public interface QRCodeService {
    /**
     * Génère une image QR code avec les dimensions par défaut
     * @param content Le contenu à encoder dans le QR code
     * @return Un tableau de bytes représentant l'image du QR code au format PNG
     * @throws RuntimeException si une erreur survient lors de la génération
     */
    byte[] generateQRCodeImage(String content) throws RuntimeException;

    /**
     * Génère une image QR code avec des dimensions personnalisées
     * @param content Le contenu à encoder dans le QR code
     * @param width La largeur souhaitée du QR code
     * @param height La hauteur souhaitée du QR code
     * @return Un tableau de bytes représentant l'image du QR code au format PNG
     * @throws RuntimeException si une erreur survient lors de la génération
     */
    byte[] generateQRCodeImage(String content, int width, int height) throws RuntimeException;

    /**
     * Sauvegarde une image QR code dans un fichier
     * @param content Le contenu à encoder dans le QR code
     * @param filePath Le chemin où sauvegarder le fichier
     * @throws RuntimeException si une erreur survient lors de la sauvegarde
     */
    void saveQRCodeImage(String content, String filePath) throws RuntimeException;
}