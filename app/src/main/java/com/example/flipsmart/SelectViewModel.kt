package com.example.flipsmart

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import com.example.flipsmart.database.CardDatabaseDAO

class SelectViewModel(
    val database: CardDatabaseDAO,
    application: Application
) : AndroidViewModel(application) {
    val decks = database.getDeckNames()
}
