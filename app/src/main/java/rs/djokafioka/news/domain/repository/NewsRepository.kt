package rs.djokafioka.news.domain.repository

import kotlinx.coroutines.flow.Flow
import rs.djokafioka.news.data.model.APIResponse
import rs.djokafioka.news.data.model.Article
import rs.djokafioka.news.data.util.Resource

interface NewsRepository {

    suspend fun getNewsHeadlines(country: String, page: Int): Resource<APIResponse>
    suspend fun getSearchedNews(country: String, searchQuery: String, page: Int): Resource<APIResponse>
    suspend fun saveNews(article: Article)
    suspend fun deleteNews(article: Article)
    fun getSavedNews(): Flow<List<Article>>
}