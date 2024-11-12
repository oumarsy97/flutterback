package sn.odc.flutter.Web.Dtos.request;

import jakarta.persistence.Column;
import lombok.Data;
import sn.odc.flutter.Datas.Entity.Transaction;

@Data
public class CreateTransactionDTO {
    private Transaction.TransactionType type =  Transaction.TransactionType.DEPOT;

    private Float montant;

    private String senderTelephone;

    private String receiverTelephone;

    private String distributeurTelephone;

}
