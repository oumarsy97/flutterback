package sn.odc.flutter.Services.Impl;

import org.springframework.stereotype.Service;
import sn.odc.flutter.Datas.Entity.Compte;
import sn.odc.flutter.Datas.Repository.Interfaces.CompteRepository;
import sn.odc.flutter.Services.Interfaces.CompteService;
import sn.odc.flutter.Web.Dtos.request.CompteDTO;

@Service
public class CompteServiceImpl extends BaseServiceImpl<Compte, Long> implements CompteService {
    private final CompteRepository compteRepository;

    public CompteServiceImpl(CompteRepository compteRepository) {
        super(compteRepository);
        this.compteRepository = compteRepository;
    }

    @Override
    public Compte createCompte(CompteDTO dto) {
        Compte compte = new Compte();


        return compteRepository.save(compte);
    }
}