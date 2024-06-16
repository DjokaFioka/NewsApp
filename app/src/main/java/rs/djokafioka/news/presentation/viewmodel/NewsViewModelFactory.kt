package rs.djokafioka.news.presentation.viewmodel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import rs.djokafioka.news.domain.usecase.DeleteSavedNewsUseCase
import rs.djokafioka.news.domain.usecase.GetNewsHeadlinesUseCase
import rs.djokafioka.news.domain.usecase.GetSavedNewsUseCase
import rs.djokafioka.news.domain.usecase.GetSearchedNewsUseCase
import rs.djokafioka.news.domain.usecase.SaveNewsUseCase

class NewsViewModelFactory(
    private val app: Application,
    private val getNewsHeadlinesUseCase: GetNewsHeadlinesUseCase,
    private val getSearchedNewsUseCase: GetSearchedNewsUseCase,
    private val saveNewsUseCase: SaveNewsUseCase,
    private val getSavedNewsUseCase: GetSavedNewsUseCase,
    private val deleteSavedNewsUseCase: DeleteSavedNewsUseCase
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return NewsViewModel(
            app,
            getNewsHeadlinesUseCase,
            getSearchedNewsUseCase,
            saveNewsUseCase,
            getSavedNewsUseCase,
            deleteSavedNewsUseCase
        ) as T
    }
}