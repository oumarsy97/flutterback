package sn.odc.flutter.Services.Impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sn.odc.flutter.Datas.Entity.Compte;
import sn.odc.flutter.Datas.Entity.Transaction;
import sn.odc.flutter.Datas.Repository.Interfaces.CompteRepository;
import sn.odc.flutter.Datas.Repository.Interfaces.TransactionRepository;
import sn.odc.flutter.Services.Interfaces.TransactionService;
import sn.odc.flutter.Web.Dtos.request.CreateTransactionDTO;
import java.util.List;

@Service
public class TransactionServiceImpl extends BaseServiceImpl<Transaction,Long> implements TransactionService {
    private final TransactionRepository transactionRepo;
    private final CompteRepository compteRepository;

    public TransactionServiceImpl(TransactionRepository repository, CompteRepository compteRepository) {
        super(repository);
        this.transactionRepo = repository;
        this.compteRepository = compteRepository;
    }

    @Override
    @Transactional
    public Transaction createTransaction(CreateTransactionDTO dto) {
        // 1. Récupérer les comptes
        Compte compteSender = null;
        Compte compteReceiver = compteRepository.findCompteByTelephone(dto.getReceiverTelephone())
                .orElseThrow(() -> new RuntimeException("Compte destinataire non trouvé"));

        // Vérifier si c'est un dépôt (pas besoin de compte émetteur)
        if (dto.getType() != Transaction.TransactionType.DEPOT) {
            compteSender = compteRepository.findCompteByTelephone(dto.getSenderTelephone())
                    .orElseThrow(() -> new RuntimeException("Compte émetteur non trouvé"));

            // Vérifier le solde pour retrait ou transfert
            if (compteSender.getMontant() < dto.getMontant()) {
                throw new RuntimeException("Montant insuffisant pour effectuer l'opération");
            }
        }

        // 2. Créer la transaction
        Transaction transaction = new Transaction();
        transaction.setType(dto.getType());
        transaction.setMontant(dto.getMontant());
        transaction.setSenderTelephone(dto.getSenderTelephone());
        transaction.setReceiverTelephone(dto.getReceiverTelephone());
        transaction.setDistributeurTelephone(dto.getDistributeurTelephone());

        // 3. Mettre à jour les soldes selon le type de transaction
        switch (dto.getType()) {
            case DEPOT:
                compteReceiver.setMontant(compteReceiver.getMontant() + dto.getMontant());
                compteRepository.save(compteReceiver);
                break;

            case RETRAIT:
                compteSender.setMontant(compteSender.getMontant() - dto.getMontant());
                compteRepository.save(compteSender);
                break;

            case TRANSFERT:
                compteSender.setMontant(compteSender.getMontant() - dto.getMontant());
                compteReceiver.setMontant(compteReceiver.getMontant() + dto.getMontant());
                compteRepository.save(compteSender);
                compteRepository.save(compteReceiver);
                break;
        }

        // 4. Sauvegarder la transaction
        return transactionRepo.save(transaction);
    }



    @Override
    public List<Transaction> getmyTransactions(String phone) {
        return this.transactionRepo.findTransactionBySenderTelephoneOrReceiverTelephone(phone,phone);
    }

}