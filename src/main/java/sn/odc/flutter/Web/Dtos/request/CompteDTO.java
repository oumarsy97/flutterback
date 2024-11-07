package sn.odc.flutter.Web.Dtos.request;

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
    private String password;
    private Compte.Statut statut = Compte.Statut.INACTIF;
}