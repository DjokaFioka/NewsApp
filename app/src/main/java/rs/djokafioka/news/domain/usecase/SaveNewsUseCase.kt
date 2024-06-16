package rs.djokafioka.news.domain.usecase

import rs.djokafioka.news.data.model.Article
import rs.djokafioka.news.domain.repository.NewsRepository

class SaveNewsUseCase(private val newsRepository: NewsRepository) {
    suspend fun execute(article: Article) = newsRepository.saveNews(article)
}