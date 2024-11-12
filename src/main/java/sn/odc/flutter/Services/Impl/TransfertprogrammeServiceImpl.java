package sn.odc.flutter.Services.Impl;

import org.springframework.stereotype.Service;
import sn.odc.flutter.Datas.Entity.Transaction;
import sn.odc.flutter.Datas.Entity.Transfertprogramme;
import sn.odc.flutter.Datas.Entity.TypeProgramme;
import sn.odc.flutter.Datas.Entity.statusProgramme;
import sn.odc.flutter.Datas.Repository.Interfaces.TransfertprogrammeRepository;
import sn.odc.flutter.Services.Interfaces.TransactionService;
import sn.odc.flutter.Services.Interfaces.TransfertprogrammeService;
import sn.odc.flutter.Web.Dtos.request.CreateTransactionDTO;
import sn.odc.flutter.Web.Dtos.request.CreateTransfertprogrammeDTO;

import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Service
public class TransfertprogrammeServiceImpl extends BaseServiceImpl<Transfertprogramme,Long> implements TransfertprogrammeService {
    private final TransfertprogrammeRepository repository;
    private final TransactionService transactionService;

    public TransfertprogrammeServiceImpl(TransfertprogrammeRepository repository, TransactionService transactionService) {
        super(repository);
        this.repository = repository;
        this.transactionService = transactionService;
    }

    @Override
    public List<Transfertprogramme> getTransfertprogrammeBySender(String telephone) {
        return repository.getTransfertprogrammeBySenderTelephone(telephone);
    }

    @Override
    public Transfertprogramme createTransfertprogramme(CreateTransfertprogrammeDTO dto) {
        if (dto == null) {
            throw new IllegalArgumentException("CreateTransfertprogrammeDTO cannot be null");
        }
        validateTransfertProgramme(dto);

        Transfertprogramme transfertprogramme = new Transfertprogramme();
        transfertprogramme.setSenderTelephone(dto.getSenderTelephone());
        transfertprogramme.setReceiverTelephone(dto.getReceiverTelephone());
        transfertprogramme.setMontant(dto.getMontant());
        transfertprogramme.setJourDuMois(dto.getJourDuMois());
        transfertprogramme.setHeure(dto.getHeure());
        transfertprogramme.setMinute(dto.getMinute());
        transfertprogramme.setType(dto.getType());
        transfertprogramme.setProchainExecutionDate(dto.getProchainExecutionDate());
        transfertprogramme.setStatus(statusProgramme.EN_ATTENTE);
        return repository.save(transfertprogramme);
    }

    @Override
    public Transaction executeTransfertprogramme(Transfertprogramme transfertprogramme) {
        if (transfertprogramme == null) {
            throw new IllegalArgumentException("Le transfert programmé ne peut pas être null");
        }

        validateTransfertProgrammeExecution(transfertprogramme);
        CreateTransactionDTO transactionDTO = new CreateTransactionDTO();
        transactionDTO.setType(Transaction.TransactionType.TRANSFERT);
        transactionDTO.setMontant(transfertprogramme.getMontant());
        transactionDTO.setSenderTelephone(transfertprogramme.getSenderTelephone());
        transactionDTO.setReceiverTelephone(transfertprogramme.getReceiverTelephone());


        // Créer la transaction via le service
        Transaction transaction = transactionService.createTransaction(transactionDTO);

        // Mettre à jour la prochaine date d'exécution
        LocalDateTime nextExecutionDate = calculateNextExecutionDate(
                transfertprogramme.getType(),
                transfertprogramme.getProchainExecutionDate()
        );
        transfertprogramme.setDernierExecutionDate(transfertprogramme.getProchainExecutionDate());
        transfertprogramme.setProchainExecutionDate(nextExecutionDate);
        repository.save(transfertprogramme);

        return transaction;
    }

    @Override
    public List<Transfertprogramme> findPendingTransfers() {
        return  repository.findByStatus(statusProgramme.EN_ATTENTE);
    }

    private void validateTransfertProgramme(CreateTransfertprogrammeDTO dto) {
        if (dto.getMontant() == null || dto.getMontant() <= 0) {
            throw new IllegalArgumentException("Le montant doit être positif");
        }
        if (dto.getSenderTelephone() == null || dto.getSenderTelephone().trim().isEmpty()) {
            throw new IllegalArgumentException("Le numéro de téléphone de l'expéditeur est requis");
        }
        if (dto.getReceiverTelephone() == null || dto.getReceiverTelephone().trim().isEmpty()) {
            throw new IllegalArgumentException("Le numéro de téléphone du destinataire est requis");
        }
        if (dto.getType() == null) {
            throw new IllegalArgumentException("Le type de programmation est requis");
        }

        // Validation spécifique selon le type
        switch (dto.getType()) {
            case MENSUELLE:
                if (dto.getJourDuMois() < 1 || dto.getJourDuMois() > 31) {
                    throw new IllegalArgumentException("Le jour du mois doit être entre 1 et 31");
                }
                break;
            case HEBDOMADAIRE:
                if (dto.getJourDuMois() < 1 || dto.getJourDuMois() > 7) {
                    throw new IllegalArgumentException("Le jour de la semaine doit être entre 1 (Lundi) et 7 (Dimanche)");
                }
                break;
        }

        if (dto.getHeure() < 0 || dto.getHeure() > 23) {
            throw new IllegalArgumentException("L'heure doit être entre 0 et 23");
        }
        if (dto.getMinute() < 0 || dto.getMinute() > 59) {
            throw new IllegalArgumentException("Les minutes doivent être entre 0 et 59");
        }
    }

    private void validateTransfertProgrammeExecution(Transfertprogramme transfertprogramme) {
        if (transfertprogramme.getProchainExecutionDate() == null) {
            throw new IllegalStateException("La date d'exécution n'est pas définie");
        }


    }

    private LocalDateTime calculateNextExecutionDate(TypeProgramme type, LocalDateTime lastExecutionDate) {
        switch (type) {
            case JOURNALIERE:
                return lastExecutionDate.plusDays(1);
            case HEBDOMADAIRE:
                int day = lastExecutionDate.getDayOfWeek().getValue(); // jour de la semaine (1-7, 1=Lundi)
                return lastExecutionDate.plusDays(8 - day); // Passer au prochain lundi
            case MENSUELLE:
                int dayOfMonth = lastExecutionDate.getDayOfMonth();
                int monthsToAdd = 1;
                if (dayOfMonth > 28) { // Pour éviter les problèmes de jours manquants (ex: 31 janvier -> 28 février)
                    monthsToAdd = 2;
                    dayOfMonth = 1;
                }
                return lastExecutionDate.plusMonths(monthsToAdd).withDayOfMonth(dayOfMonth);
            default:
                throw new IllegalArgumentException("Type de programme inconnu : " + type);
        }
    }

}