package com.example.flipsmart

import android.app.Application
import android.util.Log
import android.widget.ImageView
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import com.agog.mathdisplay.MTMathView
import com.example.flipsmart.database.CardDatabaseDAO
import com.example.flipsmart.database.Flashcard
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.create_fragment.*
import kotlinx.android.synthetic.main.review_fragment.*
import java.io.File

class ReviewViewModel(
    val database: CardDatabaseDAO,
    application: Application,
    val deck : String,
    val reviewFragment : ReviewFragment
) : AndroidViewModel(application) {

    lateinit var displayText : String
    var sideText = "Front"
    //TODO("put into a coroutine")
    val cards = database.getDeck(deck).value
    val deckSize = cards?.size ?: 0
    var currCardIndex = 0
    //Change these to LiveData
    var DrawViewVisible = MutableLiveData<Boolean>()
    var LatexViewVisible = MutableLiveData<Boolean>()
    var TextViewVisible = MutableLiveData<Boolean>()
    var side_is_front = MutableLiveData<Boolean>()
    val model = ReviewModel(database, deck, reviewFragment)
    lateinit var side_type : String
    var currCard : Flashcard? = null
    var cardDisplay = MutableLiveData<String>()

    init {

        DrawViewVisible.value = false
        LatexViewVisible.value = false
        TextViewVisible.value = false
        cardDisplay.value =  ""
        side_is_front.value = false
        val cardObserver = Observer<Flashcard>{card -> setNewCard(card)}
        model.currCard.observe(reviewFragment,cardObserver)
    }

    fun setNewCard(card : Flashcard) {
        Log.w("JOHNDEBUG","Card set")
        Log.w("JOHNDEBUG",card.cardId.toString())
        currCard = card
        flip()
    }
    fun flip(){
        side_is_front.value = !(side_is_front.value!!)
        set_side_type()
        set_view_type_visibility()
        set_side_text()
        set_card_display()
        set_side_type()
    }
    fun on_flip_rate_button(rating: Int){
        if (!side_is_front.value!!){
            rate(rating)
            model.getNextCard()
        }else {
            flip()
        }
    }

    fun rate(rating:Int){
        model.updateRating(currCard!!.cardId.toInt(),rating)
    }


    fun set_view_type_visibility(){
        DrawViewVisible.value = (side_type == "Draw")
        TextViewVisible.value = (side_type == "Text")
        LatexViewVisible.value = (side_type == "Latex")
    }

    fun set_side_text(){
        sideText = if (side_is_front.value!!) "Front" else "Back"

    }

    fun set_card_display(){
        cardDisplay.value = if(side_is_front.value!!) currCard?.front else currCard?.back
        if (DrawViewVisible.value!!){
            val imageFile = File(cardDisplay.value)
            Log.w("JOHNDEBUG",cardDisplay.value)
            if (imageFile.exists()){
                Log.w("JOHNDEBUG", "File exists")
                Picasso.get().load(imageFile).into(reviewFragment.card_image)
            }
        }
        if (LatexViewVisible.value!!){
            reviewFragment.mathview.latex=cardDisplay.value!!
        }
    }

    fun set_side_type(){
        side_type = if (side_is_front.value!!) currCard!!.frontType else currCard!!.backType
    }


}
