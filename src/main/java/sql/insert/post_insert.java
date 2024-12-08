package sql.insert;

import com.gdsc_knu.official_homepage.entity.post.enumeration.Category;
import com.gdsc_knu.official_homepage.entity.post.enumeration.PostStatus;
import lombok.extern.slf4j.Slf4j;
import sql.DataSource;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

@Slf4j
public class post_insert {
    private static final int MAX = 1000;
    private static final int MAX_MEMBER = 100;


    public static void main(String[] args) throws SQLException {
        DataSource dataSource = new DataSource();
        Connection conn = dataSource.open();
        String psql = "INSERT IGNORE INTO post(id, title, content, member_id, category, status, like_count, comment_count, shared_count) " +
                "VALUES (?,?,?,?,?,?,?,?,?)";
        PreparedStatement stmt = conn.prepareStatement(psql);

        try {
            insertPost(stmt);
            conn.setAutoCommit(false);
            stmt.executeBatch();
            conn.commit();
            log.info("success for insert post");
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
        for (int i=1; i<=MAX; i++) {
            stmt.setLong(1, i);
            stmt.setString(2, "제목");
            stmt.setString(3, "본문내용");
            stmt.setLong(4, i%MAX_MEMBER+1);
            stmt.setString(5, getCategory(i));
            stmt.setString(6, getStatus(i));
            stmt.setInt(7,0);
            stmt.setInt(8,0);
            stmt.setInt(9,0);
            stmt.addBatch();
        }
    }

    private static String getStatus(int i) {
        return i < 100 ? PostStatus.TEMPORAL.name() : PostStatus.SAVED.name();
    }

    private static final int categorySize = Category.values().length;
    private static String getCategory(int i) {
        return Category.values()[i%categorySize].name();
    }
}
