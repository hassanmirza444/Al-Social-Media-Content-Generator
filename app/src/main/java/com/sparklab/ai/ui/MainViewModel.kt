package com.sparklab.ai.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.JsonObject
import com.sparklab.ai.data.ApiState
import com.sparklab.ai.repository.MainRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject
constructor(private val mainRepository: MainRepository) : ViewModel() {

    private var _apiStateFlow: MutableStateFlow<ApiState> = MutableStateFlow(ApiState.Empty)
    val apiStateFlow: StateFlow<ApiState> = _apiStateFlow

    fun getData(obj : JsonObject) = viewModelScope.launch {
        mainRepository.getResponseData(obj)
            .onStart {
                _apiStateFlow.value = ApiState.Loading
            }.catch { e ->
                _apiStateFlow.value = ApiState.Failure(e)
            }.collect { response ->
                _apiStateFlow.value = ApiState.Success(response)
            }

    }


}