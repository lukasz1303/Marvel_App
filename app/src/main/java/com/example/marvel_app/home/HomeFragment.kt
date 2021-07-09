package com.example.marvel_app.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.marvel_app.ComicsAdapter
import com.example.marvel_app.R
import com.example.marvel_app.UIState
import com.example.marvel_app.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {

    private val viewModel: HomeViewModel by activityViewModels()

    private lateinit var binding: FragmentHomeBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(inflater)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        setupAdapter()
        initStateObserver()
        setupNavigationToDetailScreen()
        setupBottomNavigationStateObserver()
        setupSearchView()

        return binding.root
    }

    private fun setupSearchView() {
        binding.searchViewHome.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextChange(newText: String?): Boolean {
                binding.searchingEmptyTextView.visibility = View.GONE
                return false
            }

            override fun onQueryTextSubmit(query: String?): Boolean {
                viewModel.refreshComicsFromRepository(query)
                return true
            }
        })
    }

    private fun setupBottomNavigationStateObserver() {
        viewModel.inSearching.observe(viewLifecycleOwner, { searching ->
            (activity as AppCompatActivity?)!!.supportActionBar!!.hide()
            if (searching) {
                viewModel.initFragmentForSearching()
            } else {
                (activity as AppCompatActivity?)?.supportActionBar?.show()
                viewModel.refreshComicsFromRepository(null)
                binding.searchViewHome.visibility = View.GONE
            }
        })
    }

    private fun setupNavigationToDetailScreen() {
        viewModel.navigateToSelectedComic.observe(viewLifecycleOwner, {
            it?.let {
                this.findNavController()
                    .navigate(HomeFragmentDirections.actionHomeFragmentToDetailFragment(it))
                viewModel.displayComicDetailComplete()
            }
        })
    }

    private fun setupAdapter() {
        val manager = GridLayoutManager(activity, 1)
        binding.comicsListHome.apply {
            layoutManager = manager
            adapter = ComicsAdapter {
                viewModel.displayComicDetail(it)
            }
        }
    }

    private fun initStateObserver() {
        viewModel.state.observe(viewLifecycleOwner, {
            when (it) {
                is UIState.InProgress -> {
                    binding.homeProgressBar.visibility = View.VISIBLE
                    binding.homeErrorTextView.visibility = View.GONE
                    binding.searchingErrorTextView.visibility = View.GONE
                    binding.searchingEmptyTextView.visibility = View.GONE
                }
                is UIState.HomeError -> {
                    binding.homeErrorTextView.visibility = View.VISIBLE
                    binding.homeProgressBar.visibility = View.GONE
                }
                is UIState.InSearching -> {
                    binding.searchViewHome.visibility = View.VISIBLE
                    if (viewModel.searchingTitle.value == null) {
                        binding.searchingEmptyTextView.visibility = View.VISIBLE
                    }
                }
                is UIState.SearchingError -> {
                    binding.searchingErrorTextView.text =
                        getString(R.string.error_searching_message, viewModel.searchingTitle.value)
                    binding.searchingErrorTextView.visibility = View.VISIBLE
                    binding.homeProgressBar.visibility = View.GONE
                }
                else -> {
                    binding.homeProgressBar.visibility = View.GONE
                    binding.homeErrorTextView.visibility = View.GONE
                }
            }
        })
    }
}