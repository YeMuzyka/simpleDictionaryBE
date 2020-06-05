package com.yevhenii.muzyka.web.rest.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.yevhenii.muzyka.domain.EnglishWord;

import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

public class RussianWordDto {

    private Long id;
    private String firstCharacter;
    private String word;
    private Instant createDate;
    private Instant updateDate;
    @JsonIgnoreProperties("russianWords")
    private Set<EnglishWord> englishWords = new HashSet<>();

    public RussianWordDto() {

    }

    public RussianWordDto(Long id,
                          String firstCharacter,
                          String word,
                          Instant createDate,
                          Instant updateDate,
                          Set<EnglishWord> englishWords) {
        this.id = id;
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

}
