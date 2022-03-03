package org.amawal.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Dialecte.
 */
@Entity
@Table(name = "dialecte")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Dialecte implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "dialecte_id")
    private Long dialecteId;

    @Column(name = "dialecte")
    private String dialecte;

    @ManyToMany
    @JoinTable(
        name = "rel_dialecte__region",
        joinColumns = @JoinColumn(name = "dialecte_id"),
        inverseJoinColumns = @JoinColumn(name = "region_id")
    )
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "dialectes" }, allowSetters = true)
    private Set<Region> regions = new HashSet<>();

    @ManyToMany(mappedBy = "dialectes")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "wordInfo", "traductions", "wordType", "theme", "dialectes", "tamazightToLang" }, allowSetters = true)
    private Set<AmawalWord> words = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Dialecte id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getDialecteId() {
        return this.dialecteId;
    }

    public Dialecte dialecteId(Long dialecteId) {
        this.setDialecteId(dialecteId);
        return this;
    }

    public void setDialecteId(Long dialecteId) {
        this.dialecteId = dialecteId;
    }

    public String getDialecte() {
        return this.dialecte;
    }

    public Dialecte dialecte(String dialecte) {
        this.setDialecte(dialecte);
        return this;
    }

    public void setDialecte(String dialecte) {
        this.dialecte = dialecte;
    }

    public Set<Region> getRegions() {
        return this.regions;
    }

    public void setRegions(Set<Region> regions) {
        this.regions = regions;
    }

    public Dialecte regions(Set<Region> regions) {
        this.setRegions(regions);
        return this;
    }

    public Dialecte addRegion(Region region) {
        this.regions.add(region);
        region.getDialectes().add(this);
        return this;
    }

    public Dialecte removeRegion(Region region) {
        this.regions.remove(region);
        region.getDialectes().remove(this);
        return this;
    }

    public Set<AmawalWord> getWords() {
        return this.words;
    }

    public void setWords(Set<AmawalWord> amawalWords) {
        if (this.words != null) {
            this.words.forEach(i -> i.removeDialecte(this));
        }
        if (amawalWords != null) {
            amawalWords.forEach(i -> i.addDialecte(this));
        }
        this.words = amawalWords;
    }

    public Dialecte words(Set<AmawalWord> amawalWords) {
        this.setWords(amawalWords);
        return this;
    }

    public Dialecte addWord(AmawalWord amawalWord) {
        this.words.add(amawalWord);
        amawalWord.getDialectes().add(this);
        return this;
    }

    public Dialecte removeWord(AmawalWord amawalWord) {
        this.words.remove(amawalWord);
        amawalWord.getDialectes().remove(this);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Dialecte)) {
            return false;
        }
        return id != null && id.equals(((Dialecte) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Dialecte{" +
            "id=" + getId() +
            ", dialecteId=" + getDialecteId() +
            ", dialecte='" + getDialecte() + "'" +
            "}";
    }
}
