package org.amawal.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A TamazightToLang.
 */
@Entity
@Table(name = "tamazight_to_lang")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class TamazightToLang implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "traduction_id")
    private Long traductionId;

    @OneToOne
    @JoinColumn(unique = true)
    private Langue langue;

    @OneToMany(mappedBy = "tamazightToLang")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "wordInfo", "traductions", "wordType", "theme", "dialectes", "tamazightToLang" }, allowSetters = true)
    private Set<AmawalWord> words = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties(value = { "wordInfo", "traductions", "wordType", "theme", "dialectes", "tamazightToLang" }, allowSetters = true)
    private AmawalWord amawalWord;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public TamazightToLang id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getTraductionId() {
        return this.traductionId;
    }

    public TamazightToLang traductionId(Long traductionId) {
        this.setTraductionId(traductionId);
        return this;
    }

    public void setTraductionId(Long traductionId) {
        this.traductionId = traductionId;
    }

    public Langue getLangue() {
        return this.langue;
    }

    public void setLangue(Langue langue) {
        this.langue = langue;
    }

    public TamazightToLang langue(Langue langue) {
        this.setLangue(langue);
        return this;
    }

    public Set<AmawalWord> getWords() {
        return this.words;
    }

    public void setWords(Set<AmawalWord> amawalWords) {
        if (this.words != null) {
            this.words.forEach(i -> i.setTamazightToLang(null));
        }
        if (amawalWords != null) {
            amawalWords.forEach(i -> i.setTamazightToLang(this));
        }
        this.words = amawalWords;
    }

    public TamazightToLang words(Set<AmawalWord> amawalWords) {
        this.setWords(amawalWords);
        return this;
    }

    public TamazightToLang addWord(AmawalWord amawalWord) {
        this.words.add(amawalWord);
        amawalWord.setTamazightToLang(this);
        return this;
    }

    public TamazightToLang removeWord(AmawalWord amawalWord) {
        this.words.remove(amawalWord);
        amawalWord.setTamazightToLang(null);
        return this;
    }

    public AmawalWord getAmawalWord() {
        return this.amawalWord;
    }

    public void setAmawalWord(AmawalWord amawalWord) {
        this.amawalWord = amawalWord;
    }

    public TamazightToLang amawalWord(AmawalWord amawalWord) {
        this.setAmawalWord(amawalWord);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TamazightToLang)) {
            return false;
        }
        return id != null && id.equals(((TamazightToLang) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TamazightToLang{" +
            "id=" + getId() +
            ", traductionId=" + getTraductionId() +
            "}";
    }
}
