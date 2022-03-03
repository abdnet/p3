package org.amawal.domain;

import java.io.Serializable;
import java.time.Instant;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A WordInfo.
 */
@Entity
@Table(name = "word_info")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class WordInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "word_info_id")
    private Long wordInfoId;

    @Column(name = "contribution_date")
    private Instant contributionDate;

    @Column(name = "validation_date")
    private Instant validationDate;

    @Column(name = "etat")
    private Integer etat;

    @ManyToOne
    private Utilisateur contributeur;

    @ManyToOne
    private Utilisateur validateur;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public WordInfo id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getWordInfoId() {
        return this.wordInfoId;
    }

    public WordInfo wordInfoId(Long wordInfoId) {
        this.setWordInfoId(wordInfoId);
        return this;
    }

    public void setWordInfoId(Long wordInfoId) {
        this.wordInfoId = wordInfoId;
    }

    public Instant getContributionDate() {
        return this.contributionDate;
    }

    public WordInfo contributionDate(Instant contributionDate) {
        this.setContributionDate(contributionDate);
        return this;
    }

    public void setContributionDate(Instant contributionDate) {
        this.contributionDate = contributionDate;
    }

    public Instant getValidationDate() {
        return this.validationDate;
    }

    public WordInfo validationDate(Instant validationDate) {
        this.setValidationDate(validationDate);
        return this;
    }

    public void setValidationDate(Instant validationDate) {
        this.validationDate = validationDate;
    }

    public Integer getEtat() {
        return this.etat;
    }

    public WordInfo etat(Integer etat) {
        this.setEtat(etat);
        return this;
    }

    public void setEtat(Integer etat) {
        this.etat = etat;
    }

    public Utilisateur getContributeur() {
        return this.contributeur;
    }

    public void setContributeur(Utilisateur utilisateur) {
        this.contributeur = utilisateur;
    }

    public WordInfo contributeur(Utilisateur utilisateur) {
        this.setContributeur(utilisateur);
        return this;
    }

    public Utilisateur getValidateur() {
        return this.validateur;
    }

    public void setValidateur(Utilisateur utilisateur) {
        this.validateur = utilisateur;
    }

    public WordInfo validateur(Utilisateur utilisateur) {
        this.setValidateur(utilisateur);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof WordInfo)) {
            return false;
        }
        return id != null && id.equals(((WordInfo) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "WordInfo{" +
            "id=" + getId() +
            ", wordInfoId=" + getWordInfoId() +
            ", contributionDate='" + getContributionDate() + "'" +
            ", validationDate='" + getValidationDate() + "'" +
            ", etat=" + getEtat() +
            "}";
    }
}
