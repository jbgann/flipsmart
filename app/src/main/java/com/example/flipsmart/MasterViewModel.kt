package com.example.flipsmart

import android.view.View
import androidx.lifecycle.ViewModel
import androidx.navigation.findNavController

class MasterViewModel : ViewModel() {

    lateinit var sharedViewModel : SharedViewModel

    fun create_cards(view : View){
        view.findNavController().navigate(R.id.action_masterFragment_to_nameFragment)
    }

    fun review_cards(view : View){
        sharedViewModel.mode="Review"
        view.findNavController().navigate(R.id.action_masterFragment_to_selectFragment)
    }

    fun export_cards(view : View){
        sharedViewModel.mode="Export"
        view.findNavController().navigate(R.id.action_masterFragment_to_selectFragment)
    }
}
