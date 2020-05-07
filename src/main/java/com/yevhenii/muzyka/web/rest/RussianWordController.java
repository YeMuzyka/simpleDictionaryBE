package com.yevhenii.muzyka.web.rest;

import com.yevhenii.muzyka.domain.RussianWord;
import com.yevhenii.muzyka.service.RussianWordService;
import com.yevhenii.muzyka.web.rest.dto.RussianWordDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import static com.yevhenii.muzyka.domain.RussianWord.toRussianWordDto;

@RestController
@RequestMapping("/api")
public class RussianWordController {

    private final RussianWordService russianWordService;

    @Autowired
    public RussianWordController(RussianWordService russianWordService) {
        this.russianWordService = russianWordService;
    }

    @PutMapping("/russian-word/{id}")
    public ResponseEntity<RussianWordDto> updateEnglishWord(@PathVariable Long id,
                                                            @RequestBody RussianWordDto updatedRussianWord) {
        return ResponseEntity.ok(russianWordService.updateRussianWord(id, updatedRussianWord));
    }

    @GetMapping("/russian-word/{word}")
    public ResponseEntity<RussianWordDto> getEnglishWordByWord(@PathVariable String word) {
        Optional<RussianWord> englishWordByWord = russianWordService.getRussianWordByWord(word);
        if (englishWordByWord.isEmpty()) {
            return ResponseEntity.of(Optional.empty());
        }
        return ResponseEntity.of(Optional.of(toRussianWordDto(englishWordByWord.get())));
    }

    @GetMapping("/russian-word/find/first-character/{firstCharacter}")
    public ResponseEntity<List<RussianWordDto>> findAllByFirstCharacter(@PathVariable String firstCharacter,
                                                                        Pageable pageable) {
        Page<RussianWordDto> page = russianWordService.findAllByFirstCharacter(firstCharacter, pageable);
        return ResponseEntity.ok(page.getContent());
    }

    @GetMapping("/russian-word/find/create-date/{createDate}")
    public ResponseEntity<List<RussianWordDto>> findAllByCreateDateIsLessThanEqual(@PathVariable Instant createDate,
                                                                                   Pageable pageable) {
        return ResponseEntity.ok(russianWordService
            .findAllByCreateDateIsLessThanEqual(createDate, pageable)
            .getContent());
    }

    @DeleteMapping("/russian-word/word/{word}")
    public ResponseEntity deleteByWord(@PathVariable String word) {
        russianWordService.deleteByWord(word);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/russian-word/first-character/{firstCharacter}")
    public ResponseEntity deleteAllByFirstCharacter(@PathVariable String firstCharacter) {
        russianWordService.deleteAllByFirstCharacter(firstCharacter);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/russian-word/id/{id}")
    public ResponseEntity deleteById(@PathVariable Long id) {
        russianWordService.deleteById(id);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/russian-word")
    public ResponseEntity deleteAll() {
        russianWordService.deleteAll();
        return ResponseEntity.ok().build();
    }

    @GetMapping("/russian-word/find/word-containing/{containing}")
    public ResponseEntity<List<RussianWordDto>> findAllByWordContaining(@PathVariable String containing,
                                                                        Pageable pageable) {
        return ResponseEntity.ok(russianWordService.findAllByWordContaining(containing, pageable).getContent());
    }
}
