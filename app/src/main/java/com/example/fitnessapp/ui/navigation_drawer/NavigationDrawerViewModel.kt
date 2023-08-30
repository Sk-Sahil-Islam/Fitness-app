package com.example.fitnessapp.ui.navigation_drawer

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fitnessapp.data.remote.responses.Quotes
import com.example.fitnessapp.data.remote.responses.QuotesItem
import com.example.fitnessapp.repository.FitnessRepository
import com.example.fitnessapp.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NavigationDrawerViewModel @Inject constructor(
    private val repository: FitnessRepository
) : ViewModel() {

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()
    var quote = mutableStateOf<QuotesItem>(QuotesItem("","","","error"))

    init {
        loadDailyQuote()
    }

    fun loadDailyQuote() {
        viewModelScope.launch {
            _isLoading.value = true
            val result = repository.getDailyQuote()
            when (result) {
                is Resource.Error -> {
                    result.message?.let { Log.e("error", it) }
                    _isLoading.value = false
                }
                is Resource.Success -> {
                    quote.value = result.data!![0]
                    _isLoading.value = false
                }
                else -> {}
            }
        }
    }
}