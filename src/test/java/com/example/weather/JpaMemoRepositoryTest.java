package com.example.weather;

import com.example.weather.domain.Memo;
import com.example.weather.repository.JpaMemoRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class JpaMemoRepositoryTest {
    @Autowired
    JpaMemoRepository jpaMemoRepository;

    @Test
    void insertMemoTest(){
        // given
        Memo newMemo = new Memo(10, "This is jpa Memo.");
        // when
        jpaMemoRepository.save(newMemo); // jpa repository 안에 어떤 함수도 구현하지 않았는데, DB에 저장할 수 있다.
        // then
        List<Memo> memoList = jpaMemoRepository.findAll();
        assertTrue(memoList.size() > 0);
    }

    @Test
    void findByIdTest(){
        // given
        Memo newMemo = new Memo(11, "jpa");
        // 주의 : 우리는 11이라 지정했지만, 실제로 id가 11이 아닐 수 있다.
        // id의 속성을 AutoIncrement로 설정했기 때문에,
        // mySql 내부에서 자동으로 증가하는 id를 할당받았을 것이기 때문!
        // 따라서, 저장한 객체를 리턴받아서, 그 객체의 id로 검색해야 함

        // when
        Memo memo = jpaMemoRepository.save(newMemo);
        // then
        Optional<Memo> result = jpaMemoRepository.findById(memo.getId());
        assertEquals(result.get().getText(), "jpa");
    }
}