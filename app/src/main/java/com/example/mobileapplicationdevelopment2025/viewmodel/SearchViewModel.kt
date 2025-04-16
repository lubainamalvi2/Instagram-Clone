package com.example.mobileapplicationdevelopment2025.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mobileapplicationdevelopment2025.model.SearchProfileResult
import com.example.mobileapplicationdevelopment2025.network.ApiService
import com.example.mobileapplicationdevelopment2025.network.RetrofitInstance
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class SearchViewModel(private val api: ApiService = RetrofitInstance.api) : ViewModel() {

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _searchResults = MutableStateFlow<List<SearchProfileResult>>(emptyList())
    val searchResults: StateFlow<List<SearchProfileResult>> = _searchResults.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    fun onSearchQueryChanged(newQuery: String) {
        _searchQuery.value = newQuery.trim()
        if (_searchQuery.value.isEmpty()) {
            _searchResults.value = emptyList()
        }
    }

    fun executeSearch() {
        val query = _searchQuery.value
        if (query.isNotBlank()) {
            viewModelScope.launch {
                _isLoading.value = true
                _errorMessage.value = null
                try {
                    val response = api.searchUsers(query)
                    if (response.isSuccessful) {
                        _searchResults.value = response.body() ?: emptyList()
                    } else {
                        _errorMessage.value = "Error: ${response.code()}"
                        _searchResults.value = emptyList()
                    }
                } catch (e: Exception) {
                    _errorMessage.value = "Error occurred: ${e.message}"
                    _searchResults.value = emptyList()
                }
                _isLoading.value = false
            }
        }
    }
}