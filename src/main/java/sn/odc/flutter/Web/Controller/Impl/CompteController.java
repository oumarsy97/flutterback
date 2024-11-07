package sn.odc.flutter.Web.Controller.Impl;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import sn.odc.flutter.Datas.Entity.Compte;
import sn.odc.flutter.Services.Interfaces.BaseService;
import sn.odc.flutter.Services.Interfaces.CompteService;
import sn.odc.flutter.Web.Controller.Interfaces.CompteControllerInterface;

@RestController
@RequestMapping("/comptes")
public class CompteController extends BaseControllerImpl<Compte,Long> implements CompteControllerInterface {


    private final CompteService compteService;

    @Autowired
    public CompteController(CompteService compteService) {
        super(compteService);
        this.compteService = compteService;
    }
}

