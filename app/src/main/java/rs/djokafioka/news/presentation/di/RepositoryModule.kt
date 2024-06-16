package rs.djokafioka.news.presentation.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import rs.djokafioka.news.data.repository.NewsRepositoryImpl
import rs.djokafioka.news.data.repository.dataSource.NewsLocalDataSource
import rs.djokafioka.news.data.repository.dataSource.NewsRemoteDataSource
import rs.djokafioka.news.domain.repository.NewsRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class RepositoryModule {

    @Singleton
    @Provides
    fun provideNewsRepository(
        newsRemoteDataSource: NewsRemoteDataSource,
        newsLocalDataSource: NewsLocalDataSource
    ): NewsRepository {
        return NewsRepositoryImpl(newsRemoteDataSource, newsLocalDataSource)
    }
}