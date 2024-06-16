package rs.djokafioka.news.data.repository.dataSource

import retrofit2.Response
import rs.djokafioka.news.data.model.APIResponse

interface NewsRemoteDataSource {
    suspend fun getTopHeadlines(country: String, page: Int): Response<APIResponse>
    suspend fun getSearchedNews(country: String, searchQuery: String, page: Int): Response<APIResponse>
}