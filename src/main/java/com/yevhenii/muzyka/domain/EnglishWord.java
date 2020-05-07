package com.yevhenii.muzyka.domain;

import com.yevhenii.muzyka.web.rest.dto.EnglishWordDto;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.GenerationType;
import javax.persistence.Column;
import javax.persistence.ManyToMany;
import javax.persistence.FetchType;
import javax.persistence.CascadeType;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "english_words")
public class EnglishWord implements Serializable {

    private static final long serialVersionUID = 1L;

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

    @ManyToMany(fetch = FetchType.EAGER,
               cascade = {CascadeType.ALL})
    private Set<RussianWord> russianWords = new HashSet<>();

    public EnglishWord() {

    }

    public EnglishWord(@Size(max = 1) String firstCharacter,
                       String word,
                       Instant createDate,
                       Instant updateDate,
                       Set<RussianWord> russianWords) {
        this.firstCharacter = firstCharacter;
        this.word = word;
        this.createDate = createDate;
        this.updateDate = updateDate;
        this.russianWords = russianWords;
    }

    public static EnglishWordDto toEnglishWordDto(EnglishWord englishWord) {
        return new EnglishWordDto(englishWord.getId(),
                                  englishWord.getFirstCharacter(),
                                  englishWord.getWord(),
                                  englishWord.getCreateDate(),
                                  englishWord.getUpdateDate(),
                                  englishWord.getRussianWords());
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

    public Set<RussianWord> getRussianWords() {
        return russianWords;
    }

    public void setRussianWords(Set<RussianWord> russianWords) {
        this.russianWords = russianWords;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EnglishWord that = (EnglishWord) o;
        return firstCharacter.equals(that.firstCharacter) && word.equals(that.word);
    }

    @Override
    public int hashCode() {
        return Objects.hash(firstCharacter, word);
    }

}
