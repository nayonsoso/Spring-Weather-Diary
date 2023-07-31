package com.example.weather.service;

import com.example.weather.domain.Diary;
import com.example.weather.repository.DiaryRepository;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class DiaryService {
    // 스프링 부트에 이미 지정되어있는 변수의 값을 가져와서 apiKey에 넣어주겠다.
    // 변수를 설정하는 것은 application.properties 에서 가능
    // 이런 변수들을 application.properties로 따로 빼는 이유 : 매직넘버를 없애는 것과 같은 원리
    @Value("${openweathermap.key}")
    private String apiKey;
    private final DiaryRepository diaryRepository;

    public DiaryService(DiaryRepository diaryRepository) {
        this.diaryRepository = diaryRepository;
    }

    @Transactional(isolation = Isolation.SERIALIZABLE)
    public void createDiary(LocalDate date, String text){
        // open weather map에서 데이터 받아오기
        String weatherData = getWeatherString();

        // 받아온 날씨 json 파싱하기
        Map<String, Object> parsedWeather = parseWeather(weatherData);

        // 파싱된 데이터 + 일기 값 우리 db에 저장하기
        Diary nowDiary = new Diary();
        nowDiary.setWeather(parsedWeather.get("main").toString());
        nowDiary.setIcon(parsedWeather.get("icon").toString());
        nowDiary.setTemperature((Double) parsedWeather.get("temp"));
        nowDiary.setText(text);
        nowDiary.setDate(date);

        diaryRepository.save(nowDiary);
    }

    // open weather map에서 데이터 받아오기
    private String getWeatherString(){
        String apiUrl = "https://api.openweathermap.org/data/2.5/weather?q=Incheon&appid=" + apiKey;
        try{
            // URL 방식으로 연결을 하는 방법
            URL url = new URL(apiUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            // 제대로 응답했는지 확인하는 부분
            int responseCode = connection.getResponseCode();
            BufferedReader br; // 응답 내용을 담은 버퍼
            if(responseCode == 200){ // 200은 OK란 의미
                br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            } else {
                br = new BufferedReader(new InputStreamReader(connection.getErrorStream()));
            }

            // 버퍼에 담은 값을 String으로 변환
            String inputLine;
            StringBuilder response = new StringBuilder();
            while((inputLine = br.readLine()) != null){ // 응답을 끝까지 읽는다는 뜻
                response.append(inputLine);
            }
            br.close();

            return response.toString();

        } catch (Exception e){
            return e.getMessage();
        }
    }

    // 받아온 날씨 json 파싱하기
    private Map<String, Object> parseWeather(String jsonString){
        JSONParser jsonParser = new JSONParser();
        JSONObject jsonObject;

        // 잘못된 json인 경우를 대비한 try-catch
        try{
            jsonObject = (JSONObject) jsonParser.parse(jsonString);
        } catch (ParseException e){
            throw new RuntimeException();
        }
        Map<String, Object> resultMap = new HashMap<>();

        JSONObject mainData = (JSONObject) jsonObject.get("main");
        JSONArray weatherArray = (JSONArray) jsonObject.get("weather");
        JSONObject weatherData = (JSONObject) weatherArray.get(0);
        resultMap.put("temp", mainData.get("temp"));
        resultMap.put("main", weatherData.get("main"));
        resultMap.put("icon", weatherData.get("icon"));

        return resultMap;
    }

    @Transactional(readOnly = true)
    public List<Diary> readDiary(LocalDate date){
        return diaryRepository.findAllByDate(date);
    }

    @Transactional(readOnly = true)
    public List<Diary> readDiaries(LocalDate startDate, LocalDate endDate){
        return diaryRepository.findAllByDateBetween(startDate, endDate);
    }

    public void updateDiary(LocalDate date, String text){
        // 날짜에 해당하는 일기 중 id가 먼저 있는 것을 수정한다고 가정
        Diary nowdiary = diaryRepository.getFirstByDate(date);
        nowdiary.setText(text); // 내용만 바꾸고
        diaryRepository.save(nowdiary);
        // !주의할 것 : nowdiary는 id는 변하지 않고 text만 변했는데,
        // 이 경우 새로운 row가 추가되는 것이 아니라 기존 데이터에 덮어씌워진다
        // (=save가 update의 역할도 해줌)
    }


    public void deleteDiary(LocalDate date){
        // 날짜에 해당하는 일기를 모두 지운다고 가정
        diaryRepository.deleteAllByDate(date);
    }
}