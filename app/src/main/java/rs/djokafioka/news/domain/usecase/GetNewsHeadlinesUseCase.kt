package rs.djokafioka.news.domain.usecase

import rs.djokafioka.news.data.model.APIResponse
import rs.djokafioka.news.data.util.Resource
import rs.djokafioka.news.domain.repository.NewsRepository

class GetNewsHeadlinesUseCase(private val newsRepository: NewsRepository) {
    suspend fun execute(country: String, page: Int): Resource<APIResponse> {
        return newsRepository.getNewsHeadlines(country, page)
    }
}