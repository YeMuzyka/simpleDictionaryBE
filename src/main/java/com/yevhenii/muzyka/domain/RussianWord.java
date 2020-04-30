package com.yevhenii.muzyka.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.GenerationType;
import javax.persistence.Column;
import javax.persistence.ManyToMany;
import javax.persistence.FetchType;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "russian_words")
public class RussianWord implements Serializable {

    private static final long serialVersionUID = 2L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "first_character", nullable = false)
    @Size(max = 1)
    private String firstCharacter;

    @Column(name = "word", nullable = false)
    private String word;

    @Column(name = "create_date", nullable = false)
    private Instant createDate;

    @Column(name = "update_date")
    private Instant updateDate;

    @ManyToMany(mappedBy = "russianWords", fetch = FetchType.EAGER)
    @JsonIgnoreProperties("russianWords")
    private Set<EnglishWord> englishWords = new HashSet<>();

    public RussianWord() {

    }

    public RussianWord(@Size(max = 1) String firstCharacter, String word, Instant createDate, Instant updateDate, Set<EnglishWord> englishWords) {
        this.firstCharacter = firstCharacter;
        this.word = word;
        this.createDate = createDate;
        this.updateDate = updateDate;
        this.englishWords = englishWords;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstCharacter() {
        return firstCharacter;
    }

    public void setFirstCharacter(String firstCharacter) {
        this.firstCharacter = firstCharacter;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public Instant getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Instant createDate) {
        this.createDate = createDate;
    }

    public Instant getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Instant updateDate) {
        this.updateDate = updateDate;
    }

    public Set<EnglishWord> getEnglishWords() {
        return englishWords;
    }

    public void setEnglishWords(Set<EnglishWord> englishWords) {
        this.englishWords = englishWords;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RussianWord that = (RussianWord) o;
        return firstCharacter.equals(that.firstCharacter) &&
            word.equals(that.word);
    }

    @Override
    public int hashCode() {
        return Objects.hash(firstCharacter, word);
    }
}
