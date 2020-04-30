package com.yevhenii.muzyka.service;

import com.yevhenii.muzyka.domain.RussianWord;
import com.yevhenii.muzyka.repository.RussianWordRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
@Transactional
public class RussianWordService {

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
}
