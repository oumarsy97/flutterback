package sn.odc.flutter.Web.Dtos.request;

import lombok.Data;

@Data
public class CreateClientDTO {
    private String nom;
    private String prenom;
    private String telephone;
    private String  email;
    private String password;
    private String confirmCode;

}
