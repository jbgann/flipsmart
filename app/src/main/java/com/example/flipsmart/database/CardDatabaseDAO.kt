package com.example.flipsmart.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface CardDatabaseDAO {

    @Insert
    fun insert(card: Flashcard)

    @Update
    fun update(card: Flashcard)

    @Query("SELECT * from flashcard_table WHERE cardId= :key")
    fun get(key: Long): Flashcard?

    @Query("DELETE FROM flashcard_table")
    fun clear()

    @Query("SELECT * FROM flashcard_table WHERE deck= :deck")
    fun getDeck(deck: String): LiveData<List<Flashcard>>

    @Query("SELECT COUNT(*) FROM flashcard_table WHERE deck= :deck")
    fun countCards(deck: String): Int

    @Query("SELECT DISTINCT deck FROM flashcard_table ORDER BY deck ASC")
    fun getDeckNames() : LiveData<List<Deck>>

    @Query("UPDATE flashcard_table SET rating = :rating WHERE cardId= :id ")
    fun updateRating(rating:Int, id : Int)

}
