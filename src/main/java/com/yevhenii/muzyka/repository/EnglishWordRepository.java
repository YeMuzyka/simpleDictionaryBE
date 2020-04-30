package com.yevhenii.muzyka.repository;

import com.yevhenii.muzyka.domain.EnglishWord;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Repository
public interface EnglishWordRepository extends JpaRepository<EnglishWord, Long> {

    Optional<EnglishWord> findByWord(String word);

    Page<EnglishWord> findAllByFirstCharacter(String firstCharacter, Pageable pageable);

    <S extends EnglishWord> List<S> saveAll(Iterable<S> iterableList);

    Page<EnglishWord> findAllByCreateDateIsLessThanEqual(Instant instant, Pageable pageable);

    void deleteByWord(String word);

    void deleteAllByFirstCharacter(String firstCharacter);

    @Override
    void deleteById(Long id);

    @Override
    void deleteAll();

    Page<EnglishWord> findAllByWordContaining(String containing, Pageable pageable);

}
