package com.example.weather.controller;

import com.example.weather.domain.Diary;
import com.example.weather.service.DiaryService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController // 기본 @Controller랑 다른 점 : 상태를 정해줌
public class DiaryController {

    private final DiaryService diaryService;

    public DiaryController(DiaryService diaryService) {
        this.diaryService = diaryService;
    }

    @PostMapping("/create/diary")
    void createDiary(
            // ? 뒤에 올 쿼리 스트링으로 날짜를 받고, Http 바디로 text를 받음
            @RequestParam @DateTimeFormat(iso=DateTimeFormat.ISO.DATE) LocalDate date,
            @RequestBody String text){
        diaryService.createDiary(date, text);
    }

    @GetMapping("/read/diary")
    List<Diary> readDiary(
            @RequestParam @DateTimeFormat(iso=DateTimeFormat.ISO.DATE) LocalDate date){
        return diaryService.readDiary(date);
    } // 리스트를 리턴한다는 것은 JSONARRAY로 묶인 여러 Diary정보가 담긴 JSON을 보낸다는 뜻

    @GetMapping("/read/diaries")
    List<Diary> readDiaries(
            @RequestParam @DateTimeFormat(iso=DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso=DateTimeFormat.ISO.DATE) LocalDate endDate){
        return diaryService.readDiaries(startDate, endDate);
    }

    @PutMapping("/update/diary")
    void updateDiary(
            @RequestParam @DateTimeFormat(iso=DateTimeFormat.ISO.DATE) LocalDate date,
            @RequestBody String text){
        diaryService.updateDiary(date, text);
    }

    @DeleteMapping("/delete/diary")
    void deleteDiary(
            @RequestParam @DateTimeFormat(iso=DateTimeFormat.ISO.DATE) LocalDate date){
        diaryService.deleteDiary(date);
    }
}