package com.shamalrathnayake.moviemate

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.google.android.material.chip.Chip
import com.shamalrathnayake.moviemate.adapters.TvShowAdapter
import com.shamalrathnayake.moviemate.databinding.FragmentTvShowDetailBinding
import jp.wasabeef.glide.transformations.BlurTransformation

class TvShowDetailFragment : Fragment() {

    private var _binding: FragmentTvShowDetailBinding? = null
    private val binding get() = _binding!!

    private var tvShowAdapter: TvShowAdapter = TvShowAdapter { tvShow -> }

    private val args: TvShowDetailFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentTvShowDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val tvShow = TvShowDetailFragmentArgs.fromBundle(requireArguments()).tvShow

        if (!tvShow.backdropPath.isNullOrEmpty()) {
            Glide.with(binding.backdropImage)
                .load("${BuildConfig.TMDB_IMAGE_BASE_URL}w500${tvShow.backdropPath}")
                .transform(BlurTransformation(25, 3))
                .into(binding.backdropImage)
        }

        binding.apply {
            tvShowTitle.text = tvShow.name
            tvShowYear.text = tvShow.firstAirDate.toString().split("-")[0]
            tvShowRating.text = String.format("⭐ %.1f", tvShow.voteAverage)
            tvShowLanguage.text = tvShow.language.toString().uppercase()

            val genreNames = tvShow.genreIds.mapNotNull { tvShowAdapter.getGenreMap()[it] }
            binding.genreChipGroup.removeAllViews()

            for (genre in genreNames) {
                val chip = Chip(context).apply {
                    text = genre
                    isClickable = false
                    isCheckable = false
                }
                binding.genreChipGroup.addView(chip)
            }

            tvShowOverview.text = tvShow.overview

            Glide.with(tvShowPoster)
                .load("${BuildConfig.TMDB_IMAGE_BASE_URL}w500${tvShow.posterPath}")
                .into(tvShowPoster)
        }

        binding.backButton.setOnClickListener {
            val action = TvShowDetailFragmentDirections.actionTvShowDetailFragmentToTvShowsFragment()
            findNavController().navigate(action)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
} 