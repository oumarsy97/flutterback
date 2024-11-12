package sn.odc.flutter.Datas.Repository.Interfaces;

import org.springframework.stereotype.Repository;
import sn.odc.flutter.Datas.Entity.Transaction;

import java.util.List;

@Repository
public interface TransactionRepository extends BaseInterface<Transaction,Long> {
    List<Transaction> findTransactionByReceiverTelephone(String telephone);
    List<Transaction> findTransactionBySenderTelephoneOrReceiverTelephone(String telephone, String telephone2);


}
