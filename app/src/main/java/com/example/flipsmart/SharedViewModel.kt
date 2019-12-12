package com.example.flipsmart

import android.util.Log
import android.view.View
import android.widget.EditText
import androidx.databinding.BaseObservable
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.navigation.findNavController
import com.example.flipsmart.database.CardDatabaseDAO
import com.example.flipsmart.database.Flashcard

class SharedViewModel : ViewModel() {
    val currDeckName = MutableLiveData<String>()
    lateinit var selectFragment : SelectFragment
    lateinit var referenceView : View
    lateinit var database : CardDatabaseDAO
    var mode = "Review"

    init {
        currDeckName.value=""
    }
    fun onDeckClicked(deckName : String){
        Log.e("JOHNDEBUG","onDeckClick entered")
        if (mode == "Review") {
            currDeckName.value = deckName
            referenceView.findNavController().navigate(R.id.action_selectFragment_to_reviewFragment)
        }else if(mode == "Export"){
            val mainActivity = selectFragment.activity as MainActivity
            mainActivity.cardsToWrite = database.getDeck(deckName)
            val deckObserver = Observer<List<Flashcard>>({ deck -> mainActivity.requestSignIn(mainActivity)})
            mainActivity.cardsToWrite.observe(mainActivity,deckObserver)
            referenceView.findNavController().navigate(R.id.action_selectFragment_to_masterFragment)
        }
    }
}