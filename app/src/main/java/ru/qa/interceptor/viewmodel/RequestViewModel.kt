package ru.qa.interceptor.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ru.qa.interceptor.TestApplication
import ru.qa.interceptor.db.RequestResponse
import ru.qa.interceptor.db.RequestResponseRepo
import javax.inject.Inject

class RequestViewModel : ViewModel() {

    @Inject
    lateinit var requestRepo: RequestResponseRepo

    private val _allReq = MutableStateFlow(emptyList<RequestResponse>())
    val allReq: StateFlow<List<RequestResponse>> = _allReq.asStateFlow()

    private val _selected = MutableStateFlow(RequestResponse())
    val selected: StateFlow<RequestResponse> = _selected.asStateFlow()

    private val _resultFTS = MutableStateFlow(emptyList<RequestResponse>())
    val resultFTS: StateFlow<List<RequestResponse>> = _resultFTS.asStateFlow()

    init {
        TestApplication.component.injectToViewModel(this)

        viewModelScope.launch(Dispatchers.IO) {
            requestRepo.getAllRequest()
                .onEach {
                    _allReq.value = it
                }.launchIn(this)
        }
    }

    fun insert(item: RequestResponse) = viewModelScope.launch(Dispatchers.IO) {
        requestRepo.insertReq(item)
    }

    fun delete(item: RequestResponse) = viewModelScope.launch(Dispatchers.IO) {
        requestRepo.deleteReq(item)
    }

    fun deleteAll() = viewModelScope.launch(Dispatchers.IO) {
        requestRepo.deleteAll()
    }

    fun update(item: RequestResponse) = viewModelScope.launch(Dispatchers.IO) {
        requestRepo.updateReq(item)
    }

    fun searchBody(body: String) = viewModelScope.launch(Dispatchers.IO) {
        requestRepo.searchBody(body).onEach {
            withContext(Dispatchers.Main) {
                _resultFTS.value = it
            }
        }.launchIn(this)
    }

    fun select(item: RequestResponse) {
        _selected.value = item
    }
}