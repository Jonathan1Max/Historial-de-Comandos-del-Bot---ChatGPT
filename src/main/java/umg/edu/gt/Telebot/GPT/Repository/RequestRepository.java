package umg.edu.gt.Telebot.GPT.Repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.dao.EmptyResultDataAccessException;

@Repository
public class RequestRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public String getResponseByQuestion(String question) {
        String sql = "SELECT response FROM requests WHERE question = ?";
        try {
            
            return jdbcTemplate.queryForObject(sql, new Object[]{question}, String.class);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    public void saveRequest(String question, String response) {
        String sql = "INSERT INTO requests (question, response) VALUES (?, ?)";
        jdbcTemplate.update(sql, question, response);
    }
}