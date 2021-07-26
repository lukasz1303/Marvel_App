package com.example.marvel_app.favourites

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.marvel_app.ComicsAdapter
import com.example.marvel_app.R
import com.example.marvel_app.SimpleComicAdapter
import com.example.marvel_app.databinding.FragmentFavouritesBinding
import com.example.marvel_app.databinding.FragmentHomeBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FavouritesFragment : Fragment() {

    private lateinit var binding: FragmentFavouritesBinding
    private val viewModel: FavouritesViewModel by viewModels()
    private lateinit var adapter: SimpleComicAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentFavouritesBinding.inflate(inflater)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupAdapter()
    }

    private fun setupAdapter() {
        val manager = GridLayoutManager(activity, 1)
        binding.comicsListFavourites.layoutManager = manager

        binding.comicsListFavourites.apply {
            layoutManager = manager
            adapter = SimpleComicAdapter {
                viewModel.displayComicDetail(it)
            }
        }
        adapter = binding.comicsListFavourites.adapter as SimpleComicAdapter
        viewModel.comics.observe(viewLifecycleOwner, {
            adapter.submitList(it)
        })

    }

}