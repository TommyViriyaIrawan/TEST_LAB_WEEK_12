package com.example.test_lab_week_12

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.bumptech.glide.Glide
import com.example.test_lab_week_12.model.Movie
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import androidx.lifecycle.Lifecycle

class DetailsActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_MOVIE_ID = "movie_id"
        private const val IMAGE_URL = "https://image.tmdb.org/t/p/w500/"
    }

    private lateinit var posterView: ImageView
    private lateinit var titleView: TextView
    private lateinit var releaseView: TextView
    private lateinit var overviewView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details)

        posterView = findViewById(R.id.movie_poster)
        titleView = findViewById(R.id.title_text)
        releaseView = findViewById(R.id.release_text)
        overviewView = findViewById(R.id.overview_text)

        val movieId = intent.getIntExtra(EXTRA_MOVIE_ID, 0)

        val repo = (application as MovieApplication).movieRepository
        val viewModel = ViewModelProvider(
            this,
            object : ViewModelProvider.Factory {
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return DetailsViewModel(repo) as T
                }
            }
        )[DetailsViewModel::class.java]

        viewModel.loadDetails(movieId)

        observeViewModel(viewModel)
    }

    private fun observeViewModel(viewModel: DetailsViewModel) {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {

                launch {
                    viewModel.movieDetails.collectLatest { movie ->
                        movie?.let { bindUI(it) }
                    }
                }

                launch {
                    viewModel.error.collectLatest { error ->
                        if (error.isNotBlank()) {
                            Snackbar.make(titleView, error, Snackbar.LENGTH_LONG).show()
                        }
                    }
                }
            }
        }
    }

    private fun bindUI(movie: Movie) {
        titleView.text = movie.title ?: "-"
        releaseView.text = movie.releaseDate?.take(4) ?: "-"
        overviewView.text = movie.overview ?: "-"

        Glide.with(this)
            .load("$IMAGE_URL${movie.posterPath}")
            .placeholder(R.mipmap.ic_launcher)
            .fitCenter()
            .into(posterView)
    }
}
