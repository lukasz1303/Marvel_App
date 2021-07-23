package com.example.marvel_app.detail

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import com.example.marvel_app.databinding.FragmentDetailBinding
import com.google.android.material.bottomsheet.BottomSheetBehavior
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailFragment : Fragment() {

    val viewModel: DetailViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentDetailBinding.inflate(inflater)
        binding.lifecycleOwner = this
        val comic = DetailFragmentArgs.fromBundle(requireArguments()).selectedComic
        viewModel.setComic(comic)
        binding.viewModel = viewModel

        BottomSheetBehavior.from(binding.bottomSheet).state = BottomSheetBehavior.STATE_COLLAPSED

        setupFindOutMoreButtonListener(binding)

        return binding.root
    }

    private fun setupFindOutMoreButtonListener(binding: FragmentDetailBinding) {
        binding.findOutMoreButton.setOnClickListener {
            val webIntent = Intent(
                Intent.ACTION_VIEW,
                Uri.parse(viewModel.selectedComic.value?.detailUrl)
            )
            requireActivity().startActivity(webIntent)
        }
    }

}