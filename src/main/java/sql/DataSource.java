package sql;

import lombok.extern.slf4j.Slf4j;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

@Slf4j
public class DataSource {

    static Connection conn = null;

    public Connection open() {
        // 연결 정보를 수정하여 개발 서버에 대량의 더미데이터를 추가할 수 있습니다.
        String driver = "com.mysql.cj.jdbc.Driver";
        String url = "jdbc:mysql://localhost:3306/gdsc";
        String password = "1234";
        String username = "root";
        // Load a JDBC driver
        try {

            Class.forName(driver);
        } catch(ClassNotFoundException e) {
            log.error(e.getMessage());
        }

        // Make a connection
        try {
            conn = DriverManager.getConnection(url, username, password);
        }
        catch (SQLException e) {
            log.error(e.getMessage());
        }
        return conn;
    }

    public void free() {
        // release resource
        try {
            conn.close();
        } catch (SQLException e) {
            log.error(e.getMessage());
        }
    }
}
