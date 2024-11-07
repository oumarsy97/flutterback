package sn.odc.flutter.Datas.Repository.Interfaces;

import org.springframework.stereotype.Repository;
import sn.odc.flutter.Datas.Entity.Compte;

import java.util.Optional;

@Repository
public interface CompteRepository extends BaseInterface<Compte,Long> {
   Optional<Compte> findCompteByTelephone(String telephone);
   Optional<Compte> findCompteByEmail(String email);
}
