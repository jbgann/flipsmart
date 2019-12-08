package com.example.flipsmart

import android.view.View
import androidx.lifecycle.ViewModel
import androidx.navigation.findNavController

class NameViewModel : ViewModel() {
    fun create_cards(view : View){
        view.findNavController().navigate(R.id.action_nameFragment_to_createFragment)
    }
}
