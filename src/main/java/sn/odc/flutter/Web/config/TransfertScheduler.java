package sn.odc.flutter.Web.config;


import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import sn.odc.flutter.Datas.Entity.Transfertprogramme;
import sn.odc.flutter.Services.Interfaces.TransfertprogrammeService;

import java.time.LocalDateTime;
import java.util.List;

@Component
@EnableScheduling
public class TransfertScheduler {

    private final TransfertprogrammeService transfertprogrammeService;

    public TransfertScheduler(TransfertprogrammeService transfertprogrammeService) {
        this.transfertprogrammeService = transfertprogrammeService;
    }

    @Scheduled(fixedRate = 60000) // Vérifie toutes les minutes
    public void executeScheduledTransfers() {
        // Récupérer tous les transferts programmés non exécutés dont la date est arrivée
        List<Transfertprogramme> transfertsAExecuter = transfertprogrammeService.findPendingTransfers();

        for (Transfertprogramme transfert : transfertsAExecuter) {
            if (shouldExecuteTransfer(transfert)) {
                try {
                    transfertprogrammeService.executeTransfertprogramme(transfert);
                } catch (Exception e) {
                    // Gérer l'erreur et logger
                    System.err.println("Erreur lors de l'exécution du transfert " + transfert.getId() + ": " + e.getMessage());
                }
            }
        }
    }

    private boolean shouldExecuteTransfer(Transfertprogramme transfert) {
        LocalDateTime now = LocalDateTime.now();
        return transfert.getProchainExecutionDate().isBefore(now) || transfert.getProchainExecutionDate().isEqual(now);
    }
}