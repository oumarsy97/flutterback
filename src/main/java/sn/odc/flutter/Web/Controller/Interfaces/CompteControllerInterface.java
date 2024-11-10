package sn.odc.flutter.Web.Controller.Interfaces;

import org.springframework.http.ResponseEntity;
import sn.odc.flutter.Datas.Entity.Compte;
import sn.odc.flutter.Web.Dtos.response.GenericResponse;

public interface CompteControllerInterface {
    public ResponseEntity<GenericResponse<Compte>> getProfile();
}
