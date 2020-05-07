package com.yevhenii.muzyka.service;

import com.yevhenii.muzyka.domain.RussianWord;
import com.yevhenii.muzyka.repository.RussianWordRepository;
import com.yevhenii.muzyka.web.rest.dto.RussianWordDto;
import com.yevhenii.muzyka.web.rest.errors.BadRequestAlertException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static com.yevhenii.muzyka.domain.RussianWord.toRussianWordDto;
import static com.yevhenii.muzyka.web.rest.errors.ErrorConstants.RUSSIAN_WORD_ALREADY_EXISTS;
import static com.yevhenii.muzyka.web.rest.errors.ErrorConstants.RUSSIAN_WORD_NOT_FOUND;
import static java.util.stream.Collectors.toList;

@Service
@Transactional
public class RussianWordService {

    private final String RUSSIAN_ENTITY_KEY = "russianWord";

    private final RussianWordRepository russianWordRepository;

    public RussianWordService(RussianWordRepository russianWordRepository) {
        this.russianWordRepository = russianWordRepository;
    }

    public Set<RussianWord> updateOrCreateRussianWords(Set<RussianWord> russianWordSet) {
        Set<RussianWord> russianWords = new HashSet<>();
        russianWordSet.forEach(russianWord -> {
            Optional<RussianWord> russianWordbyWordOptional = russianWordRepository.findByWord(russianWord.getWord());
            if (russianWordbyWordOptional.isPresent()) {
                russianWords.add(russianWordbyWordOptional.get());
            } else {
                RussianWord newRussianWord = new RussianWord();
                newRussianWord.setFirstCharacter(russianWord.getWord().substring(0, 1));
                newRussianWord.setWord(russianWord.getWord());
                newRussianWord.setCreateDate(Instant.now());
                russianWords.add(newRussianWord);
            }
        });
        return new HashSet<>(russianWordRepository.saveAll(russianWords));
    }

    public RussianWordDto updateRussianWord(Long russianWordId, RussianWordDto updatedRussianWord) {
        Optional<RussianWord> russianWordOptional = russianWordRepository.findById(russianWordId);
        if (russianWordOptional.isEmpty()) {
            throw new BadRequestAlertException(
                String.format("Russian word with id %s not found", russianWordId),
                RUSSIAN_ENTITY_KEY, RUSSIAN_WORD_NOT_FOUND
            );
        }

        RussianWord russianWord = russianWordOptional.get();
        Optional<RussianWord> russianWordByUpdatedWordOptional = getRussianWordByWord(updatedRussianWord.getWord());
        if (russianWordByUpdatedWordOptional.isPresent()) {
            RussianWord russianWordByUpdatedWord = russianWordByUpdatedWordOptional.get();
            if (!russianWord.getId().equals(russianWordByUpdatedWord.getId())) {
                throw new BadRequestAlertException(
                    String.format("Russian word with word %s already exists", russianWordByUpdatedWord.getWord()),
                    RUSSIAN_ENTITY_KEY,
                    RUSSIAN_WORD_ALREADY_EXISTS);
            }
        }

        russianWord.setFirstCharacter(updatedRussianWord.getWord().substring(0, 1));
        russianWord.setWord(updatedRussianWord.getWord());
        russianWord.setUpdateDate(Instant.now());
        russianWord = russianWordRepository.save(russianWord);
        return toRussianWordDto(russianWord);
    }

    public Optional<RussianWord> getRussianWordByWord(String word) {
        return russianWordRepository.findByWord(word);
    }

    public Page<RussianWordDto> findAllByFirstCharacter(String firstCharacter, Pageable pageable) {
        Page<RussianWord> allByFirstCharacter = russianWordRepository.findAllByFirstCharacter(firstCharacter, pageable);
        List<RussianWordDto> convertedListDto = allByFirstCharacter.get()
                                                                   .map(this::convertToDto)
                                                                   .collect(toList());
        return new PageImpl<>(convertedListDto, pageable, allByFirstCharacter.getTotalElements());
    }

    public Page<RussianWordDto> findAllByCreateDateIsLessThanEqual(Instant instant, Pageable pageable) {
        Page<RussianWord> allByCreateDate = russianWordRepository.findAllByCreateDateIsLessThanEqual(instant, pageable);
        List<RussianWordDto> convertedListDto = allByCreateDate.get()
            .map(this::convertToDto)
            .collect(toList());
        return new PageImpl<>(convertedListDto, pageable, allByCreateDate.getTotalElements());
    }

    public void deleteByWord(String word) {
        russianWordRepository.deleteByWord(word);
    }

    public void deleteAllByFirstCharacter(String firstCharacter) {
        russianWordRepository.deleteAllByFirstCharacter(firstCharacter);
    }

    public void deleteById(Long id) {
        russianWordRepository.deleteById(id);
    }

    public void deleteAll() {
        russianWordRepository.deleteAll();
    }

    public Page<RussianWordDto> findAllByWordContaining(String containing, Pageable pageable) {
        Page<RussianWord> allByWordContaining = russianWordRepository.findAllByWordContaining(containing, pageable);
        List<RussianWordDto> convertedListDto = allByWordContaining.get()
            .map(this::convertToDto)
            .collect(toList());
        return new PageImpl<>(convertedListDto, pageable, allByWordContaining.getTotalElements());
    }

    private RussianWordDto convertToDto(RussianWord russianWord) {
        return toRussianWordDto(russianWord);
    }

}
