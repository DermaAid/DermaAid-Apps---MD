package com.example.dermaaid.ui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.dermaaid.adapter.NewsAdapter
import com.example.dermaaid.databinding.FragmentNewsBinding
import com.example.dermaaid.viewModel.NewsViewModel

class NewsFragment : Fragment() {

    private var _binding: FragmentNewsBinding? = null
    private val binding get() = _binding!!
    private lateinit var newsAdapter: NewsAdapter
    private lateinit var newsViewModel: NewsViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNewsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d("NewsFragment", "Fetching news...")

        try {
            initRecyclerView()

            newsViewModel = ViewModelProvider(this)[NewsViewModel::class.java]
            newsViewModel.fetchHealthNews()
            newsViewModel.newsList.observe(viewLifecycleOwner, Observer { newsList ->
                newsAdapter.submitList(newsList)
            })
        } catch (e: Exception) {
            Log.e("NewsFragment", "Error in fetching news", e)
        }
    }

    private fun initRecyclerView() {
        newsAdapter = NewsAdapter { url ->
            openNewsUrl(url)
        }
        binding.rvNewsList.apply {
            adapter = newsAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
    }

    private fun openNewsUrl(url: String) {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        startActivity(intent)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
