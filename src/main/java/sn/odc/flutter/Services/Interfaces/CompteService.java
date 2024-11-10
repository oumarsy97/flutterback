package sn.odc.flutter.Services.Interfaces;

import sn.odc.flutter.Datas.Entity.Compte;

public interface CompteService extends BaseService<Compte,Long> {
    boolean existsByEmail(String email);
    boolean existsByTelephone(String telephone);
    // Add your custom methods here
     public Compte createCompte(Compte dto);
     public Compte findCompteByToken(String token);
}
