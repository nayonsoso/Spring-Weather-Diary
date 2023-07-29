package com.example.weather.repository;

import com.example.weather.domain.Memo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;
import java.util.Optional;

@Repository
public class JdbcMemoRepository {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public JdbcMemoRepository(DataSource dataSource){
        // application.properties에 있는 정보들로 jdbcTemplate을 만든다는 뜻
        jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public Memo save(Memo memo){
        String sql = "INSERT INTO memo VALUES(?,?)";
        jdbcTemplate.update(sql, memo.getId(), memo.getText());
        return memo;
    }

    // SELECT를 해온 결과인 ResultSet을 RowMapper<Memo>로 바꿔주는 함수
    // ResultSet형식 : {id=1, text="This is Memo."}
    private RowMapper<Memo> memoRowMapper(){
        return (rs, rowNum) -> new Memo(
                rs.getInt("id"),
                rs.getString("text")
        );
    }
    public List<Memo> findAll(){
        String sql = "SELECT * FROM memo";
        // sql 쿼리를 던지고, 결과를 memoRowMapper()를 이용해서 Memo객체로 가져온다는 뜻
        return jdbcTemplate.query(sql, memoRowMapper());
    }

    // 반환되는 것이 null 일 수 있으므로 stream.find와 Optional을 이용하는 함수
    public Optional<Memo> findById(int id){
        String sql = "SELECT * FROM memo WHERE id=?";
        return jdbcTemplate.query(sql, memoRowMapper(), id).stream().findFirst();
    }
}
