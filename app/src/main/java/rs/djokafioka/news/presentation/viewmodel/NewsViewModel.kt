package rs.djokafioka.news.presentation.viewmodel

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import rs.djokafioka.news.R
import rs.djokafioka.news.data.model.APIResponse
import rs.djokafioka.news.data.model.Article
import rs.djokafioka.news.data.util.Resource
import rs.djokafioka.news.data.util.SnackbarData
import rs.djokafioka.news.domain.usecase.DeleteSavedNewsUseCase
import rs.djokafioka.news.domain.usecase.GetNewsHeadlinesUseCase
import rs.djokafioka.news.domain.usecase.GetSavedNewsUseCase
import rs.djokafioka.news.domain.usecase.GetSearchedNewsUseCase
import rs.djokafioka.news.domain.usecase.SaveNewsUseCase

class NewsViewModel(
    private val app: Application,
    private val getNewsHeadlinesUseCase: GetNewsHeadlinesUseCase,
    private val getSearchedNewsUseCase: GetSearchedNewsUseCase,
    private val saveNewsUseCase: SaveNewsUseCase,
    private val getSavedNewsUseCase: GetSavedNewsUseCase,
    private val deleteSavedNewsUseCase: DeleteSavedNewsUseCase
) : AndroidViewModel(app) { //AndroidViewModel instead of ViewModel since we need the context for checking the internet connection
    val newsHeadlines: MutableLiveData<Resource<APIResponse>> = MutableLiveData()
    private var newsHeadlinesList = mutableListOf<Article>()
    private val _snackBarSaveFlow = MutableSharedFlow<Int>()
    val snackBarSaveFlow = _snackBarSaveFlow.asSharedFlow()
    private val _snackBarDataFlow = MutableSharedFlow<SnackbarData<Article>>()
    val snackBarDataFlow = _snackBarDataFlow.asSharedFlow()

    fun getNewsHeadlines(country: String, page: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            newsHeadlines.postValue(Resource.Loading())
            try {
                if (isNetworkAvailable(app)) {
                    val apiResult = getNewsHeadlinesUseCase.execute(country, page)
                    apiResult.data?.articles?.let {
                        newsHeadlinesList.addAll(it)
                    }
                    apiResult.data?.articles = newsHeadlinesList
                    newsHeadlines.postValue(apiResult)
                } else {
                    newsHeadlines.postValue(Resource.Error("Internet connection is not available"))
                }
            } catch (e: Exception) {
                newsHeadlines.postValue(Resource.Error(e.message.toString()))
            }
        }
    }

    @Suppress("DEPRECATION")
    private fun isNetworkAvailable(context: Context?): Boolean {
        var result = false
        val cm = context?.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager?
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            cm?.run {
                cm.getNetworkCapabilities(cm.activeNetwork)?.run {
                    result = when {
                        hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                        hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                        hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
                        else -> false
                    }
                }
            }
        } else {
            cm?.run {
                cm.activeNetworkInfo?.run {
                    if (type == ConnectivityManager.TYPE_WIFI) {
                        result = true
                    } else if (type == ConnectivityManager.TYPE_MOBILE) {
                        result = true
                    }
                }
            }
        }
        return result
    }

    //Search
    val searchedNews: MutableLiveData<Resource<APIResponse>> = MutableLiveData()

    fun searchNews(
        country: String,
        searchQuery: String,
        page: Int
    ) = viewModelScope.launch {
        searchedNews.postValue(Resource.Loading())

        try {
            if (isNetworkAvailable(app)) {
                val response = getSearchedNewsUseCase.execute(
                    country,
                    searchQuery,
                    page
                )
                searchedNews.postValue(response)
            } else {
                searchedNews.postValue(Resource.Error("Internet connection is not available"))
            }
        } catch (e: Exception) {
            searchedNews.postValue(Resource.Error(e.message.toString()))
        }
    }

    //Local data
    fun saveArticle(article: Article) = viewModelScope.launch {
        saveNewsUseCase.execute(article)
        viewModelScope.launch {
            _snackBarSaveFlow.emit(R.string.article_saved_success)
        }
    }

    //Locally saved news
    fun getSavedNews() = liveData {
        getSavedNewsUseCase.execute().collect {
            emit(it)
        }
    }

    //Deleting locally saved articles
    fun deleteArticle(article: Article) = viewModelScope.launch {
        deleteSavedNewsUseCase.execute(article)
        viewModelScope.launch {
            val data = SnackbarData(article, R.string.article_deleted_success)
            _snackBarDataFlow.emit(data)
        }
    }

    fun restoreArticle(article: Article) = viewModelScope.launch {
        saveNewsUseCase.execute(article)
        viewModelScope.launch {
            _snackBarSaveFlow.emit(R.string.article_restored_success)
        }
    }
}