package rs.djokafioka.news.data.repository.dataSourceImpl

import kotlinx.coroutines.flow.Flow
import rs.djokafioka.news.data.db.ArticleDao
import rs.djokafioka.news.data.model.Article
import rs.djokafioka.news.data.repository.dataSource.NewsLocalDataSource

class NewsLocalDataSourceImpl(
    private val articleDao: ArticleDao
) : NewsLocalDataSource {
    override suspend fun saveArticleToDB(article: Article) {
        articleDao.insert(article)
    }

    override fun getSavedArticles(): Flow<List<Article>> {
        return articleDao.getSavedArticles()
    }

    override suspend fun deleteArticle(article: Article) {
        return articleDao.deleteArticle(article)
    }
}