package com.example.marvel_app.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.marvel_app.ComicsAdapter
import com.example.marvel_app.OnClickListener
import com.example.marvel_app.UIState
import com.example.marvel_app.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {

    private val viewModel: HomeViewModel by lazy {
        ViewModelProvider(this).get(HomeViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val binding = FragmentHomeBinding.inflate(inflater)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        val manager = GridLayoutManager(activity, 1)
        binding.comicsListHome.apply {
            layoutManager = manager
            adapter = ComicsAdapter(OnClickListener {
                viewModel.displayComicDetail(it)
            })
        }

        viewModel.state.observe(viewLifecycleOwner, {
            when (it) {
                is UIState.InProgress -> {
                    binding.homeProgressBar.visibility = View.VISIBLE
                    binding.homeErrorTextView.visibility = View.GONE
                }
                is UIState.Error -> {
                    binding.homeErrorTextView.visibility = View.VISIBLE
                    binding.homeProgressBar.visibility = View.GONE
                }
                else -> {
                    binding.homeProgressBar.visibility = View.GONE
                    binding.homeErrorTextView.visibility = View.GONE
                }
            }
        })

        viewModel.navigateToSelectedComic.observe(viewLifecycleOwner, Observer {
            if(null != it){
                this.findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToDetailFragment(it))
                viewModel.displayComicDetailComplete()
            }
        })

        return binding.root
    }
}