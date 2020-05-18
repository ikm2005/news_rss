package am.andranik.inc.newsrss.presenter.viewmodel

import am.andranik.inc.newsrss.models.NewsUI
import am.andranik.inc.newsrss.repository.NewsRepository
import am.andranik.inc.newsrss.repository.util.Status
import am.andranik.inc.newsrss.util.SingleLiveEvent
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class NewsViewModel(private val newsRepository: NewsRepository) : ViewModel() {

    private val _news = MutableLiveData<List<NewsUI>>()
    private val _errorMessage = SingleLiveEvent<String>()
    val news = _news as LiveData<List<NewsUI>>
    val message = _errorMessage as LiveData<String>

    init {
        loadNews()
    }

    fun loadNews() {
        viewModelScope.launch(Dispatchers.IO) {
            val result = newsRepository.getNewsList()
            withContext(Dispatchers.Main) {
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