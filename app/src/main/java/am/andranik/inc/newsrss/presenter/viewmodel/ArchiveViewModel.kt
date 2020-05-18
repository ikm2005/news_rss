package am.andranik.inc.newsrss.presenter.viewmodel

import am.andranik.inc.newsrss.models.NewsUI
import am.andranik.inc.newsrss.repository.ArchiveRepository
import am.andranik.inc.newsrss.repository.util.Status
import am.andranik.inc.newsrss.util.SingleLiveEvent
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ArchiveViewModel(private val archiveRepository: ArchiveRepository) : BaseViewModel() {

    private val _news = MutableLiveData<List<NewsUI>>()
    private val _singleNews = MutableLiveData<NewsUI>()
    private val _errorMessage = SingleLiveEvent<String>()
    val news = _news as LiveData<List<NewsUI>>
    val singleNews = _singleNews as LiveData<NewsUI>
    val message = _errorMessage as LiveData<String>

    fun saveNews(newsUI: NewsUI) {
        viewModelScope.launch(Dispatchers.IO) {
            archiveRepository.saveArchive(newsUI)
        }
    }

    fun loadArchives() {
        _loading.value = true
        viewModelScope.launch(Dispatchers.IO) {
            val result = archiveRepository.getArchives()
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

    fun removeArchive(link: String) {
        viewModelScope.launch(Dispatchers.IO) {
            archiveRepository.removeArchive(link)
        }
    }

    fun loadArchive(link: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val result = archiveRepository.getArchive(link)
            withContext(Dispatchers.Main) {
                when (result.status) {
                    Status.SUCCESS -> {
                        _singleNews.value = result.data
                    }
                    Status.ERROR -> _errorMessage.value = result.message
                    else -> _errorMessage.value = result.message
                }
            }
        }
    }
}