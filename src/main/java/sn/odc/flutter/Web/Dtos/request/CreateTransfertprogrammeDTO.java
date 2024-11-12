package sn.odc.flutter.Web.Dtos.request;

import lombok.Data;
import sn.odc.flutter.Datas.Entity.Transfertprogramme;
import sn.odc.flutter.Datas.Entity.TypeProgramme;

import java.time.LocalDateTime;
import java.util.Date;

@Data
public class CreateTransfertprogrammeDTO {
    private String senderTelephone;
    private String receiverTelephone;
    private Float montant;
    private int jourDuMois;
    private int heure;
    private int minute;
    private TypeProgramme type;
    private LocalDateTime lastExecutionDate;
    private LocalDateTime prochainExecutionDate;
}
