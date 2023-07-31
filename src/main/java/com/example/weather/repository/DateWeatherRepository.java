package com.example.weather.repository;

import com.example.weather.domain.DateWeather;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface DateWeatherRepository extends JpaRepository<DateWeather, LocalDate> {
    List<DateWeather> findAllByDate(LocalDate date);
    // 근데 위에 것, DB에는 하루에 하나만 날씨 정보가 저장될텐데 굳이 리스트로 받아오는 이유가 있나?
}
