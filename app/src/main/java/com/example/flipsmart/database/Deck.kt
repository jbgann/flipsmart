package com.example.flipsmart.database

import androidx.room.ColumnInfo

data class Deck (
    @ColumnInfo(name = "deck")
    var deck: String = "default"
    )