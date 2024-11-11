package sn.odc.flutter.Web.Controller.Interfaces;

import org.springframework.http.ResponseEntity;
import sn.odc.flutter.Datas.Entity.Compte;
import sn.odc.flutter.Web.Dtos.response.DataResponse;
import sn.odc.flutter.Web.Dtos.response.GenericResponse;
import sn.odc.flutter.Web.Dtos.response.LoginResponse;

public interface CompteControllerInterface {
    public ResponseEntity<DataResponse> getProfile();
}
