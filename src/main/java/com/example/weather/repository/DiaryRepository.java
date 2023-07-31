package com.example.weather.repository;

import com.example.weather.domain.Diary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface DiaryRepository extends JpaRepository<Diary, Integer> {
    List<Diary> findAllByDate(LocalDate date);
    List<Diary> findAllByDateBetween(LocalDate startDate, LocalDate endDate);
    Diary getFirstByDate(LocalDate date);

    @Transactional // 이 어노테이션이 없으면, 삭제가 실행이 안됨
    void deleteAllByDate(LocalDate date);
}
