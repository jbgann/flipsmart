package com.example.flipsmart

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.flipsmart.database.CardDatabaseDAO

class CreateViewModelFactory(
    private val dataSource: CardDatabaseDAO,
    private val application: Application,
    private val topic :String,
    private val createFragment : CreateFragment
) : ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CreateViewModel::class.java)) {
            return CreateViewModel(dataSource, application, topic, createFragment) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}