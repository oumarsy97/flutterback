package sn.odc.flutter.Datas.Entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.hibernate.resource.transaction.spi.TransactionStatus;

import java.util.Date;

@Data
@Entity
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)

public class Transaction extends BaseEntity {

    public enum TransactionType {
        DEPOT,
        RETRAIT,
        TRANSFERT
    }


    @Column(nullable = false)
    private TransactionType type =  TransactionType.DEPOT;

    @Column(nullable = false)
    private Float montant;

    @Column(nullable = true)
    private String senderTelephone;

    private String receiverTelephone;

    private statusProgramme status =statusProgramme.VALIDE;

    @Column(nullable = true)
    private String distributeurTelephone;

    private boolean annulee = false;
}