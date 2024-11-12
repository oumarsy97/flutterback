package sn.odc.flutter.Web.Controller.Interfaces;

import sn.odc.flutter.Datas.Entity.Transaction;
import sn.odc.flutter.Datas.Entity.Transfertprogramme;
import sn.odc.flutter.Web.Dtos.request.CreateTransfertprogrammeDTO;

import java.util.List;

public interface TransfertprogrammeControllerInterface {
    List<Transfertprogramme> getTransfertprogrammeBySender();
    Transfertprogramme createTransfertprogramme(CreateTransfertprogrammeDTO createTransfertprogrammeDTO);
    Transaction executeTransfertprogramme(Transfertprogramme transfertprogram);

}
