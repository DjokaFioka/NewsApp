package rs.djokafioka.news.data.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import rs.djokafioka.news.data.model.Article

@Dao
interface ArticleDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(article: Article)

    @Query("SELECT * FROM articles")
    fun getSavedArticles(): Flow<List<Article>>

    @Delete
    suspend fun deleteArticle(article: Article)
}