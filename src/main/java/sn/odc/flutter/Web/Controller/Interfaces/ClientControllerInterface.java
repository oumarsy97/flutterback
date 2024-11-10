package sn.odc.flutter.Web.Controller.Interfaces;

import org.springframework.http.ResponseEntity;
import sn.odc.flutter.Datas.Entity.Client;
import sn.odc.flutter.Web.Dtos.request.CreateClientDTO;
import sn.odc.flutter.Web.Dtos.response.GenericResponse;

public interface ClientControllerInterface  {
    public ResponseEntity<GenericResponse<Client>> createClient(CreateClientDTO request);
}
