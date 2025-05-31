package com.shamalrathnayake.moviemate

import android.content.res.Resources
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.shamalrathnayake.moviemate.adapters.CategoryChipsAdapter
import com.shamalrathnayake.moviemate.adapters.MovieAdapter
import com.shamalrathnayake.moviemate.databinding.FragmentMoviesBinding
import com.shamalrathnayake.moviemate.viewModels.MoviesViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MoviesFragment : Fragment() {

    private var _binding: FragmentMoviesBinding? = null
    private val binding get() = _binding!!
    private val viewModel: MoviesViewModel by viewModels()
    private lateinit var categoryAdapter: CategoryChipsAdapter
    private lateinit var categoryChipsRecyclerView: RecyclerView
    private lateinit var movieAdapter: MovieAdapter
    private lateinit var moviesRecyclerView: RecyclerView



    private val categories = listOf(
        "Now Playing" to "now_playing",
        "Popular" to "popular",
        "Top Rated" to "top_rated",
        "Upcoming" to "upcoming",
        "Action" to "28",
        "Adventure" to "12",
        "Animation" to "16",
        "Comedy" to "35",
        "Crime" to "80",
        "Documentary" to "99",
        "Drama" to "18",
        "Family" to "10751",
        "Fantasy" to "14",
        "History" to "36",
        "Horror" to "27",
        "Music" to "10402",
        "Mystery" to "9648",
        "Romance" to "10749",
        "Science Fiction" to "878",
        "TV Movie" to "10770",
        "Thriller" to "53",
        "War" to "10752",
        "Western" to "37"
    )




    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMoviesBinding.inflate(inflater, container, false)
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

        viewModel.loadMovies("now_playing")
    }

    private fun setupRecyclerViews(view: View) {
        moviesRecyclerView = view.findViewById(R.id.movieRecyclerView)
        categoryChipsRecyclerView = view.findViewById(R.id.categoryChipsRecyclerView)

        movieAdapter = MovieAdapter()
        moviesRecyclerView.apply {
            val displayMetrics = Resources.getSystem().displayMetrics
            val screenWidthPx = displayMetrics.widthPixels
            val itemWidthPx = (screenWidthPx * 0.50).toInt()
            val spanCount = (screenWidthPx / itemWidthPx).coerceAtLeast(2)
            layoutManager = GridLayoutManager(requireContext(), spanCount)
            adapter = movieAdapter
            addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)


                    val layoutManager = recyclerView.layoutManager as GridLayoutManager
                    val visibleItemCount = layoutManager.childCount
                    val totalItemCount = layoutManager.itemCount
                    val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()

                    if (!viewModel.isLoading.value!! &&
                        (visibleItemCount + firstVisibleItemPosition) >= totalItemCount &&
                        firstVisibleItemPosition >= 0) {

                        viewModel.loadMovies(categoryAdapter.getCurrentCategory())
                    }
                }
            })

            val spacing = resources.getDimensionPixelSize(R.dimen.grid_spacing) // Define in dimens.xml
            moviesRecyclerView.addItemDecoration(GridSpacingItemDecoration(2, spacing, true))
        }

        categoryAdapter = CategoryChipsAdapter(categories) { category ->
            viewModel.loadMovies(category, true)
        }
        categoryChipsRecyclerView.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            adapter = categoryAdapter
        }
    }
    private fun setupObservers() {
        viewModel.movies.observe(viewLifecycleOwner) { movies ->
            movieAdapter.submitList(movies)
        }

        viewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            // Handle loading state if needed
        }

        viewModel.error.observe(viewLifecycleOwner) { error ->
            error?.let {
                Toast.makeText(context, it, Toast.LENGTH_LONG).show()
            }
        }
    }

}