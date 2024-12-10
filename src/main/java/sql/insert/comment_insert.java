package sql.insert;

import lombok.extern.slf4j.Slf4j;
import sql.DataSource;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDateTime;

@Slf4j
public class comment_insert {
    // POST 마다 MAX_GROUP 개의 댓글, 각 CHILD_PER_GROUP 개의 대댓글, 작성자는 MAX_MEMBER 에서 순차 할당
    private static final int MAX_POST = 1000;
    private static final int MAX_MEMBER = 100;
    private static final int MAX_GROUP = 10;
    private static final int CHILD_PER_GROUP = 100;

    public static void main(String[] args) throws SQLException {
        DataSource dataSource = new DataSource();
        Connection conn = dataSource.open();
        String psql = "INSERT INTO comment(id, post_id, author_id, parent_id, content, author_name, author_profile, create_at, modified_at) " +
                      "VALUES (?,?,?,?,?,?,?,?,?)";
        PreparedStatement stmt = conn.prepareStatement(psql);

        try {
            insertPost(stmt);
            conn.setAutoCommit(false);
            stmt.executeBatch();
            conn.commit();
            log.info("success for insert comment");
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


    private static void insertPost(PreparedStatement stmt) throws SQLException {
        int id = 1;
        int group = 1;
        for (int postId=1; postId<=MAX_POST; postId++) {
            for (int groupId=1; groupId<=MAX_GROUP; groupId++) {
                for (int childId=1; childId<=CHILD_PER_GROUP; childId++) {
                    stmt.setLong(1, id++);
                    stmt.setLong(2, postId);
                    stmt.setLong(3, id%MAX_MEMBER+1);
                    stmt.setLong(4, group);
                    stmt.setString(5, "댓글내용");
                    stmt.setString(6, String.format("작성자%d", id%MAX_MEMBER+1));
                    stmt.setString(7, "https://via.placeholder.com/640x480");
                    stmt.setString(8, LocalDateTime.now().plusMinutes(id).toString());
                    stmt.setString(9, LocalDateTime.now().plusMinutes(id).toString());
                    stmt.addBatch();
                }
                group+=CHILD_PER_GROUP;
            }
        }
    }
}
