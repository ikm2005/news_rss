package am.andranik.inc.newsrss.presenter.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

open class BaseViewModel: ViewModel() {
    protected val _loading = MutableLiveData<Boolean>()
    val loading = _loading as LiveData<Boolean>
}