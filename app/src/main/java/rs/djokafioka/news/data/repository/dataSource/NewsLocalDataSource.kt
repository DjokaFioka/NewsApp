package rs.djokafioka.news.data.repository.dataSource

import kotlinx.coroutines.flow.Flow
import rs.djokafioka.news.data.model.Article

interface NewsLocalDataSource {
    suspend fun saveArticleToDB(article: Article)
    fun getSavedArticles(): Flow<List<Article>>
    suspend fun deleteArticle(article: Article)
}