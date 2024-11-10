package sn.odc.flutter.Web.Controller.Impl;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sn.odc.flutter.Datas.Entity.Client;
import sn.odc.flutter.Services.Interfaces.ClientService;
import sn.odc.flutter.Services.Interfaces.CompteService;
import sn.odc.flutter.Web.Controller.Interfaces.ClientControllerInterface;
import sn.odc.flutter.Web.Dtos.request.CreateClientDTO;
import sn.odc.flutter.Web.Dtos.response.GenericResponse;

@RestController
@RequestMapping("/clients")
public class ClientController extends BaseControllerImpl<Client,Long> implements ClientControllerInterface {
    private final CompteService compteService;
    private final ClientService clientService;

    public ClientController(ClientService clientService, CompteService compteService) {
        super(clientService);
        this.clientService = clientService;
        this.compteService = compteService;
    }

    @Override
    @PostMapping("/createClient")
    public ResponseEntity<GenericResponse<Client>> createClient(@RequestBody CreateClientDTO request) {
        try {
            Client createdClient = clientService.createClient(request);

            // Utiliser le constructeur approprié de GenericResponse
            GenericResponse<Client> response = new GenericResponse<>(
                    createdClient,
                    "Client créé avec succès"
            );

            // Le constructeur a déjà défini success=true, status="SUCCESS" et statusCode=200

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            // Gestion des erreurs
            GenericResponse<Client> errorResponse = new GenericResponse<>(e.getMessage());
            // Le constructeur a déjà défini success=false, status="ERROR" et statusCode=400

            return ResponseEntity.badRequest().body(errorResponse);
        }
    }

}