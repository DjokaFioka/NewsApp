package rs.djokafioka.news.presentation.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import rs.djokafioka.news.data.db.ArticleDao
import rs.djokafioka.news.data.repository.dataSource.NewsLocalDataSource
import rs.djokafioka.news.data.repository.dataSourceImpl.NewsLocalDataSourceImpl
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class LocalDataModule {

    @Singleton
    @Provides
    fun provideLocalDataSource(articleDao: ArticleDao): NewsLocalDataSource {
        return NewsLocalDataSourceImpl(articleDao)
    }
}