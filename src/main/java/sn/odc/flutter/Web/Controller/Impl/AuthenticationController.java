package sn.odc.flutter.Web.Controller.Impl;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sn.odc.flutter.Datas.Entity.Compte;
import sn.odc.flutter.Services.Impl.AuthenticationService;
import sn.odc.flutter.Services.Impl.JwtService;
import sn.odc.flutter.Web.Dtos.request.CompteDTO;
import sn.odc.flutter.Web.Dtos.request.LoginUserDto;
import sn.odc.flutter.Web.Dtos.response.LoginResponse;

@RequestMapping("/auth")
@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE, RequestMethod.OPTIONS})
@Tag(name = "Auth ", description = "API pour gérer les users")

public class AuthenticationController {
    private final JwtService jwtService;

    private final AuthenticationService authenticationService;

    public AuthenticationController(JwtService jwtService, AuthenticationService authenticationService) {
        this.jwtService = jwtService;
        this.authenticationService = authenticationService;
    }

    @PostMapping("/signup")
    public ResponseEntity<Compte> register(@RequestBody CompteDTO registerUserDto) {
        Compte registeredCompte = authenticationService.signup(registerUserDto);

        return ResponseEntity.ok(registeredCompte);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> authenticate(@RequestBody LoginUserDto loginUserDto) {
        Compte authenticatedUser = authenticationService.authenticate(loginUserDto);

        String jwtToken = jwtService.generateToken(authenticatedUser);

        LoginResponse loginResponse = new LoginResponse();
        loginResponse.setToken(jwtToken);

        return ResponseEntity.ok(loginResponse);
    }
}