package sql.insert;

import lombok.extern.slf4j.Slf4j;
import sql.DataSource;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

@Slf4j
public class application_answer_insert {
    private static final int MAX = 10000;
    private static final int HALF = MAX/2;

    public static void main(String[] args) throws SQLException {
        DataSource dataSource = new DataSource();
        Connection conn = dataSource.open();
        String psql = "INSERT INTO application_answer(id, application_id, question_number, answer) VALUES (?,?,?,?)";
        PreparedStatement stmt = conn.prepareStatement(psql);

        try {
            insertApplication(stmt);
            conn.setAutoCommit(false);
            stmt.executeBatch();
            conn.commit();
            log.info("success for insert application_answer");
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
        int id = 1;
        for (int aId=1; aId<=MAX; aId++) {
            for (int qId = 1; qId <= 4; qId++) {
                stmt.setLong(1, id++);
                stmt.setLong(2, aId);
                stmt.setLong(3, qId);
                stmt.setString(4, String.format("답변내용%d", qId));
                stmt.addBatch();
            }
        }
    }
}
