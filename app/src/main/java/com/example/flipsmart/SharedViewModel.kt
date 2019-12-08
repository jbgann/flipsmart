package com.example.flipsmart

import android.view.View
import android.widget.EditText
import androidx.databinding.BaseObservable
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.navigation.findNavController

class SharedViewModel : ViewModel() {
    val currDeckName = MutableLiveData<String>()
    lateinit var referenceView : View

    init {
        currDeckName.value=""
    }
    fun onDeckClicked(deckName : String){
        currDeckName.value=deckName
        referenceView.findNavController().navigate(R.id.action_selectFragment_to_reviewFragment)
    }
}