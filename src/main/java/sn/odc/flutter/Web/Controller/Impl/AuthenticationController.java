package sn.odc.oumar.springproject.Web.Controller.Impl;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import sn.odc.oumar.springproject.Datas.Entity.User;
import sn.odc.oumar.springproject.Services.Impl.AuthenticationService;
import sn.odc.oumar.springproject.Services.Impl.JwtService;
import sn.odc.oumar.springproject.Web.Dtos.Request.LoginUserDto;
import sn.odc.oumar.springproject.Web.Dtos.Request.RegisterUserDto;
import sn.odc.oumar.springproject.Web.Dtos.Response.impl.LoginResponse;

@RequestMapping("/auth")
@RestController
@Tag(name = "Auth ", description = "API pour gérer les users")

public class AuthenticationController {
    private final JwtService jwtService;

    private final AuthenticationService authenticationService;

    public AuthenticationController(JwtService jwtService, AuthenticationService authenticationService) {
        this.jwtService = jwtService;
        this.authenticationService = authenticationService;
    }

    @PostMapping("/signup")
    public ResponseEntity<User> register(@RequestBody RegisterUserDto registerUserDto) {
        User registeredUser = authenticationService.signup(registerUserDto);

        return ResponseEntity.ok(registeredUser);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> authenticate(@RequestBody LoginUserDto loginUserDto) {
        User authenticatedUser = authenticationService.authenticate(loginUserDto);

        String jwtToken = jwtService.generateToken(authenticatedUser);

        LoginResponse loginResponse = new LoginResponse();
        loginResponse.setToken(jwtToken);


        return ResponseEntity.ok(loginResponse);
    }
}