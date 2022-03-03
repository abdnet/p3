package org.amawal.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A AmawalWord.
 */
@Entity
@Table(name = "amawal_word")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class AmawalWord implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "word_id")
    private Long wordId;

    @Column(name = "orthographe_tifinagh")
    private String orthographeTifinagh;

    @Column(name = "orthographe_latin")
    private String orthographeLatin;

    @JsonIgnoreProperties(value = { "contributeur", "validateur" }, allowSetters = true)
    @OneToOne
    @JoinColumn(unique = true)
    private WordInfo wordInfo;

    @OneToMany(mappedBy = "amawalWord")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "langue", "words", "amawalWord" }, allowSetters = true)
    private Set<TamazightToLang> traductions = new HashSet<>();

    @ManyToOne
    private WordType wordType;

    @ManyToOne
    private Theme theme;

    @ManyToMany
    @JoinTable(
        name = "rel_amawal_word__dialecte",
        joinColumns = @JoinColumn(name = "amawal_word_id"),
        inverseJoinColumns = @JoinColumn(name = "dialecte_id")
    )
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "regions", "words" }, allowSetters = true)
    private Set<Dialecte> dialectes = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties(value = { "langue", "words", "amawalWord" }, allowSetters = true)
    private TamazightToLang tamazightToLang;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public AmawalWord id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getWordId() {
        return this.wordId;
    }

    public AmawalWord wordId(Long wordId) {
        this.setWordId(wordId);
        return this;
    }

    public void setWordId(Long wordId) {
        this.wordId = wordId;
    }

    public String getOrthographeTifinagh() {
        return this.orthographeTifinagh;
    }

    public AmawalWord orthographeTifinagh(String orthographeTifinagh) {
        this.setOrthographeTifinagh(orthographeTifinagh);
        return this;
    }

    public void setOrthographeTifinagh(String orthographeTifinagh) {
        this.orthographeTifinagh = orthographeTifinagh;
    }

    public String getOrthographeLatin() {
        return this.orthographeLatin;
    }

    public AmawalWord orthographeLatin(String orthographeLatin) {
        this.setOrthographeLatin(orthographeLatin);
        return this;
    }

    public void setOrthographeLatin(String orthographeLatin) {
        this.orthographeLatin = orthographeLatin;
    }

    public WordInfo getWordInfo() {
        return this.wordInfo;
    }

    public void setWordInfo(WordInfo wordInfo) {
        this.wordInfo = wordInfo;
    }

    public AmawalWord wordInfo(WordInfo wordInfo) {
        this.setWordInfo(wordInfo);
        return this;
    }

    public Set<TamazightToLang> getTraductions() {
        return this.traductions;
    }

    public void setTraductions(Set<TamazightToLang> tamazightToLangs) {
        if (this.traductions != null) {
            this.traductions.forEach(i -> i.setAmawalWord(null));
        }
        if (tamazightToLangs != null) {
            tamazightToLangs.forEach(i -> i.setAmawalWord(this));
        }
        this.traductions = tamazightToLangs;
    }

    public AmawalWord traductions(Set<TamazightToLang> tamazightToLangs) {
        this.setTraductions(tamazightToLangs);
        return this;
    }

    public AmawalWord addTraduction(TamazightToLang tamazightToLang) {
        this.traductions.add(tamazightToLang);
        tamazightToLang.setAmawalWord(this);
        return this;
    }

    public AmawalWord removeTraduction(TamazightToLang tamazightToLang) {
        this.traductions.remove(tamazightToLang);
        tamazightToLang.setAmawalWord(null);
        return this;
    }

    public WordType getWordType() {
        return this.wordType;
    }

    public void setWordType(WordType wordType) {
        this.wordType = wordType;
    }

    public AmawalWord wordType(WordType wordType) {
        this.setWordType(wordType);
        return this;
    }

    public Theme getTheme() {
        return this.theme;
    }

    public void setTheme(Theme theme) {
        this.theme = theme;
    }

    public AmawalWord theme(Theme theme) {
        this.setTheme(theme);
        return this;
    }

    public Set<Dialecte> getDialectes() {
        return this.dialectes;
    }

    public void setDialectes(Set<Dialecte> dialectes) {
        this.dialectes = dialectes;
    }

    public AmawalWord dialectes(Set<Dialecte> dialectes) {
        this.setDialectes(dialectes);
        return this;
    }

    public AmawalWord addDialecte(Dialecte dialecte) {
        this.dialectes.add(dialecte);
        dialecte.getWords().add(this);
        return this;
    }

    public AmawalWord removeDialecte(Dialecte dialecte) {
        this.dialectes.remove(dialecte);
        dialecte.getWords().remove(this);
        return this;
    }

    public TamazightToLang getTamazightToLang() {
        return this.tamazightToLang;
    }

    public void setTamazightToLang(TamazightToLang tamazightToLang) {
        this.tamazightToLang = tamazightToLang;
    }

    public AmawalWord tamazightToLang(TamazightToLang tamazightToLang) {
        this.setTamazightToLang(tamazightToLang);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AmawalWord)) {
            return false;
        }
        return id != null && id.equals(((AmawalWord) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AmawalWord{" +
            "id=" + getId() +
            ", wordId=" + getWordId() +
            ", orthographeTifinagh='" + getOrthographeTifinagh() + "'" +
            ", orthographeLatin='" + getOrthographeLatin() + "'" +
            "}";
    }
}
