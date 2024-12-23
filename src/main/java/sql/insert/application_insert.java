package sql.insert;

import com.gdsc_knu.official_homepage.entity.enumeration.ApplicationStatus;
import com.gdsc_knu.official_homepage.entity.enumeration.Track;
import lombok.extern.slf4j.Slf4j;
import sql.DataSource;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDateTime;

@Slf4j
public class application_insert {
    private static final int MAX = 10000;
    private static final int HALF = MAX/2;

    public static void main(String[] args) throws SQLException {
        DataSource dataSource = new DataSource();
        Connection conn = dataSource.open();
        String psql = "INSERT IGNORE INTO application(id, name, student_number, email, application_status, is_opened, is_marked, track, phone_number, tech_stack, links, note, major, create_at, modified_at) " +
                      "VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        PreparedStatement stmt = conn.prepareStatement(psql);

        try {
            insertApplication(stmt);
            conn.setAutoCommit(false);
            stmt.executeBatch();
            conn.commit();
            log.info("success for insert application");
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

    public static void insertApplication(PreparedStatement stmt) throws SQLException {
        for (int i=1; i<=MAX; i++) {
            stmt.setLong(1, i);
            stmt.setString(2, String.format("이름%d",i));
            stmt.setString(3, String.format("20240%d",i));
            stmt.setString(4, String.format("email%d@email.com",i));
            stmt.setString(5, getStatus(i));
            stmt.setBoolean(6, i < HALF);
            stmt.setBoolean(7, i < HALF);
            stmt.setString(8, getTrack(i));
            stmt.setString(9,String.format("010-0000-%d",i));
            stmt.setString(10, "Java, js, SpringBoot");
            stmt.setString(11, "https://gdsc-knu.com");
            stmt.setString(12, "메모입니다.");
            stmt.setString(13, "컴퓨터학부");
            stmt.setString(14, LocalDateTime.now().plusMinutes(i).toString());
            stmt.setString(15, LocalDateTime.now().plusMinutes(i).toString());
            stmt.addBatch();
        }
    }

    static int statusSize = ApplicationStatus.values().length;
    static int trackSize = Track.values().length;
    private static String getTrack(int i) {
        return Track.values()[i%trackSize].name();
    }
    private static String getStatus(int i) {
        return ApplicationStatus.values()[i%statusSize].name();
    }
}
