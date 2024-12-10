package sql.insert;

import com.gdsc_knu.official_homepage.entity.post.enumeration.Category;
import com.gdsc_knu.official_homepage.entity.post.enumeration.PostStatus;
import lombok.extern.slf4j.Slf4j;
import sql.DataSource;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDateTime;

@Slf4j
public class post_insert {
    // MAX_MEMBER 마다 MAX 개씩 post 작성 (10만)
    private static final int MAX = 1000;
    private static final int MAX_MEMBER = 100;


    public static void main(String[] args) throws SQLException {
        DataSource dataSource = new DataSource();
        Connection conn = dataSource.open();
        String psql = "INSERT INTO post(id, title, content, member_id, category, status, like_count, comment_count, shared_count, thumbnail_url, modified_at, published_at) " +
                "VALUES (?,?,?,?,?,?,?,?,?,?,?,?)";
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
        int id = 1;
        for (int memberId=1; memberId<=MAX_MEMBER; memberId ++) {
            for (int postCount=1; postCount<=MAX; postCount++) {
                stmt.setLong(1, id++);
                stmt.setString(2, "제목");
                stmt.setString(3, "본문내용");
                stmt.setLong(4, memberId);
                stmt.setString(5, getCategory(postCount));
                stmt.setString(6, getStatus(postCount));
                stmt.setInt(7,0);
                stmt.setInt(8,0);
                stmt.setInt(9,0);
                stmt.setString(10, "https://via.placeholder.com/640x480");
                stmt.setString(11, LocalDateTime.now().plusMinutes(postCount).toString());
                stmt.setString(12, LocalDateTime.now().plusMinutes(postCount).toString());
                stmt.addBatch();
            }
        }
    }

    private static String getStatus(int i) {
        return i % 5 == 0 ? PostStatus.TEMPORAL.name() : PostStatus.SAVED.name();
    }

    private static final int categorySize = Category.values().length;
    private static String getCategory(int i) {
        return Category.values()[i%categorySize].name();
    }
}
