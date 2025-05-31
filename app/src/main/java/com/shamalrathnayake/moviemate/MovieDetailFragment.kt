package com.shamalrathnayake.moviemate

import android.content.res.Resources
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.graphics.drawable.LayerDrawable
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.graphics.drawable.toDrawable
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.google.android.material.chip.Chip
import com.shamalrathnayake.moviemate.adapters.CategoryChipsAdapter
import com.shamalrathnayake.moviemate.adapters.MovieAdapter
import com.shamalrathnayake.moviemate.api.models.Movie
import com.shamalrathnayake.moviemate.databinding.FragmentMovieDetailBinding
import jp.wasabeef.glide.transformations.BlurTransformation


class MovieDetailFragment : Fragment() {

    private var _binding: FragmentMovieDetailBinding? = null
    private val binding get() = _binding!!

    private var movieAdapter: MovieAdapter = MovieAdapter { movie: Movie -> }


    private val args: MovieDetailFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentMovieDetailBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        System.out.println("---------------======================----------")
        System.out.println(args)

        val movie = MovieDetailFragmentArgs.fromBundle(requireArguments()).movie

        System.out.println("---------------======================----------")
        System.out.println(!movie.backdropPath.isNullOrEmpty())


        if (!movie.backdropPath.isNullOrEmpty()) {
            Glide.with(binding.backdropImage)
                .load("${BuildConfig.TMDB_IMAGE_BASE_URL}w500${movie.backdropPath}")
                .transform(BlurTransformation(25, 3))
                .into(binding.backdropImage)

        }

        binding.apply {

            movieTitle.text = movie.title
            movieYear.text = movie.releaseDate.toString().split("-")[0]
            movieRating.text = String.format("⭐ %.1f", movie.voteAverage)
            movieLanguage.text = movie.language.toString().uppercase()


            val genreNames = movie.genreIds.mapNotNull { movieAdapter.getGenreMap()[it] }
            binding.genreChipGroup.removeAllViews()

            for (genre in genreNames) {
                val chip = Chip(context).apply {
                    text = genre
                    isClickable = false
                    isCheckable = false
                }
                binding.genreChipGroup.addView(chip)
            }

            movieOverview.text = movie.overview


            System.out.println("---------------+++++++++++++----------")
            System.out.println(movie)

            Glide.with(moviePoster)
                .load("${BuildConfig.TMDB_IMAGE_BASE_URL}w500${movie.posterPath}")
                .into(moviePoster)



        }

        binding.backButton.setOnClickListener{
            val action = MovieDetailFragmentDirections.actionMovieDetailFragmentToMoviesFragment()
            findNavController().navigate(action)
        }


    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}