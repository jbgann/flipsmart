package com.example.flipsmart

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.example.flipsmart.databinding.MasterFragmentBinding


class MasterFragment : Fragment() {

    companion object {
        fun newInstance() = MasterFragment()
    }
    private lateinit var binding: MasterFragmentBinding

    private lateinit var viewModel: MasterViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = ViewModelProviders.of(this).get(MasterViewModel::class.java)
        val binding = DataBindingUtil.inflate<MasterFragmentBinding>(inflater,R.layout.master_fragment,container,false)
        binding.masterViewModel = viewModel
        binding.mathview1.latex="\\frac{1}{2}"
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        // TODO: Use the ViewModel
    }

}
