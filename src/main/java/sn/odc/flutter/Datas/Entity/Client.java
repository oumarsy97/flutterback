package sn.odc.flutter.Datas.Entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "clients")
@NoArgsConstructor
@AllArgsConstructor
public class Client extends BaseEntity {
    @Column(name = "nom")
    private String nom;
    @Column(name = "prenom")
    private String prenom;


    // Relation One-To-One avec Compte
    @JsonIgnoreProperties("client") // Ignore seulement le champ client du compte
      @OneToOne(cascade = CascadeType.ALL)
      @JoinColumn(name = "compte_id", referencedColumnName = "id")
     private Compte compte;




}
