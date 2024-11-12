package sn.odc.flutter.Datas.Entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "comptes")
@NoArgsConstructor
@AllArgsConstructor
public class Compte extends BaseEntity implements UserDetails {

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private TypeCompte type = TypeCompte.CLIENT;

    @Column(unique = true, nullable = false, length = 20)
    private String telephone;

    @Column(length = 100)
    private String email;

    @Column(nullable = false, length = 255)
    private String password;

    private float montant =  0;

    @Column(columnDefinition = "bytea")
    private byte[] qrcode;

    private Boolean estVerifie = false;

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private Statut statut = Statut.INACTIF;

    private float limiteMensuelle = 1000000;

    @Column(length = 20)
    private String reference;

    @JsonIgnoreProperties("compte")
    @OneToOne(mappedBy = "compte")
    private Client client;

    public enum Statut {
        ACTIF,
        INACTIF,
        BLOQUE
    }

    public enum TypeCompte {
        CLIENT,
        ADMIN,
        AGENT,
        Distributeur
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return type != null ?
                List.of(new SimpleGrantedAuthority(type.name())) :
                List.of();
    }

    @Override
    public String getUsername() {
        return telephone;
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
    // Dans Compte.java

}