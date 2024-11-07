package sn.odc.flutter.Services.Interfaces;

import sn.odc.flutter.Datas.Entity.Compte;
import sn.odc.flutter.Web.Dtos.request.CompteDTO;

public interface CompteService extends BaseService<Compte,Long> {
    // Add your custom methods here
     public Compte createCompte(CompteDTO dto);
}
