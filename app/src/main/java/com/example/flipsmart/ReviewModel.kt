package com.example.flipsmart

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.flipsmart.database.CardDatabaseDAO
import com.example.flipsmart.database.Flashcard
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.util.*
import androidx.lifecycle.Observer

class ReviewModel(val database : CardDatabaseDAO, val deckName : String, val reviewFragment : ReviewFragment) {

    private var DBIOJob = Job()
    private val IOScope = CoroutineScope(Dispatchers.IO + DBIOJob)

    val cards = database.getDeck(deckName)
    val randomizer = Random()
    val currCard = MutableLiveData<Flashcard>()
    val deckObserver = Observer<List<Flashcard>>({deck -> loadFirstCard(deck)})

    init{
        cards.observe(reviewFragment,deckObserver)
    }

    fun loadFirstCard(deck : List<Flashcard>){
        if (deck != null) {
            cards.removeObserver(deckObserver)
            getNextCard()
        }

    }
    //This function ensures that cards with lower ratings will be shown more often
    fun getNextCard(){
        val difficulty_threshold = randomizer.nextInt(6)
        val candidateCard = cards!!.value!!.get(randomizer.nextInt(cards.value!!.size))
        if (candidateCard.rating >= difficulty_threshold){
            currCard.value = candidateCard
        }else{
            getNextCard()
        }
    }


    fun updateRating(cardId : Int, rating: Int){
        IOScope.launch{
            updateRating_in_DB(cardId,rating)
        }
    }

    suspend fun updateRating_in_DB(cardId : Int, rating: Int){
        database.updateRating(rating,cardId)
    }


}