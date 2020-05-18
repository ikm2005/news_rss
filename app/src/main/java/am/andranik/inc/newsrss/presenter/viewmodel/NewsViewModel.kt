package am.andranik.inc.newsrss.presenter.viewmodel

import am.andranik.inc.newsrss.models.NewsUI
import am.andranik.inc.newsrss.repository.NewsRepository
import am.andranik.inc.newsrss.repository.util.Status
import am.andranik.inc.newsrss.util.SingleLiveEvent
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class NewsViewModel(private val newsRepository: NewsRepository) : BaseViewModel() {

    private val _news = MutableLiveData<List<NewsUI>>()
    private val _errorMessage = SingleLiveEvent<String>()
    val news = _news as LiveData<List<NewsUI>>
    val message = _errorMessage as LiveData<String>

    init {
        loadNews(true)
    }

    fun loadNews(isRefresh: Boolean = false) {
        if (isRefresh) {
            _loading.value = true
        }
        viewModelScope.launch(Dispatchers.IO) {
            val result = newsRepository.getNewsList()
            withContext(Dispatchers.Main) {
                _loading.value = false
                when (result.status) {
                    Status.SUCCESS -> {
                        _news.value = result.data
                    }
                    Status.ERROR -> _errorMessage.value = result.message
                    else -> _errorMessage.value = result.message
                }
            }
        }
    }

}