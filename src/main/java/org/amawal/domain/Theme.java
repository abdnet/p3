package org.amawal.domain;

import java.io.Serializable;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Theme.
 */
@Entity
@Table(name = "theme")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Theme implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "theme_id")
    private Long themeId;

    @Column(name = "theme")
    private String theme;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Theme id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getThemeId() {
        return this.themeId;
    }

    public Theme themeId(Long themeId) {
        this.setThemeId(themeId);
        return this;
    }

    public void setThemeId(Long themeId) {
        this.themeId = themeId;
    }

    public String getTheme() {
        return this.theme;
    }

    public Theme theme(String theme) {
        this.setTheme(theme);
        return this;
    }

    public void setTheme(String theme) {
        this.theme = theme;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Theme)) {
            return false;
        }
        return id != null && id.equals(((Theme) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Theme{" +
            "id=" + getId() +
            ", themeId=" + getThemeId() +
            ", theme='" + getTheme() + "'" +
            "}";
    }
}
