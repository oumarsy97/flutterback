package sn.odc.flutter.Services.Impl;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import sn.odc.flutter.Datas.Entity.Compte;
import sn.odc.flutter.Datas.Repository.Interfaces.CompteRepository;

import java.util.HashSet;
import java.util.Set;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final CompteRepository compteRepository;

    public CustomUserDetailsService(CompteRepository compteRepository) {
        this.compteRepository = compteRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Compte compte = compteRepository.findCompteByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("Compte not found"));

        Set<GrantedAuthority> authorities = new HashSet<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_" + compte.getType()));

        return new org.springframework.security.core.userdetails.User(
                compte.getEmail(),
                compte.getPassword(),
                authorities
        );
    }
}