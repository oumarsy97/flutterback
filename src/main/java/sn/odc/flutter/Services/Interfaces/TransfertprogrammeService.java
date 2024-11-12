package sn.odc.flutter.Services.Interfaces;

import sn.odc.flutter.Datas.Entity.Transaction;
import sn.odc.flutter.Datas.Entity.Transfertprogramme;
import sn.odc.flutter.Web.Dtos.request.CreateTransfertprogrammeDTO;

import java.util.List;

public interface TransfertprogrammeService extends BaseService<Transfertprogramme,Long> {
   List<Transfertprogramme> getTransfertprogrammeBySender(String telephone);
    Transfertprogramme createTransfertprogramme(CreateTransfertprogrammeDTO createTransfertprogrammeDTO);
   Transaction executeTransfertprogramme(Transfertprogramme transfertprogram);
    List<Transfertprogramme> findPendingTransfers();
}
