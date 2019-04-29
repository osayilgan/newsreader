package ai.jobox.assignment.database;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface ArticleDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertOrUpdate(List<Article> articles);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertOrUpdate(Article article);

    @Query("SELECT * FROM article ORDER BY publishedAt DESC")
    List<Article> getArticlesByDate();

    @Query("SELECT * FROM article")
    List<Article> getArticles();

    @Query("DELETE FROM article")
    void deleteAllArticles();
}
