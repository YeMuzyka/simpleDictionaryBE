package com.yevhenii.muzyka.service;

import com.yevhenii.muzyka.domain.EnglishWord;
import com.yevhenii.muzyka.domain.RussianWord;
import com.yevhenii.muzyka.repository.EnglishWordRepository;
import com.yevhenii.muzyka.web.rest.errors.BadRequestAlertException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static com.yevhenii.muzyka.web.rest.errors.ErrorConstants.ENGLISH_WORD_ALREADY_EXISTS;
import static com.yevhenii.muzyka.web.rest.errors.ErrorConstants.ENGLISH_WORD_NOT_FOUND;

@Service
@Transactional
public class EnglishWordService {

    private final String ENGLISH_ENTITY_KEY = "englishWord";

    private final EnglishWordRepository englishWordRepository;
    private final RussianWordService russianWordService;

    @Autowired
    public EnglishWordService(EnglishWordRepository englishWordRepository,
                              RussianWordService russianWordService) {
        this.englishWordRepository = englishWordRepository;
        this.russianWordService = russianWordService;
    }

    public EnglishWord createEnglishWord(EnglishWord newEnglishWord) {
        Optional<EnglishWord> englishWordByWord = getEnglishWordByWord(newEnglishWord.getWord());

        if (englishWordByWord.isEmpty()) {
            Set<RussianWord> newRussianWords = russianWordService.updateOrCreateRussianWords(
                newEnglishWord.getRussianWords());

            EnglishWord createdEnglishWord = new EnglishWord();
            createdEnglishWord.setFirstCharacter(newEnglishWord.getWord().substring(0, 1));
            createdEnglishWord.setWord(newEnglishWord.getWord());
            createdEnglishWord.setCreateDate(Instant.now());
            createdEnglishWord.setRussianWords(newRussianWords);
            return englishWordRepository.save(createdEnglishWord);
        }

        throw new BadRequestAlertException(
            String.format("Englsih word with word %s already exists", newEnglishWord.getWord()),
            ENGLISH_ENTITY_KEY,
            ENGLISH_WORD_ALREADY_EXISTS);
    }

    public EnglishWord updateEnglishWord(Long englishWordId, EnglishWord updatedEnglishWord) {
        Optional<EnglishWord> englishWordByIdOptional = englishWordRepository.findById(englishWordId);

        if (englishWordByIdOptional.isEmpty()) {
            throw new BadRequestAlertException(String.format("Englsih word with id %s not found", englishWordId),
                                               ENGLISH_ENTITY_KEY,
                                               ENGLISH_WORD_NOT_FOUND);
        }

        EnglishWord englishWord = englishWordByIdOptional.get();
        getEnglishWordByWord(updatedEnglishWord.getWord())
            .ifPresent((foundEnglishWord) -> {
                throw new BadRequestAlertException(
                    String.format("Englsih word with word %s already exists", foundEnglishWord.getWord()),
                    ENGLISH_ENTITY_KEY,
                    ENGLISH_WORD_ALREADY_EXISTS);
            });

        Set<RussianWord> russianWords = russianWordService.updateOrCreateRussianWords(
            updatedEnglishWord.getRussianWords());

        englishWord.setFirstCharacter(updatedEnglishWord.getWord().substring(0, 1));
        englishWord.setWord(updatedEnglishWord.getWord());
        englishWord.setUpdateDate(Instant.now());
        englishWord.setRussianWords(russianWords);
        return englishWordRepository.save(englishWord);
    }

    public Optional<EnglishWord> getEnglishWordByWord(String word) {
        return englishWordRepository.findByWord(word);
    }

    public Page<EnglishWord> findAllByFirstCharacter(String firstCharacter, Pageable pageable) {
        return englishWordRepository.findAllByFirstCharacter(firstCharacter, pageable);
    }

    public Page<EnglishWord> findAllByCreateDateIsLessThanEqual(Instant instant, Pageable pageable) {
        return englishWordRepository.findAllByCreateDateIsLessThanEqual(instant, pageable);
    }

    public void deleteByWord(String word) {
        englishWordRepository.deleteByWord(word);
    }

    public void deleteAllByFirstCharacter(String firstCharacter) {
        englishWordRepository.deleteAllByFirstCharacter(firstCharacter);
    }

    public void deleteById(Long id) {
        englishWordRepository.deleteById(id);
    }

    public void deleteAll() {
        englishWordRepository.deleteAll();
    }

    public Page<EnglishWord> findAllByWordContaining(String containing, Pageable pageable) {
        return englishWordRepository.findAllByWordContaining(containing, pageable);
    }

}
