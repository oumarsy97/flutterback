package sn.odc.flutter.Services.Interfaces;

import sn.odc.flutter.Datas.Entity.Client;
import sn.odc.flutter.Web.Dtos.request.CreateClientDTO;

public interface ClientService extends BaseService<Client,Long> {
    // Add your custom methods here
    public Client createClient(CreateClientDTO createClient);
}
