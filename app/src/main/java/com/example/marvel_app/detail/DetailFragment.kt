package com.example.marvel_app.detail

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.example.marvel_app.R
import com.example.marvel_app.databinding.FragmentDetailBinding
import com.google.android.material.bottomsheet.BottomSheetBehavior
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailFragment : Fragment() {

    val viewModel: DetailViewModel by viewModels()
    private lateinit var binding: FragmentDetailBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDetailBinding.inflate(inflater)
        binding.lifecycleOwner = this
        val comic = DetailFragmentArgs.fromBundle(requireArguments()).selectedComic
        viewModel.setComic(comic)
        binding.viewModel = viewModel

        BottomSheetBehavior.from(binding.bottomSheet).state = BottomSheetBehavior.STATE_COLLAPSED

        setupComicInFavouritesObserver()
        setupFindOutMoreButtonListener(binding)

        return binding.root
    }

    private fun setupComicInFavouritesObserver() {
        viewModel.checkIfComicInFavourites()
        viewModel.inFavourites.observe(viewLifecycleOwner, {
            binding.favouritesStarIcon.setImageResource(if (it) R.drawable.ic_baseline_star_36 else R.drawable.ic_baseline_star_border_36)
        })
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