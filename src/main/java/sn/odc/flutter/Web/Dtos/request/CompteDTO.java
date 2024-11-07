package sn.odc.flutter.Web.Dtos.request;

package sn.odc.flutter.Datas.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import sn.odc.flutter.Datas.Entity.Compte;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CompteDTO {
    private Long id;
    private Compte.TypeCompte type = Compte.TypeCompte.CLIENT;
    private String telephone;
    private String email;
    private String code;
    private int montant = 0;
    private String qrcode;
    private Boolean estVerifie = false;
    private Compte.Statut statut = Compte.Statut.INACTIF;
    private int limiteMensuelle = 1000000;
}