package com.shamalrathnayake.moviemate

import android.content.Context
import android.content.res.Resources
import android.os.Bundle
import android.view.KeyEvent
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.shamalrathnayake.moviemate.adapters.CategoryChipsAdapter
import com.shamalrathnayake.moviemate.adapters.TvShowAdapter
import com.shamalrathnayake.moviemate.databinding.FragmentTvShowsBinding
import com.shamalrathnayake.moviemate.viewModels.TvShowsViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TvShowsFragment : Fragment() {

    private var _binding: FragmentTvShowsBinding? = null
    private val binding get() = _binding!!
    private val viewModel: TvShowsViewModel by viewModels()
    private lateinit var categoryAdapter: CategoryChipsAdapter
    private lateinit var categoryChipsRecyclerView: RecyclerView
    private lateinit var tvShowAdapter: TvShowAdapter
    private lateinit var tvShowsRecyclerView: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentTvShowsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerViews(view)
        setupObservers()

        viewModel.loadTvShows("on_the_air")

        binding.search.setOnEditorActionListener { text, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH ||
                (event != null && event.keyCode == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_DOWN)
            ) {
                val query = text.text.toString().trim()
                if (query.isNotEmpty()) {
                    viewModel.searchTvShows(query)
                } else {
                    viewModel.loadTvShows("on_the_air", true)
                }

                val imm = context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(text.windowToken, 0)

                true
            } else {
                false
            }
        }
    }

    private fun setupRecyclerViews(view: View) {
        tvShowsRecyclerView = view.findViewById(R.id.tvShowRecyclerView)
        categoryChipsRecyclerView = view.findViewById(R.id.categoryChipsRecyclerView)

        tvShowAdapter = TvShowAdapter { tvShow ->
            val action = TvShowsFragmentDirections.actionTvShowsFragmentToTvShowDetailFragment(tvShow)
            findNavController().navigate(action)
        }

        tvShowsRecyclerView.apply {
            val displayMetrics = Resources.getSystem().displayMetrics
            val screenWidthPx = displayMetrics.widthPixels
            val itemWidthPx = (screenWidthPx * 0.50).toInt()
            val spanCount = (screenWidthPx / itemWidthPx).coerceAtLeast(2)
            layoutManager = GridLayoutManager(requireContext(), spanCount)
            adapter = tvShowAdapter
            addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)

                    val layoutManager = recyclerView.layoutManager as GridLayoutManager
                    val visibleItemCount = layoutManager.childCount
                    val totalItemCount = layoutManager.itemCount
                    val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()

                    if (!viewModel.isLoading.value!! &&
                        (visibleItemCount + firstVisibleItemPosition) >= totalItemCount &&
                        firstVisibleItemPosition >= 0
                    ) {
                        viewModel.loadTvShows(categoryAdapter.getCurrentCategory())
                    }
                }
            })

            val spacing = resources.getDimensionPixelSize(R.dimen.grid_spacing)
            tvShowsRecyclerView.addItemDecoration(GridSpacingItemDecoration(2, spacing, true))
        }

        val categories = tvShowAdapter.getCategories()
        categoryAdapter = CategoryChipsAdapter(categories) { category ->
            viewModel.loadTvShows(category, true)
        }
        categoryChipsRecyclerView.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            adapter = categoryAdapter
        }
    }

    private fun setupObservers() {
        viewModel.tvShows.observe(viewLifecycleOwner) { tvShows ->
            tvShowAdapter.submitList(tvShows)
        }

        viewModel.searchResults.observe(viewLifecycleOwner) { tvShows ->
            tvShowAdapter.submitList(tvShows)
        }

        viewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            if (isLoading) {
                binding.progressBar.visibility = View.VISIBLE
            } else {
                binding.progressBar.visibility = View.GONE
            }
        }

        viewModel.error.observe(viewLifecycleOwner) { error ->
            error?.let {
                Toast.makeText(context, it, Toast.LENGTH_LONG).show()
            }
        }
    }
} 