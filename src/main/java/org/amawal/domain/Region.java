package org.amawal.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Region.
 */
@Entity
@Table(name = "region")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Region implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "regin_id")
    private Long reginId;

    @Column(name = "region_nom")
    private String regionNom;

    @Column(name = "pays")
    private String pays;

    @ManyToMany(mappedBy = "regions")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "regions", "words" }, allowSetters = true)
    private Set<Dialecte> dialectes = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Region id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getReginId() {
        return this.reginId;
    }

    public Region reginId(Long reginId) {
        this.setReginId(reginId);
        return this;
    }

    public void setReginId(Long reginId) {
        this.reginId = reginId;
    }

    public String getRegionNom() {
        return this.regionNom;
    }

    public Region regionNom(String regionNom) {
        this.setRegionNom(regionNom);
        return this;
    }

    public void setRegionNom(String regionNom) {
        this.regionNom = regionNom;
    }

    public String getPays() {
        return this.pays;
    }

    public Region pays(String pays) {
        this.setPays(pays);
        return this;
    }

    public void setPays(String pays) {
        this.pays = pays;
    }

    public Set<Dialecte> getDialectes() {
        return this.dialectes;
    }

    public void setDialectes(Set<Dialecte> dialectes) {
        if (this.dialectes != null) {
            this.dialectes.forEach(i -> i.removeRegion(this));
        }
        if (dialectes != null) {
            dialectes.forEach(i -> i.addRegion(this));
        }
        this.dialectes = dialectes;
    }

    public Region dialectes(Set<Dialecte> dialectes) {
        this.setDialectes(dialectes);
        return this;
    }

    public Region addDialecte(Dialecte dialecte) {
        this.dialectes.add(dialecte);
        dialecte.getRegions().add(this);
        return this;
    }

    public Region removeDialecte(Dialecte dialecte) {
        this.dialectes.remove(dialecte);
        dialecte.getRegions().remove(this);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Region)) {
            return false;
        }
        return id != null && id.equals(((Region) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Region{" +
            "id=" + getId() +
            ", reginId=" + getReginId() +
            ", regionNom='" + getRegionNom() + "'" +
            ", pays='" + getPays() + "'" +
            "}";
    }
}
