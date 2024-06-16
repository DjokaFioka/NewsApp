package rs.djokafioka.news.data.repository

import kotlinx.coroutines.flow.Flow
import retrofit2.Response
import rs.djokafioka.news.data.model.APIResponse
import rs.djokafioka.news.data.model.Article
import rs.djokafioka.news.data.repository.dataSource.NewsLocalDataSource
import rs.djokafioka.news.data.repository.dataSource.NewsRemoteDataSource
import rs.djokafioka.news.data.util.Resource
import rs.djokafioka.news.domain.repository.NewsRepository

class NewsRepositoryImpl(
    private val newsRemoteDataSource: NewsRemoteDataSource,
    private val newsLocalDataSource: NewsLocalDataSource
) : NewsRepository {
    override suspend fun getNewsHeadlines(country: String, page: Int): Resource<APIResponse> {
        return responseToResource(newsRemoteDataSource.getTopHeadlines(country, page))
    }

    override suspend fun getSearchedNews(
        country: String,
        searchQuery: String,
        page: Int
    ): Resource<APIResponse> {
        return responseToResource(newsRemoteDataSource.getSearchedNews(country, searchQuery, page))
    }

    override suspend fun saveNews(article: Article) {
        newsLocalDataSource.saveArticleToDB(article)
    }

    override suspend fun deleteNews(article: Article) {
        newsLocalDataSource.deleteArticle(article)
    }

    override fun getSavedNews(): Flow<List<Article>> {
        return newsLocalDataSource.getSavedArticles()
    }

    private fun responseToResource(response: Response<APIResponse>): Resource<APIResponse> {
        if (response.isSuccessful) {
            response.body()?.let { result ->
                return Resource.Success(result)
            }
        }
        return Resource.Error(response.message())
    }
}