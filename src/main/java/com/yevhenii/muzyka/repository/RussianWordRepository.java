package com.yevhenii.muzyka.repository;

import com.yevhenii.muzyka.domain.RussianWord;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Repository
public interface RussianWordRepository extends JpaRepository<RussianWord, Long> {

    Optional<RussianWord> findByWord(String word);

    Page<RussianWord> findAllByFirstCharacter(String firstCharacter, Pageable pageable);

    <S extends RussianWord> List<S> saveAll(Iterable<S> iterableList);

    Page<RussianWord> findAllByCreateDateIsLessThanEqual(Instant instant, Pageable pageable);

    void deleteByWord(String word);

    void deleteAllByFirstCharacter(String firstCharacter);

    @Override
    void deleteById(Long id);

    @Override
    void deleteAll();

    Page<RussianWord> findAllByWordContaining(String containing, Pageable pageable);

}
