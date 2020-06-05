package com.yevhenii.muzyka.service;

import com.yevhenii.muzyka.domain.EnglishWord;
import com.yevhenii.muzyka.domain.RussianWord;
import com.yevhenii.muzyka.repository.EnglishWordRepository;
import com.yevhenii.muzyka.web.rest.dto.EnglishWordDto;
import com.yevhenii.muzyka.web.rest.errors.BadRequestAlertException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.time.Instant;
import java.util.*;

import static com.yevhenii.muzyka.domain.EnglishWord.toEnglishWordDto;
import static java.time.temporal.ChronoUnit.DAYS;
import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;
import static java.util.stream.Collectors.toSet;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class EnglishWordServiceUnitTest {
    private static final String TEST_ENGLISH_WORD = "Test";
    private static final String TEST_ENGLISH_WORD_FIRST_CHAR = TEST_ENGLISH_WORD.substring(0, 1);
    private static final long TEST_ENGLISH_WORD_ID = 1L;
    private static final String FIRST_RUSSIAN_WORD = "ПЕРВЫЙ";
    private static final String SECOND_RUSSIAN_WORD = "ВТОРОЙ";

    @Mock
    private RussianWordService russianWordService;
    @Mock
    private EnglishWordRepository englishWordRepository;
    @InjectMocks
    private EnglishWordService englishWordService;

    @Test
    public void shouldCreateEnglishWord() {
        EnglishWord mockEnglishWord = createEnshlishWord(asList(FIRST_RUSSIAN_WORD, SECOND_RUSSIAN_WORD));

        when(englishWordRepository.save(mockEnglishWord)).thenReturn(mockEnglishWord);

        EnglishWordDto createdEnglishWord = englishWordService.createEnglishWord(toEnglishWordDto(mockEnglishWord));

        verify(englishWordRepository).findByWord(TEST_ENGLISH_WORD);
        assertActualEnglishWordDto(createdEnglishWord);
    }

    @Test(expected = BadRequestAlertException.class)
    public void shouldThrowExceptionWhenCreateEnglishWord() {
        EnglishWord mockEnglishWord = createEnshlishWord(asList(FIRST_RUSSIAN_WORD, SECOND_RUSSIAN_WORD));

        when(englishWordRepository.findByWord(TEST_ENGLISH_WORD)).thenReturn(Optional.of(mockEnglishWord));

        englishWordService.createEnglishWord(toEnglishWordDto(mockEnglishWord));
    }

    @Test
    public void shouldUpdateEnglishWord() {
        EnglishWord mockEnglishWord = createEnshlishWord(asList(FIRST_RUSSIAN_WORD, SECOND_RUSSIAN_WORD));

        when(englishWordRepository.findById(TEST_ENGLISH_WORD_ID)).thenReturn(Optional.of(mockEnglishWord));
        when(englishWordRepository.findByWord(TEST_ENGLISH_WORD)).thenReturn(Optional.empty());
        when(englishWordRepository.save(mockEnglishWord)).thenReturn(mockEnglishWord);

        EnglishWordDto updateEnglishWord = englishWordService.updateEnglishWord(TEST_ENGLISH_WORD_ID,
                                                                                toEnglishWordDto(mockEnglishWord));

        verify(englishWordRepository).findById(TEST_ENGLISH_WORD_ID);
        verify(englishWordRepository).findByWord(TEST_ENGLISH_WORD);
        assertActualEnglishWordDto(updateEnglishWord);
    }

    @Test(expected = BadRequestAlertException.class)
    public void shouldThrowExceptionWhenUpdateEnglishWordIfIdIncorrect() {
        EnglishWord mockEnglishWord = createEnshlishWord(asList(FIRST_RUSSIAN_WORD, SECOND_RUSSIAN_WORD));

        when(englishWordRepository.findById(TEST_ENGLISH_WORD_ID)).thenReturn(Optional.empty());

        englishWordService.updateEnglishWord(TEST_ENGLISH_WORD_ID, toEnglishWordDto(mockEnglishWord));
        verify(englishWordRepository).findById(TEST_ENGLISH_WORD_ID);
    }

    @Test(expected = BadRequestAlertException.class)
    public void shouldThrowExceptionWhenUpdateEnglishWordIfIdsNotEquals() {
        EnglishWord mockEnglishWord = createEnshlishWord(asList(FIRST_RUSSIAN_WORD, SECOND_RUSSIAN_WORD));
        EnglishWord mockEnglishWordWithSameWord = createEnshlishWord(asList(FIRST_RUSSIAN_WORD,
                                                                                   SECOND_RUSSIAN_WORD));
        mockEnglishWordWithSameWord.setId(TEST_ENGLISH_WORD_ID + 1);

        when(englishWordRepository.findById(TEST_ENGLISH_WORD_ID)).thenReturn(Optional.of(mockEnglishWord));
        when(englishWordRepository.findByWord(TEST_ENGLISH_WORD)).thenReturn(Optional.of(mockEnglishWordWithSameWord));

        englishWordService.updateEnglishWord(TEST_ENGLISH_WORD_ID, toEnglishWordDto(mockEnglishWord));

        verify(englishWordRepository).findById(TEST_ENGLISH_WORD_ID);
        verify(englishWordRepository).findByWord(TEST_ENGLISH_WORD);
    }

    @Test
    public void shouldGetEnglishWordByWord() {
        EnglishWord mockEnglishWord = createEnshlishWord(asList(FIRST_RUSSIAN_WORD, SECOND_RUSSIAN_WORD));

        when(englishWordRepository.findByWord(TEST_ENGLISH_WORD)).thenReturn(Optional.of(mockEnglishWord));

        Optional<EnglishWord> englishWordByWordOptional = englishWordService.getEnglishWordByWord(TEST_ENGLISH_WORD);

        verify(englishWordRepository).findByWord(TEST_ENGLISH_WORD);
        assertActualEnglishWordDto(toEnglishWordDto(englishWordByWordOptional.get()));
    }

    @Test
    public void shouldReturnEmptyWhenGetEnglishWordByWord() {
        when(englishWordRepository.findByWord(TEST_ENGLISH_WORD)).thenReturn(Optional.empty());

        Optional<EnglishWord> englishWordByWordOptional = englishWordService.getEnglishWordByWord(TEST_ENGLISH_WORD);

        verify(englishWordRepository).findByWord(TEST_ENGLISH_WORD);
        assertNotNull(englishWordByWordOptional);
        assertTrue(englishWordByWordOptional.isEmpty());
        assertFalse(englishWordByWordOptional.isPresent());
    }

    @Test
    public void shouldFindAllByFirstCharacter() {
        PageRequest pageRequest = PageRequest.of(0, 1);
        EnglishWord mockEnglishWord = createEnshlishWord(asList(FIRST_RUSSIAN_WORD, SECOND_RUSSIAN_WORD));
        List<EnglishWord> mockEnglishWordList = singletonList(mockEnglishWord);
        PageImpl<EnglishWord> mockEnglsihWordPage = new PageImpl<>(mockEnglishWordList,
                                                                   pageRequest,
                                                                   mockEnglishWordList.size());

        when(englishWordRepository.findAllByFirstCharacter(TEST_ENGLISH_WORD_FIRST_CHAR, pageRequest))
            .thenReturn(mockEnglsihWordPage);

        Page<EnglishWordDto> englishWordPage = englishWordService.findAllByFirstCharacter(TEST_ENGLISH_WORD_FIRST_CHAR,
                                                                                          pageRequest);

        assertEnglsihWordDtoPage(englishWordPage);
    }

    @Test
    public void shouldFindAllByCreateDateIsLessThanEqual() {
        PageRequest pageRequest = PageRequest.of(0, 1);
        Instant searchInstant = Instant.parse("2020-06-05T10:00:00.000000000Z");
        EnglishWord mockEnglishWordInPageSearch = createEnshlishWord(asList(FIRST_RUSSIAN_WORD, SECOND_RUSSIAN_WORD));

        mockEnglishWordInPageSearch.setCreateDate(searchInstant.minus(1, DAYS));
        List<EnglishWord> mockEnglishWordList = singletonList(mockEnglishWordInPageSearch);
        PageImpl<EnglishWord> mockEnglsihWordPage = new PageImpl<>(mockEnglishWordList,
            pageRequest,
            mockEnglishWordList.size());

        when(englishWordRepository.findAllByCreateDateIsLessThanEqual(searchInstant, pageRequest))
            .thenReturn(mockEnglsihWordPage);

        Page<EnglishWordDto> englishWordPage = englishWordService.findAllByCreateDateIsLessThanEqual(searchInstant,
                                                                                                     pageRequest);

        assertEnglsihWordDtoPage(englishWordPage);
    }

    private EnglishWord createEnshlishWord(List<String> russianWordList) {
        Set<RussianWord> russianWords = russianWordList.stream().map(this::mapStringToRussianWord).collect(toSet());

        EnglishWord englishWord = new EnglishWord();
        englishWord.setId(TEST_ENGLISH_WORD_ID);
        englishWord.setFirstCharacter(TEST_ENGLISH_WORD_FIRST_CHAR);
        englishWord.setWord(TEST_ENGLISH_WORD);
        englishWord.setCreateDate(Instant.now());
        englishWord.setRussianWords(russianWords);
        return englishWord;
    }

    private RussianWord mapStringToRussianWord(String word) {
        RussianWord russianWord = new RussianWord();
        russianWord.setWord(word);
        russianWord.setFirstCharacter(word.substring(0, 1));
        return russianWord;
    }

    private void assertActualEnglishWordDto(EnglishWordDto actual) {
        assertNotNull(actual);
        assertEquals(TEST_ENGLISH_WORD_ID, actual.getId());
        assertEquals(TEST_ENGLISH_WORD, actual.getWord());
        assertEquals(TEST_ENGLISH_WORD_FIRST_CHAR, actual.getFirstCharacter());
        assertNotNull(actual.getRussianWords());
    }

    private void assertEnglsihWordDtoPage(Page<EnglishWordDto> englishWordPage) {
        assertNotNull(englishWordPage);
        assertEquals(1, englishWordPage.getTotalPages());
        assertEquals(1, englishWordPage.getTotalElements());

        List<EnglishWordDto> englishWordList = englishWordPage.getContent();
        assertNotNull(englishWordList);

        EnglishWordDto englishWordDto = englishWordList.get(0);
        assertActualEnglishWordDto(englishWordDto);
    }

}
