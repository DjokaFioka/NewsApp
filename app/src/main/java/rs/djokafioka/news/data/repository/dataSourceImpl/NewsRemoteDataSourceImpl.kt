package rs.djokafioka.news.data.repository.dataSourceImpl

import retrofit2.Response
import rs.djokafioka.news.data.api.NewsAPIService
import rs.djokafioka.news.data.model.APIResponse
import rs.djokafioka.news.data.repository.dataSource.NewsRemoteDataSource

class NewsRemoteDataSourceImpl(
    private val newsAPIService: NewsAPIService
) : NewsRemoteDataSource {
    override suspend fun getTopHeadlines(country: String, page: Int): Response<APIResponse> {
        return newsAPIService.getTopHeadlines(country = country, page = page)
    }

    override suspend fun getSearchedNews(
        country: String,
        searchQuery: String,
        page: Int
    ): Response<APIResponse> {
        return newsAPIService.getSearchedTopHeadlines(country, searchQuery, page)
    }
}