package rs.djokafioka.news.domain.usecase

import rs.djokafioka.news.data.model.Article
import rs.djokafioka.news.domain.repository.NewsRepository

class DeleteSavedNewsUseCase(private val newsRepository: NewsRepository) {
    suspend fun execute(article: Article) = newsRepository.deleteNews(article)
}