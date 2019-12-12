package com.example.flipsmart

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.example.flipsmart.database.CardDatabase
import com.example.flipsmart.databinding.CreateFragmentBinding
import kotlinx.android.synthetic.main.create_fragment.*
import android.util.DisplayMetrics




class CreateFragment : Fragment() {

    companion object {
        fun newInstance() = CreateFragment()
    }

    private lateinit var viewModel: CreateViewModel

    private lateinit var sharedViewModel : SharedViewModel

    lateinit var binding: CreateFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate<CreateFragmentBinding>(inflater,R.layout.create_fragment,container,false)
        val application = requireNotNull(this.activity).application
        val dataSource = CardDatabase.getInstance(application).cardDatabaseDao
        sharedViewModel = activity?.run {
            ViewModelProviders.of(this).get(SharedViewModel::class.java)
        } ?: throw Exception("Invalid Activity")
        val viewModelFactory = CreateViewModelFactory(dataSource,application,sharedViewModel.currDeckName.value ?: "default", this)
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(CreateViewModel::class.java)
        binding.createViewModel = viewModel
        binding.sharedViewModel = sharedViewModel
        binding.setLifecycleOwner(this)
        Log.e("JOHNDEBUG",sharedViewModel.currDeckName.value ?: "default")
        val metrics = DisplayMetrics()
        getActivity()!!.getWindowManager().getDefaultDisplay().getMetrics(metrics)
        viewModel.canvasMetrics = metrics

        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(CreateViewModel::class.java)
        // TODO: Use the ViewModel
    }

}
