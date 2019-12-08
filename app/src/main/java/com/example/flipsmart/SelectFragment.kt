package com.example.flipsmart

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.example.flipsmart.database.CardDatabase
import com.example.flipsmart.databinding.CreateFragmentBinding
import com.example.flipsmart.databinding.NameFragmentBinding
import com.example.flipsmart.databinding.SelectFragmentBinding


class SelectFragment : Fragment() {

    companion object {
        fun newInstance() = SelectFragment()
    }

    private lateinit var binding: SelectFragmentBinding

    private lateinit var sharedViewModel : SharedViewModel

    private lateinit var viewModel: SelectViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        sharedViewModel = activity?.run {
            ViewModelProviders.of(this).get(SharedViewModel::class.java)
        } ?: throw Exception("Invalid Activity")
        val adapter = DeckAdapter(DeckListener{deckName -> sharedViewModel.onDeckClicked(deckName)})
        val application = requireNotNull(this.activity).application
        val dataSource = CardDatabase.getInstance(application).cardDatabaseDao
        val viewModelFactory = SelectViewModelFactory(dataSource,application)
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(SelectViewModel::class.java)
        viewModel.decks.observe(viewLifecycleOwner, Observer {it?.let {adapter.submitList(it)}})
        binding = DataBindingUtil.inflate<SelectFragmentBinding>(inflater,R.layout.select_fragment,container,false)
        sharedViewModel.referenceView = binding.textView6


        binding.selectViewModel = viewModel
        binding.sharedViewModel = sharedViewModel
        binding.deckList.adapter = adapter
        binding.setLifecycleOwner(this)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        // TODO: Use the ViewModel
    }


}
