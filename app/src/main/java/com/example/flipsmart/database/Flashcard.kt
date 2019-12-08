package com.example.flipsmart.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "flashcard_table")
data class Flashcard(
    @PrimaryKey(autoGenerate = true)
    var cardId : Long = 0L,
    @ColumnInfo(name = "front")
    var front : String = "",
    @ColumnInfo(name = "back")
    var back: String = "",
    @ColumnInfo(name = "deck")
    var deck: String = "default",
    @ColumnInfo(name = "frontType")
    var frontType: String = "Text",
    @ColumnInfo(name = "backType")
    var backType: String = "Text",
    @ColumnInfo(name = "rating")
    var rating: Int = 1
)