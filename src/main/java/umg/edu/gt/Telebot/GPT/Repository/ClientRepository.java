package umg.edu.gt.Telebot.GPT.Repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import umg.edu.gt.Telebot.GPT.Model.Client;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@Repository
public class ClientRepository {

    private static final String TABLENAME = "clients";
    private static DataSource dataSource;

    @Autowired
    public ClientRepository(DataSource dataSource) {
        this.dataSource = dataSource;
    }


    public static void add(String name, Long chat_id) throws SQLException {
        String sql = "INSERT INTO " + TABLENAME + " (name, chat_id) VALUES (?, ?)";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, name);
            stmt.setLong(2, chat_id);
            stmt.executeUpdate();
        }
    }

    public static Client getById(Long chat_id) throws SQLException {
        String sql = "SELECT * FROM " + TABLENAME + " WHERE chat_id = ?";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setLong(1, chat_id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new Client(rs.getInt("client"), rs.getString("name"));
            }
        }
        return null;
    }
}