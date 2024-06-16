package rs.djokafioka.news.domain.usecase

import rs.djokafioka.news.data.model.APIResponse
import rs.djokafioka.news.data.util.Resource
import rs.djokafioka.news.domain.repository.NewsRepository

class GetSearchedNewsUseCase(private val newsRepository: NewsRepository) {
    suspend fun execute(country: String, searchQuery: String, page: Int): Resource<APIResponse> {
        return newsRepository.getSearchedNews(country, searchQuery, page)
    }
}