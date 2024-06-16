package rs.djokafioka.news.presentation.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import rs.djokafioka.news.domain.repository.NewsRepository
import rs.djokafioka.news.domain.usecase.DeleteSavedNewsUseCase
import rs.djokafioka.news.domain.usecase.GetNewsHeadlinesUseCase
import rs.djokafioka.news.domain.usecase.GetSavedNewsUseCase
import rs.djokafioka.news.domain.usecase.GetSearchedNewsUseCase
import rs.djokafioka.news.domain.usecase.SaveNewsUseCase
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class UseCaseModule {

    @Singleton
    @Provides
    fun provideGetNewsHeadlinesUseCase(
        newsRepository: NewsRepository
    ): GetNewsHeadlinesUseCase {
        return GetNewsHeadlinesUseCase(newsRepository)
    }

    @Singleton
    @Provides
    fun provideGetSearchedNewsUseCase(
        newsRepository: NewsRepository
    ): GetSearchedNewsUseCase {
        return GetSearchedNewsUseCase(newsRepository)
    }

    @Singleton
    @Provides
    fun provideSaveNewsUseCase(
        newsRepository: NewsRepository
    ): SaveNewsUseCase {
        return SaveNewsUseCase(newsRepository)
    }

    @Singleton
    @Provides
    fun provideGetSavedNewsUseCase(
        newsRepository: NewsRepository
    ): GetSavedNewsUseCase {
        return GetSavedNewsUseCase(newsRepository)
    }

    @Singleton
    @Provides
    fun provideDeleteSavedNewsUseCase(
        newsRepository: NewsRepository
    ): DeleteSavedNewsUseCase {
        return DeleteSavedNewsUseCase(newsRepository)
    }

}