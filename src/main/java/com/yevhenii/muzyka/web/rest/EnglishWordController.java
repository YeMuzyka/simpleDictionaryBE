package com.yevhenii.muzyka.web.rest;

import com.yevhenii.muzyka.domain.EnglishWord;
import com.yevhenii.muzyka.service.EnglishWordService;
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

@RestController
@RequestMapping("/api")
public class EnglishWordController {

    private final EnglishWordService englishWordService;

    @Autowired
    public EnglishWordController(EnglishWordService englishWordService) {
        this.englishWordService = englishWordService;
    }

    @PostMapping("/english-word")
    public ResponseEntity<EnglishWord> createNewEnglsihWord(@RequestBody EnglishWord newEnglishWord)
        throws URISyntaxException {
        EnglishWord result = englishWordService.createEnglishWord(newEnglishWord);
        return ResponseEntity.created(new URI("/api/english-word/" + result.getId()))
            .body(result);
    }

    @PutMapping("/english-word/{id}")
    public ResponseEntity<EnglishWord> updateEnglishWord(@PathVariable Long id,
                                                         @RequestBody EnglishWord updatedEnglishWord) {
        return ResponseEntity.ok(englishWordService.updateEnglishWord(id, updatedEnglishWord));
    }

    @GetMapping("/english-word/{word}")
    public ResponseEntity<EnglishWord> getEnglishWordByWord(@PathVariable String word) {
        return ResponseEntity.of(englishWordService.getEnglishWordByWord(word));
    }

    @GetMapping("/english-word/find/first-character/{firstCharacter}")
    public ResponseEntity<List<EnglishWord>> findAllByFirstCharacter(@PathVariable String firstCharacter,
                                                                     Pageable pageable) {
        Page<EnglishWord> page = englishWordService.findAllByFirstCharacter(firstCharacter, pageable);
        return ResponseEntity.ok(page.getContent());
    }

    @GetMapping("/english-word/find/create-date/{createDate}")
    public ResponseEntity<List<EnglishWord>> findAllByFirstCharacter(@PathVariable Instant createDate,
                                                                     Pageable pageable) {
        return ResponseEntity.ok(englishWordService.findAllByCreateDateIsLessThanEqual(createDate, pageable).getContent());
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
    public ResponseEntity<List<EnglishWord>> findAllByWordContaining(@PathVariable String containing,
                                                                     Pageable pageable) {
        return ResponseEntity.ok(englishWordService.findAllByWordContaining(containing, pageable).getContent());
    }

}
