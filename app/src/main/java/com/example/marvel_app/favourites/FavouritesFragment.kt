package com.example.marvel_app.favourites

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.marvel_app.SimpleComicAdapter
import com.example.marvel_app.UIState
import com.example.marvel_app.databinding.FragmentFavouritesBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FavouritesFragment : Fragment() {

    private lateinit var binding: FragmentFavouritesBinding
    private val viewModel: FavouritesViewModel by viewModels()
    private lateinit var adapter: SimpleComicAdapter
    private lateinit var navController: NavController

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentFavouritesBinding.inflate(inflater)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel
        navController = this.findNavController()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupAdapter()
        setupNavigationToDetailScreen()
        initStateObserver()
    }

    override fun onResume() {
        super.onResume()
        viewModel.refreshFavouritesComicsFromRepository()
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
        adapter.stateRestorationPolicy =
            RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY
        viewModel.comics.observe(viewLifecycleOwner, {
            adapter.submitList(it)
        })
    }

    private fun setupNavigationToDetailScreen() {
        viewModel.navigateToSelectedComic.observe(viewLifecycleOwner, {
            it?.let {
                navController
                    .navigate(
                        FavouritesFragmentDirections.actionFavouritesFragmentToDetailFragment(
                            it
                        )
                    )
                viewModel.displayComicDetailComplete()
            }
        })
    }


    private fun initStateObserver() {
        viewModel.state.observe(viewLifecycleOwner, {
            when (it) {
                is UIState.InProgress -> {
                    binding.favouritesProgressBar.visibility = View.VISIBLE
                    binding.favouritesErrorTextView.visibility = View.GONE
                }
                is UIState.Error -> {
                    binding.favouritesErrorTextView.visibility = View.VISIBLE
                    binding.favouritesProgressBar.visibility = View.GONE
                }
                else -> {
                    binding.favouritesProgressBar.visibility = View.GONE
                    binding.favouritesErrorTextView.visibility = View.GONE
                }
            }
        })
    }
}