package sn.odc.flutter.Datas.Entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.Data;
import lombok.ToString;
import java.time.LocalDateTime;

@Data
@Entity
@ToString
public class Transfertprogramme extends BaseEntity {
    @Column(nullable = false)
    private statusProgramme status = statusProgramme.EN_ATTENTE;

    @Column(nullable = false)
    private Float montant;

    @Column(nullable = true)
    private String senderTelephone;

    private String receiverTelephone;

    @Column(nullable = false)
    private LocalDateTime prochainExecutionDate;

    private int jourDuMois;
    private int heure;
    private int minute;

    private TypeProgramme type = TypeProgramme.JOURNALIERE;

    public void setDernierExecutionDate(LocalDateTime prochainExecutionDate) {
        this.prochainExecutionDate = prochainExecutionDate;
        this.jourDuMois = prochainExecutionDate.getDayOfMonth();
        this.heure = prochainExecutionDate.getHour();
        this.minute = prochainExecutionDate.getMinute();

    }
}