package com.example.flipsmart

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.flipsmart.database.CardDatabaseDAO

class ReviewViewModelFactory(
    private val dataSource: CardDatabaseDAO,
    private val application: Application,
    private val topic :String,
    private val reviewFragment : ReviewFragment
) : ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ReviewViewModel::class.java)) {
            return ReviewViewModel(dataSource, application, topic, reviewFragment) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}