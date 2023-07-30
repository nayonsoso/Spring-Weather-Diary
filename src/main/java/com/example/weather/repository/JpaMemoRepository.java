package com.example.weather.repository;

import com.example.weather.domain.Memo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

// JpaRepository는 ORM에 필요한 함수들을 미리 정의해놓은 인터페이스
// DB에 접근해서 쿼리를 실행할 함수를 직접 정의할 필요가 없어진다.
@Repository
public interface JpaMemoRepository extends JpaRepository<Memo, Integer> {
    // 위 코드의 <Memo, Integer>에서 Memo는 jpa가 다룰 객체 타입, Integer은 객체를 식별할 수 있는 id의 자료형
}