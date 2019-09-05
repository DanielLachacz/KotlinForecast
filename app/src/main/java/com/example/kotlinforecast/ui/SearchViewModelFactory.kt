package com.example.kotlinforecast.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.kotlinforecast.data.repository.SearchRepository

class SearchViewModelFactory(private val cityRepository: SearchRepository)
    : ViewModelProvider.NewInstanceFactory(){

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return SearchViewModel(cityRepository) as T
    }
}