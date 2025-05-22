import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexionDB {

    private static final String URl = "jdbc:mysql://proyecto.cqdeymjatzl3.us-east-1.rds.amazonaws.com:3306/Cruce_De_Caminos";
    private static final String USER = "admin";
    private static final String PASSWORD = "Admin.12345678";

    public static Connection getConnection() throws SQLException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver"); // cargar el driver
        } catch (ClassNotFoundException e) {
            throw new SQLException("Error al cargar el driver JDBC", e);
        }

        return DriverManager.getConnection(URl, USER, PASSWORD);
    }
}


