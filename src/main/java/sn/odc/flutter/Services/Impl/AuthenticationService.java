package sn.odc.flutter.Services.Impl;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import sn.odc.flutter.Datas.Entity.Compte;
import sn.odc.flutter.Datas.Repository.Interfaces.CompteRepository;
import sn.odc.flutter.Web.Dtos.request.CompteDTO;
import sn.odc.flutter.Web.Dtos.request.LoginUserDto;
import org.springframework.security.authentication.BadCredentialsException;

@Service
public class AuthenticationService {
    private final CompteRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    public AuthenticationService(
            CompteRepository userRepository,
            AuthenticationManager authenticationManager,
            PasswordEncoder passwordEncoder
    ) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public Compte signup(CompteDTO input) {
        // Vérifier si l'email existe déjà
        if (userRepository.findCompteByTelephone(input.getEmail()).isPresent()) {
            throw new RuntimeException("Email already exists");
        }

        Compte compte = new Compte();
        compte.setEmail(input.getEmail());
        compte.setTelephone(input.getTelephone());
        compte.setPassword(passwordEncoder.encode(input.getPassword()));
        return userRepository.save(compte);
    }

    public Compte authenticate(LoginUserDto input) {
        try {
            // Vérifier si l'email existe
            Compte compte = userRepository.findCompteByTelephone(input.getTelephone())
                    .orElseThrow(() -> new BadCredentialsException("User not found"));

            // Vérifier si le mot de passe correspond
            if (!passwordEncoder.matches(input.getPassword(), compte.getPassword())) {
                throw new BadCredentialsException("Invalid password");
            }

            // Authentifier
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            input.getTelephone(),
                            input.getPassword()
                    )
            );


            return compte;
        } catch (BadCredentialsException e) {
            throw new BadCredentialsException("Authentication failed: " + e.getMessage());
        }
    }
}