package com.example.marvel_app.home

import android.content.Context
import android.os.Bundle
import android.view.*
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.activityViewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.marvel_app.ComicsAdapter
import com.example.marvel_app.R
import com.example.marvel_app.UIState
import com.example.marvel_app.databinding.FragmentHomeBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private val viewModel: HomeViewModel by activityViewModels()
    private lateinit var navController: NavController
    private lateinit var binding: FragmentHomeBinding
    private lateinit var inputMethodManager: InputMethodManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(inflater)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel
        navController = this.findNavController()
        inputMethodManager =
            context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        setHasOptionsMenu(true)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupAdapter()
        initStateObserver()
        setupNavigationToDetailScreen()
        setupBottomNavigationStateObserver()
        setupSearchView()

        binding.searchViewHomeLinearLayout.setOnClickListener {
            binding.searchViewHome.requestFocus()
            inputMethodManager.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT, 0)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.home_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.settings_menu) {
            navController.navigate(HomeFragmentDirections.actionHomeFragmentToSettingsFragment())
        }
        return super.onOptionsItemSelected(item)
    }


    private fun setupSearchView() {
        binding.searchViewHome.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextChange(newText: String?): Boolean {
                binding.searchingEmptyTextView.visibility = View.GONE
                return false
            }

            override fun onQueryTextSubmit(query: String?): Boolean {
                viewModel.refreshComicsFromRepository(query)
                inputMethodManager.hideSoftInputFromWindow(view?.windowToken, 0)
                return true
            }
        })
    }

    private fun setupBottomNavigationStateObserver() {
        viewModel.inSearching.observe(viewLifecycleOwner, { searching ->
            if (searching) {
                viewModel.initFragmentForSearching()
                (activity as AppCompatActivity?)!!.supportActionBar!!.hide()
            } else {
                (activity as AppCompatActivity?)?.supportActionBar?.show()
                viewModel.refreshComicsFromRepository(null)
                binding.searchViewHome.visibility = View.GONE
                binding.searchViewHome.setQuery("", false)
            }
        })
    }

    private fun setupNavigationToDetailScreen() {
        viewModel.navigateToSelectedComic.observe(viewLifecycleOwner, {
            it?.let {
                navController
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
                is UIState.Error -> {
                    binding.homeErrorTextView.visibility = View.VISIBLE
                    binding.homeProgressBar.visibility = View.GONE
                }
                is UIState.InSearching -> {
                    binding.searchViewHome.visibility = View.VISIBLE
                    binding.homeErrorTextView.visibility = View.GONE
                    binding.homeProgressBar.visibility = View.GONE

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