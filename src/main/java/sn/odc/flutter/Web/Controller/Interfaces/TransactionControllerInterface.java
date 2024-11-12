package sn.odc.flutter.Web.Controller.Interfaces;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import sn.odc.flutter.Datas.Entity.Transaction;
import sn.odc.flutter.Web.Dtos.request.CreateTransactionDTO;

import java.util.List;

public interface TransactionControllerInterface  {
    @PostMapping("/create")
    Transaction createTransaction(@RequestBody CreateTransactionDTO transaction);

    @PostMapping("/mytransactions")
    List<Transaction> getMytransactions();
}
