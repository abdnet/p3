package org.amawal.domain;

import java.io.Serializable;
import java.time.Instant;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Utilisateur.
 */
@Entity
@Table(name = "utilisateur")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Utilisateur implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "utilisateur_id")
    private Long utilisateurId;

    @Column(name = "nom")
    private String nom;

    @Column(name = "prenom")
    private String prenom;

    @Column(name = "adresse_email")
    private String adresseEmail;

    @Column(name = "avatar")
    private String avatar;

    @Column(name = "date_inscription")
    private Instant dateInscription;

    @Column(name = "valid_profil")
    private Boolean validProfil;

    @Column(name = "activate_profil")
    private Boolean activateProfil;

    @Column(name = "genre")
    private String genre;

    @Column(name = "password")
    private String password;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Utilisateur id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUtilisateurId() {
        return this.utilisateurId;
    }

    public Utilisateur utilisateurId(Long utilisateurId) {
        this.setUtilisateurId(utilisateurId);
        return this;
    }

    public void setUtilisateurId(Long utilisateurId) {
        this.utilisateurId = utilisateurId;
    }

    public String getNom() {
        return this.nom;
    }

    public Utilisateur nom(String nom) {
        this.setNom(nom);
        return this;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return this.prenom;
    }

    public Utilisateur prenom(String prenom) {
        this.setPrenom(prenom);
        return this;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getAdresseEmail() {
        return this.adresseEmail;
    }

    public Utilisateur adresseEmail(String adresseEmail) {
        this.setAdresseEmail(adresseEmail);
        return this;
    }

    public void setAdresseEmail(String adresseEmail) {
        this.adresseEmail = adresseEmail;
    }

    public String getAvatar() {
        return this.avatar;
    }

    public Utilisateur avatar(String avatar) {
        this.setAvatar(avatar);
        return this;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public Instant getDateInscription() {
        return this.dateInscription;
    }

    public Utilisateur dateInscription(Instant dateInscription) {
        this.setDateInscription(dateInscription);
        return this;
    }

    public void setDateInscription(Instant dateInscription) {
        this.dateInscription = dateInscription;
    }

    public Boolean getValidProfil() {
        return this.validProfil;
    }

    public Utilisateur validProfil(Boolean validProfil) {
        this.setValidProfil(validProfil);
        return this;
    }

    public void setValidProfil(Boolean validProfil) {
        this.validProfil = validProfil;
    }

    public Boolean getActivateProfil() {
        return this.activateProfil;
    }

    public Utilisateur activateProfil(Boolean activateProfil) {
        this.setActivateProfil(activateProfil);
        return this;
    }

    public void setActivateProfil(Boolean activateProfil) {
        this.activateProfil = activateProfil;
    }

    public String getGenre() {
        return this.genre;
    }

    public Utilisateur genre(String genre) {
        this.setGenre(genre);
        return this;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String getPassword() {
        return this.password;
    }

    public Utilisateur password(String password) {
        this.setPassword(password);
        return this;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Utilisateur)) {
            return false;
        }
        return id != null && id.equals(((Utilisateur) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Utilisateur{" +
            "id=" + getId() +
            ", utilisateurId=" + getUtilisateurId() +
            ", nom='" + getNom() + "'" +
            ", prenom='" + getPrenom() + "'" +
            ", adresseEmail='" + getAdresseEmail() + "'" +
            ", avatar='" + getAvatar() + "'" +
            ", dateInscription='" + getDateInscription() + "'" +
            ", validProfil='" + getValidProfil() + "'" +
            ", activateProfil='" + getActivateProfil() + "'" +
            ", genre='" + getGenre() + "'" +
            ", password='" + getPassword() + "'" +
            "}";
    }
}
