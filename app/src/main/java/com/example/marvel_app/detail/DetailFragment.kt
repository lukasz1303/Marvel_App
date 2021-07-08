package com.example.marvel_app.detail

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.example.marvel_app.R
import com.example.marvel_app.databinding.FragmentDetailBinding
import com.google.android.material.bottomsheet.BottomSheetBehavior

class DetailFragment : Fragment() {

    val viewModel: DetailViewModel by lazy {
        val comic = DetailFragmentArgs.fromBundle(requireArguments()).selectedComic
        val viewModelFactory = DetailViewModelFactory(comic)
        ViewModelProvider(this, viewModelFactory).get(DetailViewModel::class.java)
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentDetailBinding.inflate(inflater)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        BottomSheetBehavior.from(binding.bottomSheet).state=BottomSheetBehavior.STATE_COLLAPSED

        return binding.root
    }

}