package rs.djokafioka.news.presentation.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import rs.djokafioka.news.data.api.NewsAPIService
import rs.djokafioka.news.data.repository.dataSource.NewsRemoteDataSource
import rs.djokafioka.news.data.repository.dataSourceImpl.NewsRemoteDataSourceImpl
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class RemoteDataModule {

    @Singleton
    @Provides
    fun provideNewsRemoteDataSource(newsAPIService: NewsAPIService): NewsRemoteDataSource {
        return NewsRemoteDataSourceImpl(newsAPIService)
    }
}