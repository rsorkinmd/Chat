import javax.net.ssl.SSLHandshakeException;
import java.sql.*;

/**
 * Created by roman on 30.07.16.
 */
public class DBHandler {
    private static final String SQL_GET_NICKNAME = "SELECT Nickname FROM main where Login=? and Password=?";
    private static final String DB_CONNECTION_URL = "jdbc:sqlite:ClientsDB.db";
    public static final String DB_DRIVER = "org.sqlite.JDBC";

    static {
        try {
            Class.forName(DB_DRIVER);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public String getNickNameByLoginAndPassword(String login, String pass) {
        try (Connection connection = DriverManager.getConnection(DB_CONNECTION_URL);
             PreparedStatement stmt = connection.prepareStatement(SQL_GET_NICKNAME)) {

            stmt.setString(1, login);
            stmt.setString(2, pass);

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return rs.getString("Nickname");
            } else {
                throw new HandshakeException(String.format("Логин %s - Пароль не верный или данного пользователя не существует", login));
            }
        } catch (SQLException e) {
            throw new HandshakeException(e.getMessage(), e.getCause());
        }
    }
}
