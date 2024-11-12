package sn.odc.flutter.Datas.Repository.Interfaces;

import org.springframework.stereotype.Repository;
import sn.odc.flutter.Datas.Entity.Transfertprogramme;
import sn.odc.flutter.Datas.Entity.statusProgramme;

import java.util.List;

@Repository
public interface TransfertprogrammeRepository extends BaseInterface<Transfertprogramme,Long> {
    public List<Transfertprogramme> getTransfertprogrammeBySenderTelephone(String telephone);
    public List<Transfertprogramme> findByStatus(statusProgramme status);
}
