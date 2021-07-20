package com.example.marvel_app.home

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.*
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import androidx.recyclerview.widget.GridLayoutManager
import com.example.marvel_app.ComicsAdapter
import com.example.marvel_app.R
import com.example.marvel_app.UIState
import com.example.marvel_app.databinding.FragmentHomeBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChangedBy
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.launch


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

        binding.searchViewLinearLayout.setOnClickListener {
            binding.searchEditText.requestFocus()
            inputMethodManager.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT, 0)
        }

        binding.searchEditText.setOnKeyListener(View.OnKeyListener { _, keyCode, event ->
            if (keyCode == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_UP) {
                viewModel.refreshComicsFromRepository(binding.searchEditText.editableText.toString())
                inputMethodManager.hideSoftInputFromWindow(view?.windowToken, 0)
                return@OnKeyListener true
            }
            false
        })

        binding.searchEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                binding.searchingEmptyTextView.visibility = View.GONE
                binding.searchViewCancel.visibility = View.VISIBLE
            }

            override fun afterTextChanged(s: Editable?) {}

        })

        binding.searchViewCancel.setOnClickListener {
            binding.searchEditText.text = null
            binding.searchingEmptyTextView.visibility = View.VISIBLE
            viewModel.clearComicList()
            inputMethodManager.hideSoftInputFromWindow(view?.windowToken, 0)
            binding.searchViewCancel.visibility = View.GONE
            binding.searchingErrorTextView.visibility = View.GONE
        }
    }

    private fun setupBottomNavigationStateObserver() {
        viewModel.inSearching.observe(viewLifecycleOwner, { searching ->
            if (searching) {
                viewModel.initFragmentForSearching()
                (activity as AppCompatActivity?)!!.supportActionBar!!.hide()
            } else {
                (activity as AppCompatActivity?)?.supportActionBar?.show()
                binding.searchViewConstraintLayout.visibility = View.GONE
                binding.searchEditText.text = null
                binding.searchViewCancel.visibility = View.GONE
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

        var searchJob: Job? = null

        binding.comicsListHome.apply {
            layoutManager = manager
            adapter = ComicsAdapter {
                viewModel.displayComicDetail(it)
            }
        }
        val adapter = binding.comicsListHome.adapter as ComicsAdapter


        searchJob?.cancel()
        searchJob = lifecycleScope.launch {
            viewModel.refreshComicsFromRepositoryFlow(null).collectLatest {
                adapter.submitData(it)
            }
        }

        lifecycleScope.launch {
            adapter.loadStateFlow
                // Only emit when REFRESH LoadState changes.
                .distinctUntilChangedBy { it.refresh }
                // Only react to cases where REFRESH completes i.e., NotLoading.
                .filter { it.refresh is LoadState.NotLoading }
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
                    binding.searchViewConstraintLayout.visibility = View.VISIBLE
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