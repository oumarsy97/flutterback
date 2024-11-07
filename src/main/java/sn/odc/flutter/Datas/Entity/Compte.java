package sn.odc.flutter.Datas.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "comptes")
@NoArgsConstructor
@AllArgsConstructor
public class Compte extends BaseEntity implements UserDetails {
    public Collection<? extends GrantedAuthority> getAuthorities() {

        return type != null ?
                List.of(new SimpleGrantedAuthority(type.name())) :
                List.of();
    }

    @Override
    public String getUsername() {
        return email;
    }




    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }


    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }



    public enum Statut {
        ACTIF,
        INACTIF,
        BLOQUE
    }

    public enum TypeCompte {
        CLIENT,
        ADMIN,
        AGENT,
        Distributeur;
    }

    @Enumerated(EnumType.STRING)
    private TypeCompte type = TypeCompte.CLIENT;

    @Column(unique = true, nullable = false)
    private String telephone;

    private String email;

    @Column(nullable = false)
    private String password;

    private int montant = 0;

    private String qrcode;

    private Boolean estVerifie = false;

    @Enumerated(EnumType.STRING)
    private Statut statut = Statut.INACTIF;

    private int limiteMensuelle = 1000000;

}