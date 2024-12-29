package sql.insert;

import com.gdsc_knu.official_homepage.entity.enumeration.Track;
import lombok.extern.slf4j.Slf4j;
import sql.DataSource;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

@Slf4j
public class member_insert {
    private static final int MAX = 100000; // 10만

    public static void main(String[] args) throws SQLException {
        DataSource dataSource = new DataSource();
        Connection conn = dataSource.open();
        String psql = "INSERT IGNORE INTO member(id, name, age, email, phone_number, role, track, student_number, major, profile_url) " +
                      "VALUES (?,?,?,?,?,?,?,?,?,?)";
        PreparedStatement stmt = conn.prepareStatement(psql);

        try {
            insertMember(stmt);
            conn.setAutoCommit(false);
            stmt.executeBatch();
            conn.commit();
            log.info("success for insert member");
        }
        catch (SQLException e) {
            conn.rollback();
            log.error(e.getMessage());
        }
        finally {
            conn.setAutoCommit(true);
            dataSource.free();
            stmt.close();
        }
    }


    private static void insertMember(PreparedStatement stmt) throws SQLException {
        for (int i=1; i<=MAX; i++) {
            stmt.setLong(1, i);
            stmt.setString(2, String.format("이름%d",i));
            stmt.setInt(3,20);
            stmt.setString(4, String.format("email%d@email.com",i));
            stmt.setString(5, String.format("010-0000-%d",i));
            stmt.setString(6, "ROLE_MEMBER");
            stmt.setString(7, getTrack(i));
            stmt.setString(8, String.format("2024%d",i));
            stmt.setString(9,"컴퓨터학부");
            stmt.setString(10, "https://via.placeholder.com/640x480");
            stmt.addBatch();
        }
    }


    static int trackSize = Track.values().length;
    private static String getTrack(int i) {
        return Track.values()[i%trackSize].name();
    }

}