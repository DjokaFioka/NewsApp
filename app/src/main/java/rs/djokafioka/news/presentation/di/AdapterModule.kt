package rs.djokafioka.news.presentation.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import rs.djokafioka.news.presentation.adapter.NewsAdapter
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AdapterModule {

    @Singleton
    @Provides
    fun provideNewsAdapter(): NewsAdapter {
        return NewsAdapter()
    }
}