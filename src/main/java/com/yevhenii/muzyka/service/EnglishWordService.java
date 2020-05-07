package com.yevhenii.muzyka.service;

import com.yevhenii.muzyka.domain.EnglishWord;
import com.yevhenii.muzyka.domain.RussianWord;
import com.yevhenii.muzyka.repository.EnglishWordRepository;
import com.yevhenii.muzyka.web.rest.dto.EnglishWordDto;
import com.yevhenii.muzyka.web.rest.errors.BadRequestAlertException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static com.yevhenii.muzyka.domain.EnglishWord.toEnglishWordDto;
import static com.yevhenii.muzyka.web.rest.errors.ErrorConstants.ENGLISH_WORD_ALREADY_EXISTS;
import static com.yevhenii.muzyka.web.rest.errors.ErrorConstants.ENGLISH_WORD_NOT_FOUND;
import static java.util.stream.Collectors.toList;

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

    public EnglishWordDto createEnglishWord(EnglishWordDto newEnglishWord) {
        Optional<EnglishWord> englishWordByWord = getEnglishWordByWord(newEnglishWord.getWord());

        if (englishWordByWord.isEmpty()) {
            Set<RussianWord> newRussianWords = russianWordService.updateOrCreateRussianWords(
                newEnglishWord.getRussianWords());

            EnglishWord createdEnglishWord = new EnglishWord();
            createdEnglishWord.setFirstCharacter(newEnglishWord.getWord().substring(0, 1));
            createdEnglishWord.setWord(newEnglishWord.getWord());
            createdEnglishWord.setCreateDate(Instant.now());
            createdEnglishWord.setRussianWords(newRussianWords);
            createdEnglishWord = englishWordRepository.save(createdEnglishWord);
            return toEnglishWordDto(createdEnglishWord);
        }

        throw new BadRequestAlertException(
            String.format("Englsih word with word %s already exists", newEnglishWord.getWord()),
            ENGLISH_ENTITY_KEY,
            ENGLISH_WORD_ALREADY_EXISTS);
    }

    public EnglishWordDto updateEnglishWord(Long englishWordId, EnglishWordDto updatedEnglishWord) {
        Optional<EnglishWord> englishWordByIdOptional = englishWordRepository.findById(englishWordId);

        if (englishWordByIdOptional.isEmpty()) {
            throw new BadRequestAlertException(String.format("Englsih word with id %s not found", englishWordId),
                                               ENGLISH_ENTITY_KEY,
                                               ENGLISH_WORD_NOT_FOUND);
        }

        EnglishWord englishWord = englishWordByIdOptional.get();
        Optional<EnglishWord> englishWordByUpdatedWordOptional = getEnglishWordByWord(updatedEnglishWord.getWord());
        if (englishWordByUpdatedWordOptional.isPresent()) {
            EnglishWord englishWordByUpdatedWord = englishWordByUpdatedWordOptional.get();
            if (!englishWord.getId().equals(englishWordByUpdatedWord.getId())) {
                throw new BadRequestAlertException(
                    String.format("Englsih word with word %s already exists", englishWordByUpdatedWord.getWord()),
                    ENGLISH_ENTITY_KEY,
                    ENGLISH_WORD_ALREADY_EXISTS);
            }
        }

        Set<RussianWord> russianWords = russianWordService.updateOrCreateRussianWords(
            updatedEnglishWord.getRussianWords());

        englishWord.setFirstCharacter(updatedEnglishWord.getWord().substring(0, 1));
        englishWord.setWord(updatedEnglishWord.getWord());
        englishWord.setUpdateDate(Instant.now());
        englishWord.setRussianWords(russianWords);
        englishWord = englishWordRepository.save(englishWord);
        return toEnglishWordDto(englishWord);
    }

    public Optional<EnglishWord> getEnglishWordByWord(String word) {
        return englishWordRepository.findByWord(word);
    }

    public Page<EnglishWordDto> findAllByFirstCharacter(String firstCharacter, Pageable pageable) {
        Page<EnglishWord> allByFirstCharacter = englishWordRepository.findAllByFirstCharacter(firstCharacter, pageable);
        List<EnglishWordDto> convertedList = allByFirstCharacter.get()
                                                                .map(this::convertToDto)
                                                                .collect(toList());
        return new PageImpl<>(convertedList, pageable, allByFirstCharacter.getTotalElements());
    }

    public Page<EnglishWordDto> findAllByCreateDateIsLessThanEqual(Instant instant, Pageable pageable) {
        Page<EnglishWord> allByCreateDate = englishWordRepository.findAllByCreateDateIsLessThanEqual(instant, pageable);
        List<EnglishWordDto> convertedListDto = allByCreateDate.get()
                                                               .map(this::convertToDto)
                                                               .collect(toList());
        return new PageImpl<>(convertedListDto, pageable, allByCreateDate.getTotalElements());
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

    public Page<EnglishWordDto> findAllByWordContaining(String containing, Pageable pageable) {
        Page<EnglishWord> allByWordContaining = englishWordRepository.findAllByWordContaining(containing, pageable);
        List<EnglishWordDto> convertedListDto = allByWordContaining.get()
                                                                   .map(this::convertToDto)
                                                                   .collect(toList());
        return new PageImpl<>(convertedListDto, pageable, allByWordContaining.getTotalElements());
    }

    private EnglishWordDto convertToDto(EnglishWord englishWord) {
        return toEnglishWordDto(englishWord);
    }

}
