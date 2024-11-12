package sn.odc.flutter.Services.Interfaces;

import org.springframework.stereotype.Service;
import sn.odc.flutter.Datas.Entity.Transaction;
import sn.odc.flutter.Web.Dtos.request.CreateTransactionDTO;

import java.util.List;


@Service
public interface TransactionService extends BaseService<Transaction,Long> {

    Transaction createTransaction(CreateTransactionDTO entity);
    List<Transaction> getmyTransactions(String telephone);
}
