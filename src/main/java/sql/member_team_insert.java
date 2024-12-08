package sql;

import lombok.extern.slf4j.Slf4j;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

@Slf4j
public class member_team_insert {
    private static final int MAX_MEMBER = 1000;
    private static final int MAX_PARENT = 100;
    private static final int CHILD_PER_PARENT = 100;

    public static void main(String[] args) throws SQLException {
        DataSource dataSource = new DataSource();
        Connection conn = dataSource.open();
        String psql = "INSERT INTO member_team(id, member_id, team_id) VALUES (?,?,?)";
        PreparedStatement stmt = conn.prepareStatement(psql);

        try {
            insertMemberTeam(stmt);
            conn.setAutoCommit(false);
            stmt.executeBatch();
            conn.commit();
            log.info("success for insert member_team");
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

    public static void insertMemberTeam(PreparedStatement stmt) throws SQLException {
        int id = 1;
        for (int memberID = 1; memberID <= MAX_MEMBER; memberID ++) {
            for (int teamID = memberID; teamID <= MAX_PARENT*CHILD_PER_PARENT; teamID+=CHILD_PER_PARENT) {
                stmt.setLong(1, id++);
                stmt.setLong(2, memberID);
                stmt.setLong(3, teamID);
                stmt.addBatch();
            }
        }
    }
}
