package sql;

import lombok.extern.slf4j.Slf4j;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

@Slf4j
public class team_insert {
    private static final int MAX_PARENT = 100;
    private static final int CHILD_PER_PARENT = 100;

    public static void main(String[] args) throws SQLException {
        DataSource dataSource = new DataSource();
        Connection conn = dataSource.open();
        String psql = "INSERT INTO team(id, team_name, parent_id) VALUES (?,?,?)";
        PreparedStatement stmt = conn.prepareStatement(psql);

        try {
            insertTeam(stmt);
            conn.setAutoCommit(false);
            stmt.executeBatch();
            conn.commit();
            log.info("success for insert team");
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

    public static void insertTeam(PreparedStatement stmt) throws SQLException {
        for (int i=1; i<=MAX_PARENT; i++) {
            stmt.setLong(1, i);
            stmt.setString(2, String.format("팀이름%d",i));
            stmt.setNull(3, -5); // BIGINT
            stmt.addBatch();
        }

        for (int i=1; i<=MAX_PARENT; i++) {
            for (int j=1; j<=CHILD_PER_PARENT; j++) {
                stmt.setLong(1, i*100+j);
                stmt.setString(2, String.format("팀이름%d %d",i,j));
                stmt.setInt(3,i);
                stmt.addBatch();
            }
        }
    }
}
