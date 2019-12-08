package com.example.flipsmart.database


import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Flashcard::class], version = 3, exportSchema = false)
abstract class CardDatabase : RoomDatabase() {
    abstract val cardDatabaseDao: CardDatabaseDAO

    companion object {
        @Volatile
        private var INSTANCE: CardDatabase? = null
        fun getInstance(context: Context): CardDatabase {
            synchronized(this) {
                var instance = INSTANCE
                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        CardDatabase::class.java,
                        "flashcard_database")
                        .fallbackToDestructiveMigration()
                        .build()
                    INSTANCE = instance
                }
                return instance
            }
        }
    }

}