package sn.odc.oumar.springproject.Web.Dtos.Response.impl;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginResponse {
    private String token;

    private long expiresIn;


}