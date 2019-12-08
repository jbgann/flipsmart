package com.example.flipsmart

import android.view.View
import androidx.lifecycle.ViewModel
import androidx.navigation.findNavController

class MasterViewModel : ViewModel() {
    fun create_cards(view : View){
        view.findNavController().navigate(R.id.action_masterFragment_to_nameFragment)
    }

    fun review_cards(view : View){
        view.findNavController().navigate(R.id.action_masterFragment_to_selectFragment)
    }
}
