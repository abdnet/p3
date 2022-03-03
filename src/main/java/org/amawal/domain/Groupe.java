package org.amawal.domain;

import java.io.Serializable;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Groupe.
 */
@Entity
@Table(name = "groupe")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Groupe implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "groupe_id")
    private Long groupeId;

    @Column(name = "groupe_code")
    private String groupeCode;

    @Column(name = "habilitation_level")
    private Integer habilitationLevel;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Groupe id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getGroupeId() {
        return this.groupeId;
    }

    public Groupe groupeId(Long groupeId) {
        this.setGroupeId(groupeId);
        return this;
    }

    public void setGroupeId(Long groupeId) {
        this.groupeId = groupeId;
    }

    public String getGroupeCode() {
        return this.groupeCode;
    }

    public Groupe groupeCode(String groupeCode) {
        this.setGroupeCode(groupeCode);
        return this;
    }

    public void setGroupeCode(String groupeCode) {
        this.groupeCode = groupeCode;
    }

    public Integer getHabilitationLevel() {
        return this.habilitationLevel;
    }

    public Groupe habilitationLevel(Integer habilitationLevel) {
        this.setHabilitationLevel(habilitationLevel);
        return this;
    }

    public void setHabilitationLevel(Integer habilitationLevel) {
        this.habilitationLevel = habilitationLevel;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Groupe)) {
            return false;
        }
        return id != null && id.equals(((Groupe) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Groupe{" +
            "id=" + getId() +
            ", groupeId=" + getGroupeId() +
            ", groupeCode='" + getGroupeCode() + "'" +
            ", habilitationLevel=" + getHabilitationLevel() +
            "}";
    }
}
