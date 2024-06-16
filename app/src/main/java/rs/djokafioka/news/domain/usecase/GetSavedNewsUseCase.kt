package rs.djokafioka.news.domain.usecase

import kotlinx.coroutines.flow.Flow
import rs.djokafioka.news.data.model.Article
import rs.djokafioka.news.domain.repository.NewsRepository


class GetSavedNewsUseCase(private val newsRepository: NewsRepository) {
    fun execute(): Flow<List<Article>> {
        return newsRepository.getSavedNews()
    }
}