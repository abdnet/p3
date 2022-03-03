package org.amawal.domain;

import java.io.Serializable;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Langue.
 */
@Entity
@Table(name = "langue")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Langue implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "langue_id")
    private Long langueId;

    @Column(name = "langue")
    private String langue;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Langue id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getLangueId() {
        return this.langueId;
    }

    public Langue langueId(Long langueId) {
        this.setLangueId(langueId);
        return this;
    }

    public void setLangueId(Long langueId) {
        this.langueId = langueId;
    }

    public String getLangue() {
        return this.langue;
    }

    public Langue langue(String langue) {
        this.setLangue(langue);
        return this;
    }

    public void setLangue(String langue) {
        this.langue = langue;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Langue)) {
            return false;
        }
        return id != null && id.equals(((Langue) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Langue{" +
            "id=" + getId() +
            ", langueId=" + getLangueId() +
            ", langue='" + getLangue() + "'" +
            "}";
    }
}
