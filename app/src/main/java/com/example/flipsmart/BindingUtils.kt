package com.example.flipsmart

import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.example.flipsmart.database.Deck

@BindingAdapter("deckName")
fun TextView.setDeckName(deck: Deck) {
    text = deck.deck
}