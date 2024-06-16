package rs.djokafioka.news.presentation.di

import android.app.Application
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import rs.djokafioka.news.domain.usecase.DeleteSavedNewsUseCase
import rs.djokafioka.news.domain.usecase.GetNewsHeadlinesUseCase
import rs.djokafioka.news.domain.usecase.GetSavedNewsUseCase
import rs.djokafioka.news.domain.usecase.GetSearchedNewsUseCase
import rs.djokafioka.news.domain.usecase.SaveNewsUseCase
import rs.djokafioka.news.presentation.viewmodel.NewsViewModelFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class FactoryModule {

    @Singleton
    @Provides
    fun provideNewsViewModelFactory(
        application: Application,
        getNewsHeadlinesUseCase: GetNewsHeadlinesUseCase,
        getSearchedNewsUseCase: GetSearchedNewsUseCase,
        saveNewsUseCase: SaveNewsUseCase,
        getSavedNewsUseCase: GetSavedNewsUseCase,
        deleteSavedNewsUseCase: DeleteSavedNewsUseCase
    ): NewsViewModelFactory {
        return NewsViewModelFactory(
            application,
            getNewsHeadlinesUseCase,
            getSearchedNewsUseCase,
            saveNewsUseCase,
            getSavedNewsUseCase,
            deleteSavedNewsUseCase
        )
    }
}