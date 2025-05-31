package com.shamalrathnayake.moviemate.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.shamalrathnayake.moviemate.BuildConfig
import com.shamalrathnayake.moviemate.api.models.TvShow
import com.shamalrathnayake.moviemate.databinding.MovieCardBinding

class TvShowAdapter(
    private val onTvShowClick: (TvShow) -> Unit
) : ListAdapter<TvShow, TvShowAdapter.TvShowViewHolder>(TvShowDiffCallback()) {

    private val categories = listOf(
        "On The Air" to "on_the_air",
        "Popular" to "popular",
        "Top Rated" to "top_rated",
        "Airing Today" to "airing_today",
        "Action" to "10759",
        "Adventure" to "10759",
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
        "Thriller" to "53",
        "War" to "10752",
        "Western" to "37"
    )

    private val genreMap = mapOf(
        10759 to "Action & Adventure",
        16 to "Animation",
        35 to "Comedy",
        80 to "Crime",
        99 to "Documentary",
        18 to "Drama",
        10751 to "Family",
        14 to "Fantasy",
        36 to "History",
        27 to "Horror",
        10402 to "Music",
        9648 to "Mystery",
        10749 to "Romance",
        878 to "Science Fiction",
        53 to "Thriller",
        10752 to "War",
        37 to "Western"
    )

    fun getCategories(): List<Pair<String, String>> {
        return categories
    }

    fun getGenreMap(): Map<Int, String> {
        return genreMap
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): TvShowViewHolder {
        val binding = MovieCardBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return TvShowViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TvShowViewHolder, position: Int) {
        val tvShow = getItem(position)
        holder.bind(tvShow)
        holder.itemView.setOnClickListener {
            onTvShowClick(tvShow)
        }
    }

    class TvShowViewHolder(
        private val binding: MovieCardBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(tvShow: TvShow) {
            binding.apply {
                movieTitle.text = tvShow.name
                movieYear.text = tvShow.firstAirDate.toString().split("-")[0]
                movieRating.text = String.format("⭐ %.1f", tvShow.voteAverage)

                Glide.with(posterImage)
                    .load("${BuildConfig.TMDB_IMAGE_BASE_URL}w342${tvShow.posterPath}")
                    .into(posterImage)
            }
        }
    }

    private class TvShowDiffCallback : DiffUtil.ItemCallback<TvShow>() {
        override fun areItemsTheSame(oldItem: TvShow, newItem: TvShow): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: TvShow, newItem: TvShow): Boolean {
            return oldItem == newItem
        }
    }
} 