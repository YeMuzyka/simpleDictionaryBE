package com.yevhenii.muzyka.web.rest;

import com.yevhenii.muzyka.domain.EnglishWord;
import com.yevhenii.muzyka.service.EnglishWordService;
import com.yevhenii.muzyka.web.rest.dto.EnglishWordDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.net.URISyntaxException;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

import static com.yevhenii.muzyka.domain.EnglishWord.toEnglishWordDto;

@RestController
@RequestMapping("/api")
public class EnglishWordController {

    private final EnglishWordService englishWordService;

    @Autowired
    public EnglishWordController(EnglishWordService englishWordService) {
        this.englishWordService = englishWordService;
    }

    @PostMapping("/english-word")
    public ResponseEntity<EnglishWordDto> createNewEnglsihWord(@RequestBody EnglishWordDto newEnglishWord)
        throws URISyntaxException {
        EnglishWordDto result = englishWordService.createEnglishWord(newEnglishWord);
        return ResponseEntity.created(new URI("/api/english-word/" + result.getId()))
                             .body(result);
    }

    @PutMapping("/english-word/{id}")
    public ResponseEntity<EnglishWordDto> updateEnglishWord(@PathVariable Long id,
                                                         @RequestBody EnglishWordDto updatedEnglishWord) {
        return ResponseEntity.ok(englishWordService.updateEnglishWord(id, updatedEnglishWord));
    }

    @GetMapping("/english-word/{word}")
    public ResponseEntity<EnglishWordDto> getEnglishWordByWord(@PathVariable String word) {
        Optional<EnglishWord> englishWordByWord = englishWordService.getEnglishWordByWord(word);
        if (englishWordByWord.isEmpty()) {
            return ResponseEntity.of(Optional.empty());
        }
        return ResponseEntity.of(Optional.of(toEnglishWordDto(englishWordByWord.get())));
    }

    @GetMapping("/english-word/find/first-character/{firstCharacter}")
    public ResponseEntity<List<EnglishWordDto>> findAllByFirstCharacter(@PathVariable String firstCharacter,
                                                                     Pageable pageable) {
        Page<EnglishWordDto> page = englishWordService.findAllByFirstCharacter(firstCharacter, pageable);
        return ResponseEntity.ok(page.getContent());
    }

    @GetMapping("/english-word/find/create-date/{createDate}")
    public ResponseEntity<List<EnglishWordDto>> findAllByCreateDateIsLessThanEqual(@PathVariable Instant createDate,
                                                                                Pageable pageable) {
        return ResponseEntity.ok(englishWordService
                                .findAllByCreateDateIsLessThanEqual(createDate, pageable)
                                .getContent());
    }

    @DeleteMapping("/english-word/word/{word}")
    public ResponseEntity deleteByWord(@PathVariable String word) {
        englishWordService.deleteByWord(word);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/english-word/first-character/{firstCharacter}")
    public ResponseEntity deleteAllByFirstCharacter(@PathVariable String firstCharacter) {
        englishWordService.deleteAllByFirstCharacter(firstCharacter);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/english-word/id/{id}")
    public ResponseEntity deleteById(@PathVariable Long id) {
        englishWordService.deleteById(id);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/english-word")
    public ResponseEntity deleteAll() {
        englishWordService.deleteAll();
        return ResponseEntity.ok().build();
    }

    @GetMapping("/english-word/find/word-containing/{containing}")
    public ResponseEntity<List<EnglishWordDto>> findAllByWordContaining(@PathVariable String containing,
                                                                     Pageable pageable) {
        return ResponseEntity.ok(englishWordService.findAllByWordContaining(containing, pageable).getContent());
    }

}
