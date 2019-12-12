package com.example.flipsmart

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.example.flipsmart.databinding.NameFragmentBinding

class NameFragment : Fragment() {

    companion object {
        fun newInstance() = NameFragment()
    }

    private lateinit var binding: NameFragmentBinding

    private lateinit var sharedViewModel : SharedViewModel

    private lateinit var viewModel: NameViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = ViewModelProviders.of(this).get(NameViewModel::class.java)
        sharedViewModel = activity?.run {
            ViewModelProviders.of(this).get(SharedViewModel::class.java)
        } ?: throw Exception("Invalid Activity")
        binding = DataBindingUtil.inflate<NameFragmentBinding>(inflater,R.layout.name_fragment,container,false)
        binding.nameViewModel = viewModel
        binding.sharedViewModel = sharedViewModel
        binding.setLifecycleOwner(this)
        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onResume(){
        super.onResume()
        sharedViewModel.currDeckName.value =""
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        // TODO: Use the ViewModel
    }

}
