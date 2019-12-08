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
import com.example.flipsmart.databinding.ReviewFragmentBinding
import kotlinx.android.synthetic.main.review_fragment.*


class ReviewFragment : Fragment() {

    companion object {
        fun newInstance() = ReviewFragment()
    }

    private lateinit var viewModel: ReviewViewModel

    private lateinit var sharedViewModel : SharedViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = DataBindingUtil.inflate<ReviewFragmentBinding>(inflater,R.layout.review_fragment,container,false)
        val application = requireNotNull(this.activity).application
        val dataSource = CardDatabase.getInstance(application).cardDatabaseDao
        sharedViewModel = activity?.run {
            ViewModelProviders.of(this).get(SharedViewModel::class.java)
        } ?: throw Exception("Invalid Activity")
        val viewModelFactory = ReviewViewModelFactory(dataSource,application,sharedViewModel.currDeckName.value ?: "default",this)
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(ReviewViewModel::class.java)
        binding.reviewViewModel = viewModel
        binding.sharedViewModel = sharedViewModel
        binding.setLifecycleOwner(this)
        //There does not seem to be an XML attribute to set the LaTeX for MTMathView
        //so databinding can't be used.  Passing the mathview into the viewmodel
        //seems to be the next best thing.
        Log.e("JOHNDEBUG",sharedViewModel.currDeckName.value ?: "default")

        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(ReviewViewModel::class.java)
        // TODO: Use the ViewModel
    }

}
